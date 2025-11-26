package dev.siliconcarbidecube.create_redstone_additions.forge;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.architectury.platform.forge.EventBuses;
import dev.siliconcarbidecube.create_redstone_additions.util.CustomPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateRedstoneAdditions.MOD_ID)
public final class CreateRedstoneAdditionsForge {

    public CreateRedstoneAdditionsForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(CreateRedstoneAdditions.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        CreateRedstoneAdditions.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new CustomPonderPlugin());
    }

}
