package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.ModInteracts;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import dev.siliconcarbidecube.create_redstone_additions.init.ModTabs;
import net.minecraft.resources.ResourceLocation;

public final class CreateRedstoneAdditions {
    public static final String MOD_ID = "create_redstone_additions";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        ModBlocks.register();
        ModItems.register();
        ModTabs.register();
        ModInteracts.register();
    }

}
