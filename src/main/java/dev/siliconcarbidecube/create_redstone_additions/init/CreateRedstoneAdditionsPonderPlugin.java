package dev.siliconcarbidecube.create_redstone_additions.init;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import dev.siliconcarbidecube.create_redstone_additions.ponder.AllCRAPonderTags;
import dev.siliconcarbidecube.create_redstone_additions.ponder.AllCRAPonderScenes;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import org.jetbrains.annotations.NotNull;

public class CreateRedstoneAdditionsPonderPlugin implements PonderPlugin {

    @Override
    public @NotNull String getModId() {
        return CreateRedstoneAdditions.MODID;
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        AllCRAPonderTags.register(helper);
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        AllCRAPonderScenes.register(helper);
    }
}
