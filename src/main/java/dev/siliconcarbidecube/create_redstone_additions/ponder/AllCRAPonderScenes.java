package dev.siliconcarbidecube.create_redstone_additions.ponder;

import dev.siliconcarbidecube.create_redstone_additions.ponder.scenes.*;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class AllCRAPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "diode"),
                "diode",
                DiodeScene::diode
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "conjunctor"),
                "conjunctor",
                ConjunctorScene::conjunctor
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "disjunctor"),
                "disjunctor",
                DisjunctorScene::disjunctor
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "inverter"),
                "inverter",
                InverterScene::inverter
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "crossroad"),
                "crossroad",
                CrossroadScene::crossroad
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "golden_resistor"),
                "resistors",
                ResistorsScene::resistors
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "iron_resistor"),
                "resistors",
                ResistorsScene::resistors
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "ceramic_resistor"),
                "resistors",
                ResistorsScene::resistors
        );

        helper.addStoryBoard(
                new ResourceLocation("create_redstone_additions", "glass_resistor"),
                "resistors",
                ResistorsScene::resistors
        );

    }
}
