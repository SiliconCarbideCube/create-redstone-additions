package dev.siliconcarbidecube.create_redstone_additions.util;

import dev.architectury.platform.Platform;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

public class VerdigrisCheck {
    private static final ResourceLocation CYAN_QUARTZ_RECIPE =
            new ResourceLocation("create_redstone_additions", "cyan_quartz");

    public static void init() {
        LifecycleEvent.SERVER_STARTING.register(VerdigrisCheck::onServerStarting);
    }

    private static void onServerStarting(MinecraftServer server) {
        if (Platform.isModLoaded("copperative")) {
            RecipeManager manager = server.getRecipeManager();

            manager.byKey(CYAN_QUARTZ_RECIPE).ifPresent(recipe -> {
                manager.getRecipes().remove(recipe);
            });
        }
    }
}
