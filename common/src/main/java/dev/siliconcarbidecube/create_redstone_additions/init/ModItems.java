package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.siliconcarbidecube.create_redstone_additions.items.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create("create_redstone_additions", Registries.ITEM);

    // Block Items
    public static final RegistrySupplier<Item> DIODE_BLOCK_ITEM =
            REGISTRY.register("diode", () ->
                    new BlockItem(ModBlocks.DIODE_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> CROSSROAD_BLOCK_ITEM =
            REGISTRY.register("crossroad", () ->
                    new BlockItem(ModBlocks.CROSSROAD_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> INVERTER_BLOCK_ITEM =
            REGISTRY.register("inverter", () ->
                    new BlockItem(ModBlocks.INVERTER_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> GOLDEN_RESISTOR_BLOCK_ITEM =
            REGISTRY.register("golden_resistor", () ->
                    new BlockItem(ModBlocks.GOLDEN_RESISTOR_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> IRON_RESISTOR_BLOCK_ITEM =
            REGISTRY.register("iron_resistor", () ->
                    new BlockItem(ModBlocks.IRON_RESISTOR_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> CERAMIC_RESISTOR_BLOCK_ITEM =
            REGISTRY.register("ceramic_resistor", () ->
                    new BlockItem(ModBlocks.CERAMIC_RESISTOR_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> GLASS_RESISTOR_BLOCK_ITEM =
            REGISTRY.register("glass_resistor", () ->
                    new BlockItem(ModBlocks.GLASS_RESISTOR_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> CONJUNCTOR_BLOCK_ITEM =
            REGISTRY.register("conjunctor", () ->
                    new BlockItem(ModBlocks.CONJUNCTOR_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> DISJUNCTOR_BLOCK_ITEM =
            REGISTRY.register("disjunctor", () ->
                    new BlockItem(ModBlocks.DISJUNCTOR_BLOCK.get(), new Item.Properties()));

    // Items
    public static final RegistrySupplier<Item> CYAN_QUARTZ =
            REGISTRY.register("cyan_quartz", () ->
                    new CyanQuartz(new Item.Properties()));

    public static final RegistrySupplier<Item> POLISHED_CYAN_QUARTZ =
            REGISTRY.register("polished_cyan_quartz", () ->
                    new PolishedCyanQuartz(new Item.Properties()));

    public static final RegistrySupplier<Item> QUARTZ_SEMICONDUCTOR =
            REGISTRY.register("quartz_semiconductor", () ->
                    new QuartzSemiconductor(new Item.Properties()));

    public static final RegistrySupplier<Item> VERDIGRIS =
            REGISTRY.register("verdigris", () ->
                    new Verdigris(new Item.Properties()));

    public static void register() {
        REGISTRY.register();
    }
}
