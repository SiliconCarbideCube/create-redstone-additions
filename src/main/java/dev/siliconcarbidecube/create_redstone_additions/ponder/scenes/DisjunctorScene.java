package dev.siliconcarbidecube.create_redstone_additions.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;

import dev.siliconcarbidecube.create_redstone_additions.blocks.Disjunctor;

public class DisjunctorScene {
    public static void disjunctor(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("disjunctor", "Redstone Disjunctor");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().layersFrom(1), Direction.UP);
        scene.idle(20);

        BlockPos lampPos = util.grid().at(4, 1, 3);
        BlockPos wireMiddle = util.grid().at(3, 1, 3);
        BlockPos torchLeft = util.grid().at(2, 1, 1);
        BlockPos wireLeft = util.grid().at(2, 1, 2);
        BlockPos disjPos = util.grid().at(2, 1, 3);
        BlockPos wireRight = util.grid().at(2, 1, 4);
        BlockPos torchRight = util.grid().at(2, 1, 5);

        scene.overlay().showText(50)
                .text("Disjunctor activates if it receives a signal from any side.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(disjPos, Direction.UP));
        scene.idle(60);

        scene.addKeyframe();

        scene.world().hideSection(util.select().position(torchRight), Direction.DOWN);
        scene.world().modifyBlock(wireRight, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);

        scene.overlay().showText(40)
                .text("Signal from left side is enough...")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(disjPos, Direction.UP));
        scene.idle(60);

        scene.addKeyframe();

        scene.world().showSection(util.select().position(torchRight), Direction.UP);
        scene.world().modifyBlock(wireRight, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().hideSection(util.select().position(torchLeft), Direction.DOWN);
        scene.world().modifyBlock(wireLeft, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);

        scene.overlay().showText(40)
                .text("Signal from right side is also enough...")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(disjPos, Direction.UP));
        scene.idle(60);

        scene.addKeyframe();

        scene.world().hideSection(util.select().position(torchRight), Direction.DOWN);
        scene.world().modifyBlock(wireRight, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().hideSection(util.select().position(torchLeft), Direction.DOWN);
        scene.world().modifyBlock(wireLeft, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(disjPos, state -> state.setValue(Disjunctor.POWERED, false), false);
        scene.world().modifyBlock(wireMiddle, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(lampPos, state -> state.setValue(RedstoneLampBlock.LIT, false), false);

        scene.overlay().showText(40)
                .text("Disjunctor stays deactivated if no signal is received from either side.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(disjPos, Direction.UP));
        scene.idle(60);

        scene.addKeyframe();

        scene.world().showSection(util.select().position(torchLeft), Direction.UP);
        scene.world().showSection(util.select().position(torchRight), Direction.UP);
        scene.world().modifyBlock(wireLeft, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(wireRight, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(disjPos, state -> state.setValue(Disjunctor.POWERED, true), false);
        scene.world().modifyBlock(wireMiddle, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(lampPos, state -> state.setValue(RedstoneLampBlock.LIT, true), false);

        scene.overlay().showText(50)
                .colored(PonderPalette.GREEN)
                .text("Activated if either side provides a signal.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(disjPos, Direction.UP));
        scene.idle(60);
    }
}
