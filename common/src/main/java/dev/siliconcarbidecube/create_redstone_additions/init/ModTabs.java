package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions.MOD_ID;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> MY_TAB = REGISTER.register(
            "create_redstone_additions_tab",
            () -> CreativeTabRegistry.create(builder -> {
                builder.title(Component.translatable("tab.create_redstone_additions"))
                        .icon(() -> new ItemStack(ModItems.CYAN_QUARTZ.get()))
                        .displayItems((parameters, output) -> {
                            // Block Items
                            output.accept(ModItems.DIODE_BLOCK_ITEM.get());
                            output.accept(ModItems.CROSSROAD_BLOCK_ITEM.get());
                            output.accept(ModItems.GOLDEN_RESISTOR_BLOCK_ITEM.get());
                            output.accept(ModItems.IRON_RESISTOR_BLOCK_ITEM.get());
                            output.accept(ModItems.CERAMIC_RESISTOR_BLOCK_ITEM.get());
                            output.accept(ModItems.GLASS_RESISTOR_BLOCK_ITEM.get());
                            output.accept(ModItems.CONJUNCTOR_BLOCK_ITEM.get());
                            output.accept(ModItems.DISJUNCTOR_BLOCK_ITEM.get());
                            output.accept(ModItems.INVERTER_BLOCK_ITEM.get());

                            // Items
                            output.accept(ModItems.CYAN_QUARTZ.get());
                            output.accept(ModItems.POLISHED_CYAN_QUARTZ.get());
                            output.accept(ModItems.OXIDIZED_COPPER_INGOT.get());
                            output.accept(ModItems.OXIDIZED_COPPER_NUGGET.get());
                        });
            })
    );

    public static void register() {
        REGISTER.register();
    }
}
