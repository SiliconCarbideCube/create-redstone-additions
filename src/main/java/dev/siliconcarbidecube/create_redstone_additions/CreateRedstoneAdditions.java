package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.init.*;

import net.fabricmc.api.ModInitializer;

public class CreateRedstoneAdditions implements ModInitializer {
    public static final String MOD_ID = "create_redstone_additions";

    @Override
    public void onInitialize() {
        CreateRedstoneAdditionsModBlocks.registerBlocks();
        CreateRedstoneAdditionsModItems.registerItems();
        CreateRedstoneAdditionsModTabs.registerTabs();
    }
}
