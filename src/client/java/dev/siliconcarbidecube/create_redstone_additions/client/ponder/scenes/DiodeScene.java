package dev.siliconcarbidecube.create_redstone_additions.client.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;

public class DiodeScene {
    public static void diode(SceneBuilder builder, SceneBuildingUtil util) {
        System.out.println("Diode scene is running!");

        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("diode", "The Diode");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().layersFrom(1), Direction.UP);
        scene.idle(20);

        BlockPos lampPos = util.grid().at(1, 1, 3);
        BlockPos wire1Pos = util.grid().at(2, 1, 3);
        BlockPos diodePos = util.grid().at(3, 1, 3);

        scene.overlay().showText(60)
                .text("A Diode passes the signal when cyan quartz anode is facing the signal direction, but unlike a repeater, does not refresh it.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(diodePos, Direction.UP));
        scene.idle(70);

        scene.addKeyframe();

        scene.world().hideSection(util.select().position(diodePos), Direction.DOWN);
        scene.idle(10);

        scene.world().rotateSection(
                scene.world().showIndependentSection(util.select().position(diodePos), Direction.DOWN),
                0, 180, 0, 20
        );
        scene.idle(20);

        scene.world().modifyBlock(wire1Pos, state ->
                state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(lampPos, state ->
                state.setValue(RedstoneLampBlock.LIT, false), false);

        scene.idle(10);

        scene.overlay().showText(50)
                .colored(PonderPalette.RED)
                .text("When reversed, rose quartz cathode is facing the signal direction thus, blocking it.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(diodePos, Direction.UP));
        scene.idle(60);
    }
}
