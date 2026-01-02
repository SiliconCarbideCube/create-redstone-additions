package dev.siliconcarbidecube.create_redstone_additions.ponder;

import dev.siliconcarbidecube.create_redstone_additions.ponder.scenes.*;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ItemLike;

public class PonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {

        PonderSceneRegistrationHelper<ItemLike> itemHelper = helper.withKeyFunction(
                item -> BuiltInRegistries.ITEM.getKey(item.asItem()));

        itemHelper.addStoryBoard(
                ModBlocks.DIODE_BLOCK.get(),
                "diode",
                DiodeScene::diode
        );

        itemHelper.addStoryBoard(
                ModBlocks.CONJUNCTOR_BLOCK.get(),
                "conjunctor",
                ConjunctorScene::conjunctor
        );

        itemHelper.addStoryBoard(
                ModBlocks.DISJUNCTOR_BLOCK.get(),
                "disjunctor",
                DisjunctorScene::disjunctor
        );

        itemHelper.addStoryBoard(
                ModBlocks.INVERTER_BLOCK.get(),
                "inverter",
                InverterScene::inverter
        );

        itemHelper.addStoryBoard(
                ModBlocks.CROSSROAD_BLOCK.get(),
                "crossroad",
                CrossroadScene::crossroad
        );

        itemHelper.addStoryBoard(
                ModBlocks.GOLDEN_RESISTOR_BLOCK.get(),
                "resistors",
                ResistorsScene::resistors
        );

        itemHelper.addStoryBoard(
                ModBlocks.IRON_RESISTOR_BLOCK.get(),
                "resistors",
                ResistorsScene::resistors
        );

        itemHelper.addStoryBoard(
                ModBlocks.CERAMIC_RESISTOR_BLOCK.get(),
                "resistors",
                ResistorsScene::resistors
        );

        itemHelper.addStoryBoard(
                ModBlocks.GLASS_RESISTOR_BLOCK.get(),
                "resistors",
                ResistorsScene::resistors
        );

    }
}
