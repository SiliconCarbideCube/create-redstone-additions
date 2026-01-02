package dev.siliconcarbidecube.create_redstone_additions.neoforge.integration;

import com.mojang.logging.LogUtils;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

@JeiPlugin
public class JeiIntegration implements IModPlugin {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final AtomicBoolean LOGGED = new AtomicBoolean(false);
    
    @Override
    public ResourceLocation getPluginUid() {
        if (LOGGED.compareAndSet(false, true)) {
            LOGGER.info("[{}] JEI plugin loaded", CreateRedstoneAdditions.MOD_ID);
        }
        return ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, "jei_plugin");
    }
}
