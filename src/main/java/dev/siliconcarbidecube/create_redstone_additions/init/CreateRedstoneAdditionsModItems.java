package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.siliconcarbidecube.create_redstone_additions.items.CyanQuartz;
import dev.siliconcarbidecube.create_redstone_additions.items.IntegratedCircuit;
import dev.siliconcarbidecube.create_redstone_additions.items.PolishedCyanQuartz;
import dev.siliconcarbidecube.create_redstone_additions.items.OxidizedCopperIngot;
import dev.siliconcarbidecube.create_redstone_additions.items.OxidizedCopperNugget;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CreateRedstoneAdditionsModItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, "create_redstone_additions");

    public static final RegistryObject<Item> DIODE_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.DIODE_BLOCK);

    public static final RegistryObject<Item> GOLDEN_RESISTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.GOLDEN_RESISTOR_BLOCK);

    public static final RegistryObject<Item> IRON_RESISTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.IRON_RESISTOR_BLOCK);

    public static final RegistryObject<Item> CERAMIC_RESISTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.CERAMIC_RESISTOR_BLOCK);

    public static final RegistryObject<Item> GLASS_RESISTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.GLASS_RESISTOR_BLOCK);

    public static final RegistryObject<Item> CROSSROAD_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK);

    public static final RegistryObject<Item> CONJUNCTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.CONJUNCTOR_BLOCK);

    public static final RegistryObject<Item> DISJUNCTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.DISJUNCTOR_BLOCK);

    public static final RegistryObject<Item> EXCLUSIVE_DISJUNCTOR_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.EXCLUSIVE_DISJUNCTOR_BLOCK);

    public static final RegistryObject<Item> INVERTER_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.INVERTER_BLOCK);

//    public static final RegistryObject<Item> METRONOME_BLOCK_ITEM = block(CreateRedstoneAdditionsModBlocks.METRONOME_BLOCK);

    public static final RegistryObject<Item> CYAN_QUARTZ = REGISTRY.register("cyan_quartz", () -> new CyanQuartz());

    public static final RegistryObject<Item> POLISHED_CYAN_QUARTZ = REGISTRY.register("polished_cyan_quartz", () -> new PolishedCyanQuartz());

//    public static final RegistryObject<Item> INTEGRATED_CIRCUIT = REGISTRY.register("integrated_circuit", () -> new IntegratedCircuit());

    public static final RegistryObject<Item> OXIDIZED_COPPER_INGOT = REGISTRY.register("oxidized_copper_ingot", () -> new OxidizedCopperIngot());

    public static final RegistryObject<Item> OXIDIZED_COPPER_NUGGET = REGISTRY.register("oxidized_copper_nugget", () -> new OxidizedCopperNugget());

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem((Block)block.get(), new Item.Properties()));
    }
}
