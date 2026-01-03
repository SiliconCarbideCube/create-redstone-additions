package dev.siliconcarbidecube.create_redstone_additions.neoforge.ponder;

import com.mojang.logging.LogUtils;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.ponder.PonderScenes;
import dev.siliconcarbidecube.create_redstone_additions.ponder.PonderTags;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CreateRedstoneAdditionsPonderPlugin implements PonderPlugin {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public @NotNull String getModId() {
        return CreateRedstoneAdditions.MOD_ID;
    }

    @Override
    public void registerTags(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
        LOGGER.info("[{}] Registering Ponder tags", CreateRedstoneAdditions.MOD_ID);
        PonderTags.register(helper);
    }

    @Override
    public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        LOGGER.info("[{}] Registering Ponder scenes", CreateRedstoneAdditions.MOD_ID);
        PonderScenes.register(helper);
    }
}
