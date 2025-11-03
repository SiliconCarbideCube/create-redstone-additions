package dev.siliconcarbidecube.create_redstone_additions.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;

import dev.siliconcarbidecube.create_redstone_additions.blocks.Crossroad;

public class CrossroadScene {
    public static void crossroad(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("crossroad", "Redstone Crossroad");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().layersFrom(1), Direction.UP);
        scene.idle(20);

        BlockPos lampWest   = util.grid().at(1, 1, 3);
        BlockPos torchEast  = util.grid().at(5, 1, 3);
        BlockPos wireEast   = util.grid().at(4, 1, 3);
        BlockPos wireWest   = util.grid().at(2, 1, 3);

        BlockPos lampNorth  = util.grid().at(3, 1, 5);
        BlockPos torchSouth = util.grid().at(3, 1, 1);
        BlockPos wireSouth  = util.grid().at(3, 1, 2);
        BlockPos wireNorth  = util.grid().at(3, 1, 4);

        BlockPos crossroad  = util.grid().at(3, 1, 3);

        scene.overlay().showText(70)
                .text("The Crossroad bridges a redstone signal in one specific direction and that direction only.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(crossroad, Direction.UP));
        scene.idle(80);

        scene.idle(20);

        scene.overlay().showText(70)
                .text("It prevents redstone signals from spreading in all directions when wires intersect.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(crossroad, Direction.UP));
        scene.idle(80);

        scene.addKeyframe();

        scene.rotateCameraY(90);

        scene.world().modifyBlock(torchEast, state -> state.setValue(RedstoneTorchBlock.LIT, false), false);
        scene.world().modifyBlock(wireEast, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(wireWest, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(lampWest, state -> state.setValue(RedstoneLampBlock.LIT, false), false);
        scene.world().modifyBlock(crossroad, state -> state
                .setValue(Crossroad.FLANK_POWER, 0)
                .setValue(Crossroad.MODEL_TYPE, 2), false);

        scene.overlay().showText(80)
                .text("Only the east–west redstone flow is deactivated. North–south remains unaffected.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(crossroad, Direction.UP));
        scene.idle(90);

        scene.addKeyframe();

        scene.world().modifyBlock(torchEast, state -> state.setValue(RedstoneTorchBlock.LIT, true), false);
        scene.world().modifyBlock(wireEast, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(wireWest, state -> state.setValue(RedStoneWireBlock.POWER, 13), false);
        scene.world().modifyBlock(lampWest, state -> state.setValue(RedstoneLampBlock.LIT, true), false);
        scene.world().modifyBlock(crossroad, state -> state
                .setValue(Crossroad.FLANK_POWER, 14)
                .setValue(Crossroad.MODEL_TYPE, 3), false);

        scene.addKeyframe();
        scene.rotateCameraY(-90);

        scene.world().modifyBlock(torchSouth, state -> state.setValue(RedstoneTorchBlock.LIT, false), false);
        scene.world().modifyBlock(wireSouth, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(wireNorth, state -> state.setValue(RedStoneWireBlock.POWER, 0), false);
        scene.world().modifyBlock(lampNorth, state -> state.setValue(RedstoneLampBlock.LIT, false), false);
        scene.world().modifyBlock(crossroad, state -> state
                .setValue(Crossroad.FLANK_POWER, 14)
                .setValue(Crossroad.MODEL_TYPE, 1), false);

        scene.overlay().showText(80)
                .text("Now only the north–south redstone flow is deactivated. East–west remains unaffected.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(crossroad, Direction.UP));
        scene.idle(90);

        scene.addKeyframe();

        scene.world().modifyBlock(torchSouth, state -> state.setValue(RedstoneTorchBlock.LIT, true), false);
        scene.world().modifyBlock(wireSouth, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(wireNorth, state -> state.setValue(RedStoneWireBlock.POWER, 13), false);
        scene.world().modifyBlock(lampNorth, state -> state.setValue(RedstoneLampBlock.LIT, true), false);
        scene.world().modifyBlock(crossroad, state -> state
                .setValue(Crossroad.FLANK_POWER, 14)
                .setValue(Crossroad.MODEL_TYPE, 3), false);

        scene.overlay().showText(80)
                .colored(PonderPalette.GREEN)
                .text("With the crossroad, flow of redstone signals is independent across opposite directions. Use it wisely!")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(crossroad, Direction.UP));
        scene.idle(100);
    }
}
