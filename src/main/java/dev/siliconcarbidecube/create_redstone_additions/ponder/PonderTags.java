package dev.siliconcarbidecube.create_redstone_additions.ponder;

import dev.siliconcarbidecube.create_redstone_additions.util.ResourcePathfinder;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;

public class PonderTags {

    public static final ResourceLocation
    CREATE_REDSTONE_ADDITIONS_PONDER = ResourcePathfinder.locate("create_redstone_additions_ponder");

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                item -> BuiltInRegistries.ITEM.getKey(item.asItem()));

        helper.registerTag(CREATE_REDSTONE_ADDITIONS_PONDER)
                .addToIndex()
                .item(ModItems.CYAN_QUARTZ, true, false)
                .title("Redstone Additions")
                .description("Additional Redstone based blocks")
                .register();

        itemHelper.addToTag(CREATE_REDSTONE_ADDITIONS_PONDER)
                .add(ModBlocks.DIODE.get())
                .add(ModBlocks.GOLDEN_RESISTOR.get())
                .add(ModBlocks.IRON_RESISTOR.get())
                .add(ModBlocks.CERAMIC_RESISTOR.get())
                .add(ModBlocks.GLASS_RESISTOR.get())
                .add(ModBlocks.CROSSROAD.get())
                .add(ModBlocks.INVERTER.get())
                .add(ModBlocks.CONJUNCTOR.get())
                .add(ModBlocks.DISJUNCTOR.get());
    }

}
