package dev.siliconcarbidecube.create_redstone_additions;

import com.mojang.logging.LogUtils;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import dev.siliconcarbidecube.create_redstone_additions.init.ModTabs;
import dev.siliconcarbidecube.create_redstone_additions.interacts.AddInteracts;
import dev.siliconcarbidecube.create_redstone_additions.util.CustomPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(CreateRedstoneAdditions.MOD_ID)
public class CreateRedstoneAdditions {
    public static final String MOD_ID = "create_redstone_additions";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final NonNullSupplier<Registrate> REGISTRATE_SUPPLIER = NonNullSupplier.lazy(() -> Registrate.create(MOD_ID));
    public static final Registrate REGISTRATE = REGISTRATE_SUPPLIER.get();

    public CreateRedstoneAdditions(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        ModBlocks.register();
        ModItems.register();
        ModTabs.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("If you see this that means Create: Redstone Additions is loading. Hello there!");

        AddInteracts.add();
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new CustomPonderPlugin());
        }
    }
}
