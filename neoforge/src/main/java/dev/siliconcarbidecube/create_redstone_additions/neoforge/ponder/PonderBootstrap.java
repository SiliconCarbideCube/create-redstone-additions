package dev.siliconcarbidecube.create_redstone_additions.neoforge.ponder;

import com.mojang.logging.LogUtils;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import org.slf4j.Logger;

public final class PonderBootstrap {

    private static final Logger LOGGER = LogUtils.getLogger();

    private PonderBootstrap() {
    }

    public static void onConstruct(final FMLConstructModEvent event) {
        // Register before Ponder does its normal indexing pass.
        LOGGER.info("[{}] Registering Ponder plugin", CreateRedstoneAdditions.MOD_ID);
        PonderIndex.addPlugin(new CreateRedstoneAdditionsPonderPlugin());
    }
}
