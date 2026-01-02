package dev.siliconcarbidecube.create_redstone_additions.neoforge.ponder;

import com.mojang.logging.LogUtils;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import org.slf4j.Logger;

@EventBusSubscriber(modid = CreateRedstoneAdditions.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PonderBootstrap {

    private static final Logger LOGGER = LogUtils.getLogger();

    private PonderBootstrap() {
    }

    @SubscribeEvent
    public static void onConstruct(final FMLConstructModEvent event) {
        // Register before Ponder does its normal indexing pass.
        LOGGER.info("[{}] Registering Ponder plugin", CreateRedstoneAdditions.MOD_ID);
        PonderIndex.addPlugin(new CreateRedstoneAdditionsPonderPlugin());
    }
}
