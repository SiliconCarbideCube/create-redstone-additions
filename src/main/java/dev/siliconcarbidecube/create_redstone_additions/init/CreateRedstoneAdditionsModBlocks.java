package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.siliconcarbidecube.create_redstone_additions.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CreateRedstoneAdditionsModBlocks {

    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, "create_redstone_additions");

    public static final RegistryObject<Block> DIODE_BLOCK = REGISTRY.register("diode", () -> new Diode());

    public static final RegistryObject<Block> GOLDEN_RESISTOR_BLOCK = REGISTRY.register("golden_resistor", () -> new ResistorI());

    public static final RegistryObject<Block> IRON_RESISTOR_BLOCK = REGISTRY.register("iron_resistor", () -> new ResistorII());

    public static final RegistryObject<Block> CERAMIC_RESISTOR_BLOCK = REGISTRY.register("ceramic_resistor", () -> new ResistorIII());

    public static final RegistryObject<Block> GLASS_RESISTOR_BLOCK = REGISTRY.register("glass_resistor", () -> new ResistorIV());

    public static final RegistryObject<Block> CROSSROAD_BLOCK = REGISTRY.register("crossroad", () -> new Crossroad());
    
    public static final RegistryObject<Block> INVERTER_BLOCK = REGISTRY.register("inverter", () -> new Inverter());

    public static final RegistryObject<Block> CONJUNCTOR_BLOCK = REGISTRY.register("conjunctor", () -> new Conjunctor());

    public static final RegistryObject<Block> DISJUNCTOR_BLOCK = REGISTRY.register("disjunctor", () -> new Disjunctor());

    public static final RegistryObject<Block> EXCLUSIVE_DISJUNCTOR_BLOCK = REGISTRY.register("exclusive_disjunctor", () -> new ExclusiveDisjunctor());

//    public static final RegistryObject<Block> METRONOME_BLOCK = REGISTRY.register("metronome", () -> new Metronome());
}
