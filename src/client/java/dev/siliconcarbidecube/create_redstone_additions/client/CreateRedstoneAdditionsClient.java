package dev.siliconcarbidecube.create_redstone_additions.client;

import dev.siliconcarbidecube.create_redstone_additions.client.init.CreateRedstoneAdditionsPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.fabricmc.api.ClientModInitializer;

public class CreateRedstoneAdditionsClient implements ClientModInitializer {

    public static final String MOD_ID = "create_redstone_additions";

    @Override
    public void onInitializeClient() {
        PonderIndex.addPlugin(new CreateRedstoneAdditionsPonderPlugin());
    }
}
