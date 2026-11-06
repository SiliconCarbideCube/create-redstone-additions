package dev.siliconcarbidecube.create_redstone_additions.test;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.serialization.Lifecycle;
import net.minecraft.SystemReport;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.debugchart.LocalSampleLogger;
import net.minecraft.util.debugchart.SampleLogger;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * One superflat server per test JVM, with a real overworld (NeoForge's testframework ephemeral JUnit server has none).
 * The world is frozen: game ticks only advance through {@link #tick(int)}, so tests are deterministic.
 * Every world access must go through {@link #call}/{@link #run}, which execute on the server thread.
 */
public final class TestServer {
    private static final AtomicReference<WorldServer> SERVER = new AtomicReference<>();
    private static final long BOOT_TIMEOUT_SECONDS = 120;
    private static WorldServer instance;

    private TestServer() {}

    public static synchronized MinecraftServer get() {
        if (instance == null) {
            instance = boot();
        }
        return instance;
    }

    public static ServerLevel level() {
        return get().overworld();
    }

    public static <T> T call(Supplier<T> task) {
        MinecraftServer server = get();
        if (server.isSameThread()) {
            return task.get();
        }
        try {
            return server.submit(task).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof RuntimeException r) throw r;
            if (e.getCause() instanceof Error err) throw err;
            throw e;
        }
    }

    public static void run(Runnable task) {
        call(() -> {
            task.run();
            return null;
        });
    }

    /** Advances the frozen world by exactly {@code ticks} game ticks (block ticks, entities, block events). */
    public static void tick(int ticks) {
        if (ticks <= 0) return;
        MinecraftServer server = get();
        if (server.isSameThread()) {
            throw new IllegalStateException("tick() cannot be called from the server thread");
        }
        run(() -> server.tickRateManager().stepGameIfPaused(ticks));
        while (call(() -> server.tickRateManager().frozenTicksToRun()) > 0) {
            Thread.onSpinWait();
        }
    }

    private static WorldServer boot() {
        try {
            Path tempDir = Files.createTempDirectory("crt-test-server-");
            LevelStorageSource.LevelStorageAccess access = LevelStorageSource.createDefault(tempDir.resolve("world")).validateAndCreateAccess("main");
            PackRepository packs = ServerPacksSource.createPackRepository(access);
            WorldServer server = MinecraftServer.spin(thread -> WorldServer.create(thread, tempDir, access, packs));
            Runtime.getRuntime().addShutdownHook(new Thread(server::stopServer));

            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(BOOT_TIMEOUT_SECONDS);
            while (SERVER.get() == null) {
                if (System.nanoTime() > deadline || !server.getRunningThread().isAlive()) {
                    throw new IllegalStateException("Test server failed to start");
                }
                Thread.sleep(50);
            }
            server.submit(() -> server.tickRateManager().setFrozen(true)).join();
            return server;
        } catch (Exception e) {
            throw new IllegalStateException("Test server failed to start", e);
        }
    }

    private static final class WorldServer extends MinecraftServer {
        private static final Services NO_SERVICES = new Services(null, ServicesKeySet.EMPTY, null, null);
        private final Path tempDir;
        private final LocalSampleLogger sampleLogger = new LocalSampleLogger(1);

        private WorldServer(Thread thread, LevelStorageSource.LevelStorageAccess access, PackRepository packs, WorldStem stem, Path tempDir) {
            super(thread, access, packs, stem, Proxy.NO_PROXY, DataFixers.getDataFixer(), NO_SERVICES, LoggerChunkProgressListener::createFromGameruleRadius);
            this.tempDir = tempDir;
        }

        static WorldServer create(Thread thread, Path tempDir, LevelStorageSource.LevelStorageAccess access, PackRepository packs) {
            packs.reload();
            GameRules rules = new GameRules();
            rules.getRule(GameRules.RULE_SPAWN_CHUNK_RADIUS).set(0, null);
            rules.getRule(GameRules.RULE_RANDOMTICKING).set(0, null);
            rules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
            rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false, null);
            WorldDataConfiguration config = new WorldDataConfiguration(
                    new DataPackConfig(new ArrayList<>(packs.getAvailableIds()), List.of()), FeatureFlags.REGISTRY.allFlags());
            LevelSettings settings = new LevelSettings("Test Level", GameType.SURVIVAL, false, Difficulty.NORMAL, true, rules, config);
            WorldLoader.InitConfig initConfig = new WorldLoader.InitConfig(
                    new WorldLoader.PackConfig(packs, config, false, true), Commands.CommandSelection.DEDICATED, 4);
            try {
                WorldStem stem = Util.blockUntilDone(exec -> WorldLoader.load(
                        initConfig,
                        ctx -> {
                            Registry<LevelStem> stems = new MappedRegistry<>(Registries.LEVEL_STEM, Lifecycle.stable()).freeze();
                            WorldDimensions.Complete dimensions = ctx.datapackWorldgen()
                                    .registryOrThrow(Registries.WORLD_PRESET)
                                    .getHolderOrThrow(WorldPresets.FLAT)
                                    .value()
                                    .createWorldDimensions()
                                    .bake(stems);
                            return new WorldLoader.DataLoadOutput<>(
                                    new PrimaryLevelData(settings, new WorldOptions(0L, false, false), dimensions.specialWorldProperty(), dimensions.lifecycle()),
                                    dimensions.dimensionsRegistryAccess());
                        },
                        WorldStem::new,
                        Util.backgroundExecutor(),
                        exec)).get();
                return new WorldServer(thread, access, packs, stem, tempDir);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to load test server data", e);
            }
        }

        @Override
        public boolean initServer() {
            setPlayerList(new PlayerList(this, registries(), playerDataStorage, 1) {});
            ServerLifecycleHooks.handleServerAboutToStart(this);
            loadLevel();
            ServerLifecycleHooks.handleServerStarting(this);
            return true;
        }

        @Override
        public void tickServer(BooleanSupplier hasTimeLeft) {
            super.tickServer(hasTimeLeft);
            SERVER.set(this);
        }

        @Override
        public void waitUntilNextTick() {
            runAllTasks();
        }

        @Override
        public boolean saveEverything(boolean suppressLog, boolean flush, boolean forced) {
            return false;
        }

        @Override
        public void stopServer() {
            getConnection().stop();
            getPlayerList().removeAll();
            try {
                storageSource.deleteLevel();
                storageSource.close();
                FileUtils.deleteDirectory(tempDir.toFile());
            } catch (IOException ignored) {
            }
        }

        @Override
        public SystemReport fillServerSystemReport(SystemReport report) {
            report.setDetail("Type", "Test server");
            return report;
        }

        @Override
        protected SampleLogger getTickTimeLogger() {
            return sampleLogger;
        }

        @Override
        public boolean isTickTimeLoggingEnabled() {
            return false;
        }

        @Override
        public boolean isHardcore() {
            return false;
        }

        @Override
        public int getOperatorUserPermissionLevel() {
            return 0;
        }

        @Override
        public int getFunctionCompilationLevel() {
            return 4;
        }

        @Override
        public boolean shouldRconBroadcast() {
            return false;
        }

        @Override
        public boolean isDedicatedServer() {
            return false;
        }

        @Override
        public int getRateLimitPacketsPerSecond() {
            return 0;
        }

        @Override
        public boolean isEpollEnabled() {
            return false;
        }

        @Override
        public boolean isCommandBlockEnabled() {
            return true;
        }

        @Override
        public boolean isPublished() {
            return false;
        }

        @Override
        public boolean shouldInformAdmins() {
            return false;
        }

        @Override
        public boolean isSingleplayerOwner(GameProfile profile) {
            return false;
        }
    }
}
