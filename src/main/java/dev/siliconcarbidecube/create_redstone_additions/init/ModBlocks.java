package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;

import dev.siliconcarbidecube.create_redstone_additions.blocks.*;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;

public class ModBlocks {
    public static final DeferredRegister<Block> REGISTRY =
            DeferredRegister.create("create_redstone_additions", Registries.BLOCK);

    public static final RegistrySupplier<Block> DIODE_BLOCK =
            REGISTRY.register("diode", Diode::new);

    public static final RegistrySupplier<Block> CROSSROAD_BLOCK =
            REGISTRY.register("crossroad", Crossroad::new);

    public static final RegistrySupplier<Block> INVERTER_BLOCK =
            REGISTRY.register("inverter", Inverter::new);

    public static final RegistrySupplier<Block> GOLDEN_RESISTOR_BLOCK =
            REGISTRY.register("golden_resistor", ResistorI::new);

    public static final RegistrySupplier<Block> IRON_RESISTOR_BLOCK =
            REGISTRY.register("iron_resistor", ResistorII::new);

    public static final RegistrySupplier<Block> CERAMIC_RESISTOR_BLOCK =
            REGISTRY.register("ceramic_resistor", ResistorIII::new);

    public static final RegistrySupplier<Block> GLASS_RESISTOR_BLOCK =
            REGISTRY.register("glass_resistor", ResistorIV::new);

    public static final RegistrySupplier<Block> CONJUNCTOR_BLOCK =
            REGISTRY.register("conjunctor", Conjunctor::new);

    public static final RegistrySupplier<Block> DISJUNCTOR_BLOCK =
            REGISTRY.register("disjunctor", Disjunctor::new);


    public static void register() {
        REGISTRY.register();
    }
}
