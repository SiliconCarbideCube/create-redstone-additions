package dev.siliconcarbidecube.create_redstone_additions.test;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * An isolated, force-loaded 16x16x16 cell (one chunk, with an empty chunk between cells) in the shared test world.
 * All positions are local: (0..15, 0..15, 0..15); y=0 is a stone floor, so place test blocks at y=1 and above.
 * Methods may be called from the test thread; they run on the server thread.
 */
public final class TestArea implements AutoCloseable {
    public static final int SIZE = 16;
    private static final int CELLS_PER_ROW = 16;
    private static final AtomicInteger NEXT_CELL = new AtomicInteger();

    private final BlockPos origin;

    private TestArea(BlockPos origin) {
        this.origin = origin;
    }

    public static TestArea create() {
        int cell = NEXT_CELL.getAndIncrement();
        ChunkPos chunk = new ChunkPos(2 + (cell % CELLS_PER_ROW) * 2, 2 + (cell / CELLS_PER_ROW) * 2);
        TestArea area = new TestArea(new BlockPos(chunk.getMinBlockX(), 0, chunk.getMinBlockZ()));
        TestServer.run(() -> {
            ServerLevel level = TestServer.level();
            level.setChunkForced(chunk.x, chunk.z, true);
            level.getChunk(chunk.x, chunk.z);
            area.reset();
        });
        while (!TestServer.call(() -> TestServer.level().isPositionEntityTicking(area.origin))) {
            Thread.onSpinWait();
        }
        return area;
    }

    public ServerLevel level() {
        return TestServer.level();
    }

    public BlockPos abs(BlockPos local) {
        return origin.offset(local);
    }

    public BlockPos abs(int x, int y, int z) {
        return origin.offset(x, y, z);
    }

    public <T> T call(Function<ServerLevel, T> task) {
        return TestServer.call(() -> task.apply(TestServer.level()));
    }

    public void tick(int ticks) {
        TestServer.tick(ticks);
    }

    // Blocks

    public BlockState get(BlockPos local) {
        return call(level -> level.getBlockState(abs(local)));
    }

    /** Sets the exact state with full neighbour updates (flag 3). */
    public void set(BlockPos local, BlockState state) {
        call(level -> level.setBlock(abs(local), state, Block.UPDATE_ALL));
    }

    /** Like {@link #set} but first adapts the state to its neighbours (wire connections etc.), as a placement would. */
    public void place(BlockPos local, BlockState state) {
        call(level -> level.setBlock(abs(local), Block.updateFromNeighbourShapes(state, level, abs(local)), Block.UPDATE_ALL));
    }

    public void clear(BlockPos local) {
        set(local, Blocks.AIR.defaultBlockState());
    }

    public void wire(BlockPos local) {
        place(local, Blocks.REDSTONE_WIRE.defaultBlockState());
    }

    public void redstoneBlock(BlockPos local) {
        set(local, Blocks.REDSTONE_BLOCK.defaultBlockState());
    }

    /** Floor lever (attached to the block below), optionally already on. */
    public void lever(BlockPos local, boolean on) {
        set(local, Blocks.LEVER.defaultBlockState().setValue(LeverBlock.FACE, AttachFace.FLOOR).setValue(LeverBlock.FACING, Direction.NORTH));
        if (on) setLever(local, true);
    }

    public void setLever(BlockPos local, boolean on) {
        call(level -> {
            BlockState state = level.getBlockState(abs(local));
            if (state.getValue(LeverBlock.POWERED) != on) ((LeverBlock) Blocks.LEVER).pull(state, level, abs(local), null);
            return null;
        });
    }

    // Redstone

    /** Strongest signal the block at {@code local} receives from any neighbour. */
    public int power(BlockPos local) {
        return call(level -> level.getBestNeighborSignal(abs(local)));
    }

    /** Signal the block at {@code local} receives from its neighbour on {@code side}. */
    public int signalInto(BlockPos local, Direction side) {
        return call(level -> level.getSignal(abs(local).relative(side), side));
    }

    /** Signal the block at {@code local} emits toward its neighbour on {@code side}. */
    public int signalFrom(BlockPos local, Direction side) {
        return call(level -> level.getSignal(abs(local), side.getOpposite()));
    }

    public int wirePower(BlockPos local) {
        return get(local).getValue(RedStoneWireBlock.POWER);
    }

    // Players

    /** Survival fake player standing at the cell centre, looking {@code facing}, holding {@code stack} in the main hand. */
    public FakePlayer player(ItemStack stack, Direction facing) {
        return call(level -> {
            FakePlayer player = new FakePlayer(level, new GameProfile(UUID.randomUUID(), "crt_test"));
            player.setGameMode(GameType.SURVIVAL);
            Vec3 pos = Vec3.atBottomCenterOf(abs(SIZE / 2, 1, SIZE / 2));
            player.moveTo(pos.x, pos.y, pos.z, facing.toYRot(), 0);
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            return player;
        });
    }

    public FakePlayer player(ItemStack stack) {
        return player(stack, Direction.NORTH);
    }

    public BlockHitResult hit(BlockPos local, Direction face) {
        BlockPos pos = abs(local);
        return new BlockHitResult(Vec3.atCenterOf(pos).relative(face, 0.5), face, pos, false);
    }

    /** Full server right-click flow, as from a client packet: RightClickBlock event, block use, then item use. */
    public InteractionResult useItemOn(FakePlayer player, BlockPos local, Direction face, InteractionHand hand) {
        return call(level -> player.gameMode.useItemOn(player, level, player.getItemInHand(hand), hand, hit(local, face)));
    }

    public InteractionResult useItemOn(FakePlayer player, BlockPos local, Direction face) {
        return useItemOn(player, local, face, InteractionHand.MAIN_HAND);
    }

    /** Item use only ({@code ItemStack.useOn}): skips the RightClickBlock event and block use. */
    public InteractionResult useOn(FakePlayer player, BlockPos local, Direction face, InteractionHand hand) {
        return call(level -> player.getItemInHand(hand).useOn(new UseOnContext(player, hand, hit(local, face))));
    }

    public InteractionResult useOn(FakePlayer player, BlockPos local, Direction face) {
        return useOn(player, local, face, InteractionHand.MAIN_HAND);
    }

    /** Survival break by the player (BreakEvent, tool checks, loot drops as item entities). */
    public boolean breakBlock(FakePlayer player, BlockPos local) {
        return call(level -> player.gameMode.destroyBlock(abs(local)));
    }

    // Drops

    /** Loot the block at {@code local} would drop, without breaking it. */
    public List<ItemStack> drops(BlockPos local) {
        return call(level -> Block.getDrops(level.getBlockState(abs(local)), level, abs(local), level.getBlockEntity(abs(local))));
    }

    public List<ItemEntity> items(BlockPos local, double radius) {
        return call(level -> level.getEntities(EntityType.ITEM, new AABB(abs(local)).inflate(radius), Entity::isAlive));
    }

    /** Total count of {@code item} in item entities within {@code radius} of {@code local}. */
    public int countItems(Item item, BlockPos local, double radius) {
        return items(local, radius).stream().map(ItemEntity::getItem).filter(s -> s.is(item)).mapToInt(ItemStack::getCount).sum();
    }

    public void clearItems() {
        call(level -> {
            level.getEntities(EntityType.ITEM, bounds(), e -> true).forEach(Entity::discard);
            return null;
        });
    }

    private AABB bounds() {
        return new AABB(origin.getX(), origin.getY(), origin.getZ(), origin.getX() + SIZE, origin.getY() + SIZE, origin.getZ() + SIZE).inflate(2);
    }

    private void reset() {
        ServerLevel level = TestServer.level();
        int flags = Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE;
        for (BlockPos pos : BlockPos.betweenClosed(origin, origin.offset(SIZE - 1, SIZE - 1, SIZE - 1))) {
            level.setBlock(pos, pos.getY() == origin.getY() ? Blocks.STONE.defaultBlockState() : Blocks.AIR.defaultBlockState(), flags);
        }
        level.getEntities((Entity) null, bounds(), e -> !(e instanceof FakePlayer)).forEach(Entity::discard);
    }

    @Override
    public void close() {
        TestServer.run(this::reset);
    }
}
