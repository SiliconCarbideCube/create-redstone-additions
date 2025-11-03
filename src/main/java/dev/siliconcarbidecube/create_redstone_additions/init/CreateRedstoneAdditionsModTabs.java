package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreateRedstoneAdditionsModTabs {
    public static CreativeModeTab REDSTONE_ADDITIONS_TAB;

    public static void registerTabs() {
        REDSTONE_ADDITIONS_TAB = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID),
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                        .title(Component.translatable("tab.create_redstone_additions"))
                        .icon(() -> new ItemStack(CreateRedstoneAdditionsModItems.CYAN_QUARTZ))
                        .displayItems((parameters, output) -> {
                            output.accept(CreateRedstoneAdditionsModItems.CYAN_QUARTZ);
                            output.accept(CreateRedstoneAdditionsModItems.POLISHED_CYAN_QUARTZ);
                            output.accept(CreateRedstoneAdditionsModItems.OXIDIZED_COPPER_INGOT);
                            output.accept(CreateRedstoneAdditionsModItems.OXIDIZED_COPPER_NUGGET);

                            output.accept(CreateRedstoneAdditionsModBlocks.CONJUNCTOR_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.DIODE_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.DISJUNCTOR_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.INVERTER_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.GOLDEN_RESISTOR_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.IRON_RESISTOR_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.CERAMIC_RESISTOR_BLOCK);
                            output.accept(CreateRedstoneAdditionsModBlocks.GLASS_RESISTOR_BLOCK);

                        })
                        .build()
        );
    }
}
