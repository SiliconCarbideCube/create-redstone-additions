package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModItems;
import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModTabs;
import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsPonderPlugin;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.createmod.ponder.foundation.PonderIndex;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreateRedstoneAdditions.MODID)
public class CreateRedstoneAdditions
{
    public static final DirectionProperty DIRECTION = DirectionProperty.create("direction");
    public static final String MODID = "create_redstone_additions";

    public CreateRedstoneAdditions() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        CreateRedstoneAdditionsModBlocks.REGISTRY.register(modEventBus);
        CreateRedstoneAdditionsModItems.REGISTRY.register(modEventBus);
        CreateRedstoneAdditionsModTabs.REGISTRY.register(modEventBus);

        PonderIndex.addPlugin(new CreateRedstoneAdditionsPonderPlugin());
    }
}
