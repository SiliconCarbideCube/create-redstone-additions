package dev.siliconcarbidecube.create_redstone_additions.ponder;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.ponder.scenes.*;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class PonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {

        helper.addStoryBoard(
                ModBlocks.DIODE.getId(),
                "diode",
                DiodeScene::diode
        );

        helper.addStoryBoard(
                ModBlocks.CONJUNCTOR.getId(),
                "conjunctor",
                ConjunctorScene::conjunctor
        );

        helper.addStoryBoard(
                ModBlocks.DISJUNCTOR.getId(),
                "disjunctor",
                DisjunctorScene::disjunctor
        );

        helper.addStoryBoard(
                ModBlocks.INVERTER.getId(),
                "inverter",
                InverterScene::inverter
        );

        helper.addStoryBoard(
                ModBlocks.CROSSROAD.getId(),
                "crossroad",
                CrossroadScene::crossroad
        );

        helper.addStoryBoard(
                ModBlocks.GOLDEN_RESISTOR.getId(),
                "resistors",
                ResistorsScene::resistors
        );

        helper.addStoryBoard(
                ModBlocks.IRON_RESISTOR.getId(),
                "resistors",
                ResistorsScene::resistors
        );

        helper.addStoryBoard(
                ModBlocks.CERAMIC_RESISTOR.getId(),
                "resistors",
                ResistorsScene::resistors
        );

        helper.addStoryBoard(
                ModBlocks.GLASS_RESISTOR.getId(),
                "resistors",
                ResistorsScene::resistors
        );

    }
}
