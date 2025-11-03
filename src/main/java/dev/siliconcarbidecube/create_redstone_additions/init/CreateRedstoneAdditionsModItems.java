package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.items.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import static dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModBlocks.*;

public class CreateRedstoneAdditionsModItems {

    public static Item CYAN_QUARTZ;
    public static Item POLISHED_CYAN_QUARTZ;
    public static Item OXIDIZED_COPPER_INGOT;
    public static Item OXIDIZED_COPPER_NUGGET;

    public static void registerItems() {
        CYAN_QUARTZ = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "cyan_quartz"),
                new CyanQuartz()
        );

        POLISHED_CYAN_QUARTZ = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "polished_cyan_quartz"),
                new PolishedCyanQuartz()
        );

        OXIDIZED_COPPER_INGOT = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "oxidized_copper_ingot"),
                new OxidizedCopperIngot()
        );

        OXIDIZED_COPPER_NUGGET = Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "oxidized_copper_nugget"),
                new OxidizedCopperNugget()
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "diode"),
                new BlockItem(DIODE_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "conjunctor"),
                new BlockItem(CONJUNCTOR_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "crossroad"),
                new BlockItem(CROSSROAD_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "disjunctor"),
                new BlockItem(DISJUNCTOR_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "inverter"),
                new BlockItem(INVERTER_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "golden_resistor"),
                new BlockItem(GOLDEN_RESISTOR_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "iron_resistor"),
                new BlockItem(IRON_RESISTOR_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "ceramic_resistor"),
                new BlockItem(CERAMIC_RESISTOR_BLOCK, new Item.Properties())
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "glass_resistor"),
                new BlockItem(GLASS_RESISTOR_BLOCK, new Item.Properties())
        );

    }
}

