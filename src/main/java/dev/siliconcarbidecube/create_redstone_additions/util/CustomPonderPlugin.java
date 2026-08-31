package dev.siliconcarbidecube.create_redstone_additions.util;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import dev.siliconcarbidecube.create_redstone_additions.ponder.PonderTags;
import dev.siliconcarbidecube.create_redstone_additions.ponder.PonderScenes;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import org.jetbrains.annotations.NotNull;

public class CustomPonderPlugin implements PonderPlugin {

    @Override
    public @NotNull String getModId() {
        return CreateRedstoneAdditions.MOD_ID;
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTags.register(helper);
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderScenes.register(helper);
    }
}
