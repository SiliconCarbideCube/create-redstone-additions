package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.ModInteracts;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import dev.siliconcarbidecube.create_redstone_additions.init.ModTabs;

public final class CreateRedstoneAdditions {
    public static final String MOD_ID = "create_redstone_additions";

    public static void init() {
        ModBlocks.register();
        ModItems.register();
        ModTabs.register();
        ModInteracts.register();
    }

}
