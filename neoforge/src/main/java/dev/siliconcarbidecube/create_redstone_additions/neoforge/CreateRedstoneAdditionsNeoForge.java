package dev.siliconcarbidecube.create_redstone_additions.neoforge;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(CreateRedstoneAdditions.MOD_ID)
public final class CreateRedstoneAdditionsNeoForge {

    public CreateRedstoneAdditionsNeoForge(IEventBus modEventBus) {
        CreateRedstoneAdditions.init();

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(dev.siliconcarbidecube.create_redstone_additions.neoforge.ponder.PonderBootstrap::onConstruct);
            
            // Register debug commands only in development environment
            if (!net.neoforged.fml.loading.FMLLoader.isProduction()) {
                NeoForge.EVENT_BUS.addListener(dev.siliconcarbidecube.create_redstone_additions.neoforge.debug.RecipeDebugCommands::register);
            }
        }
    }

}
