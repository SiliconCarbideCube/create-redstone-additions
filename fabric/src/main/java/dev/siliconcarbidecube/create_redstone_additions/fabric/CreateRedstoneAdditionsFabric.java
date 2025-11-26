package dev.siliconcarbidecube.create_redstone_additions.fabric;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.fabricmc.api.ModInitializer;

public final class CreateRedstoneAdditionsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CreateRedstoneAdditions.init();
    }
}
