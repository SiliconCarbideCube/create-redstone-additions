package dev.siliconcarbidecube.create_redstone_additions.ponder;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;

public class PonderTags {

    public static final ResourceLocation REDSTONE_ADDITIONS = new ResourceLocation(CreateRedstoneAdditions.MOD_ID);

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                item -> BuiltInRegistries.ITEM.getKey(item.asItem()));

        helper.registerTag(REDSTONE_ADDITIONS)
                .addToIndex()
                .item(ModItems.CYAN_QUARTZ.get(), true, false)
                .title("Redstone Additions")
                .description("Additional Redstone based blocks")
                .register();

        itemHelper.addToTag(REDSTONE_ADDITIONS)
                .add(ModBlocks.DIODE_BLOCK.get())
                .add(ModBlocks.GOLDEN_RESISTOR_BLOCK.get())
                .add(ModBlocks.IRON_RESISTOR_BLOCK.get())
                .add(ModBlocks.CERAMIC_RESISTOR_BLOCK.get())
                .add(ModBlocks.GLASS_RESISTOR_BLOCK.get())
                .add(ModBlocks.CROSSROAD_BLOCK.get())
                .add(ModBlocks.INVERTER_BLOCK.get())
                .add(ModBlocks.CONJUNCTOR_BLOCK.get())
                .add(ModBlocks.DISJUNCTOR_BLOCK.get());
    }

}
