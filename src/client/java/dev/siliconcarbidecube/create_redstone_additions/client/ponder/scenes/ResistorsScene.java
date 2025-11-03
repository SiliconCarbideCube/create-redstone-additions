package dev.siliconcarbidecube.create_redstone_additions.client.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class ResistorsScene {
    public static void resistors(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("resistors", "Redstone Resistor Types");
        scene.configureBasePlate(0, 0, 9); // 9x7 baseplate
        scene.showBasePlate();

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().layersFrom(1), Direction.UP);
        scene.idle(20);

        // Positions
        BlockPos golden  = util.grid().at(3, 1, 1);
        BlockPos iron    = util.grid().at(3, 1, 3);
        BlockPos ceramic = util.grid().at(3, 1, 5);
        BlockPos glass   = util.grid().at(3, 1, 7);

        // Intro text
        scene.overlay().showText(80)
                .text("Resistors reduce the power of redstone depending on the type.")
                .colored(PonderPalette.WHITE)
                .placeNearTarget(); // centered
        scene.idle(100);

        scene.addKeyframe();

        // Golden resistor
        scene.overlay().showText(70)
                .text("Golden resistor reduces redstone power by 1.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(golden, Direction.UP));
        scene.idle(80);

        scene.addKeyframe();

        // Iron resistor
        scene.overlay().showText(70)
                .text("Iron resistor reduces redstone power by 2.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(iron, Direction.UP));
        scene.idle(80);

        scene.addKeyframe();

        // Ceramic resistor
        scene.overlay().showText(70)
                .text("Ceramic resistor reduces redstone power by 5.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(ceramic, Direction.UP));
        scene.idle(80);

        scene.addKeyframe();

        // Glass resistor
        scene.overlay().showText(70)
                .text("Glass resistor reduces redstone power by 10.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(glass, Direction.UP));
        scene.idle(100);
    }
}
