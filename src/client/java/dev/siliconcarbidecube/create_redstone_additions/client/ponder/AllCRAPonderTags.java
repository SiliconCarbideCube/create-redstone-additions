package dev.siliconcarbidecube.create_redstone_additions.client.ponder;

import dev.siliconcarbidecube.create_redstone_additions.client.CreateRedstoneAdditionsClient;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModItems;

public class AllCRAPonderTags {

    public static final ResourceLocation REDSTONE_ADDITIONS = new ResourceLocation(CreateRedstoneAdditionsClient.MOD_ID);

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                item -> BuiltInRegistries.ITEM.getKey(item.asItem()));

        helper.registerTag(REDSTONE_ADDITIONS)
                .addToIndex()
                .item(CreateRedstoneAdditionsModItems.CYAN_QUARTZ, true, false)
                .title("Redstone Additions")
                .description("Additional Redstone based blocks")
                .register();

        itemHelper.addToTag(REDSTONE_ADDITIONS)
                .add(CreateRedstoneAdditionsModBlocks.DIODE_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.GOLDEN_RESISTOR_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.IRON_RESISTOR_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.CERAMIC_RESISTOR_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.GLASS_RESISTOR_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.INVERTER_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.CONJUNCTOR_BLOCK)
                .add(CreateRedstoneAdditionsModBlocks.DISJUNCTOR_BLOCK);
    }

}
