package dev.siliconcarbidecube.create_redstone_additions.util;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.minecraft.resources.ResourceLocation;

public class ResourcePathfinder {

    public static ResourceLocation locate(String path) {
        return ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, path);
    }

}
