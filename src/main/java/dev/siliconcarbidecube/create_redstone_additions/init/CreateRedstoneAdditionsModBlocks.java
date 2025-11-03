package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.blocks.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class CreateRedstoneAdditionsModBlocks {

    public static Block CONJUNCTOR_BLOCK;
    public static Block DIODE_BLOCK;
    public static Block CROSSROAD_BLOCK;
    public static Block DISJUNCTOR_BLOCK;
    public static Block INVERTER_BLOCK;
    public static Block GOLDEN_RESISTOR_BLOCK;
    public static Block IRON_RESISTOR_BLOCK;
    public static Block CERAMIC_RESISTOR_BLOCK;
    public static Block GLASS_RESISTOR_BLOCK;

    public static void registerBlocks() {
        DIODE_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "diode"),
                new Diode()
        );

        CONJUNCTOR_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "conjunctor"),
                new Conjunctor()
        );

        CROSSROAD_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "crossroad"),
                new Crossroad()
        );

        DISJUNCTOR_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "disjunctor"),
                new Disjunctor()
        );

        INVERTER_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "inverter"),
                new Inverter()
        );

        GOLDEN_RESISTOR_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "golden_resistor"),
                new ResistorI()
        );

        IRON_RESISTOR_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "iron_resistor"),
                new ResistorII()
        );

        CERAMIC_RESISTOR_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "ceramic_resistor"),
                new ResistorIII()
        );

        GLASS_RESISTOR_BLOCK = Registry.register(
                BuiltInRegistries.BLOCK,
                new ResourceLocation(CreateRedstoneAdditions.MOD_ID, "glass_resistor"),
                new ResistorIV()
        );
    }
}

