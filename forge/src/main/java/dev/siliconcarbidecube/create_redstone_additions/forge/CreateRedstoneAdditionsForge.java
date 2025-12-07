package dev.siliconcarbidecube.create_redstone_additions.forge;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.architectury.platform.forge.EventBuses;
import dev.siliconcarbidecube.create_redstone_additions.util.CustomPonderPlugin;
import dev.siliconcarbidecube.create_redstone_additions.util.VerdigrisCheck;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.world.level.block.RepeaterBlock;

@Mod(CreateRedstoneAdditions.MOD_ID)
public final class CreateRedstoneAdditionsForge {

    public CreateRedstoneAdditionsForge() {
        EventBuses.registerModEventBus(CreateRedstoneAdditions.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        CreateRedstoneAdditions.init();

        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onServerStarting(ServerStartingEvent event) {
        VerdigrisCheck.init();
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new CustomPonderPlugin());
    }

}
