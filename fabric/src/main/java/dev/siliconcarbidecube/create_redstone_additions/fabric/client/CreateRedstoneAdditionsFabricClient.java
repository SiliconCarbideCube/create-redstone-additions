package dev.siliconcarbidecube.create_redstone_additions.fabric.client;

import dev.siliconcarbidecube.create_redstone_additions.util.CustomPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;

public final class CreateRedstoneAdditionsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        PonderIndex.addPlugin(new CustomPonderPlugin());
    }
}
