package dev.siliconcarbidecube.create_redstone_additions.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;

import dev.siliconcarbidecube.create_redstone_additions.blocks.Inverter;
import net.minecraft.world.phys.Vec3;

public class InverterScene {
    public static void inverter(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("inverter", "Redstone Inverter");
        scene.configureBasePlate(0, 0, 7);
        scene.showBasePlate();

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().layersFrom(1), Direction.UP);
        scene.idle(20);

        BlockPos lampPos = util.grid().at(5, 1, 3);
        BlockPos torchPos = util.grid().at(1, 1, 3);
        BlockPos wireLeft = util.grid().at(2, 1, 3);
        BlockPos inverter = util.grid().at(3, 1, 3);
        BlockPos wireRight = util.grid().at(4, 1, 3);

        scene.overlay().showText(60)
                .text("Inverter always emits a signal as long as there is no redstone input from its opposite side.")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(inverter, Direction.UP));
        scene.idle(70);

        scene.addKeyframe();

        scene.world().hideSection(util.select().position(torchPos), Direction.DOWN);
        scene.world().hideSection(util.select().position(wireLeft), Direction.DOWN);
        scene.idle(20);

        scene.world().hideSection(util.select().position(inverter), Direction.DOWN);
        scene.world().hideSection(util.select().position(wireRight), Direction.DOWN);
        scene.world().hideSection(util.select().position(lampPos), Direction.DOWN);
        scene.idle(20);

        scene.world().moveSection(
                scene.world().showIndependentSection(util.select().position(inverter), Direction.DOWN),
                new Vec3(-1, 0, 0), 15
        );

        scene.world().moveSection(
                scene.world().showIndependentSection(util.select().position(wireRight), Direction.DOWN),
                new Vec3(-1, 0, 0), 15
        );

        scene.world().moveSection(
                scene.world().showIndependentSection(util.select().position(lampPos), Direction.DOWN),
                new Vec3(-1, 0, 0), 15
        );

        scene.world().modifyBlock(inverter, state -> state.setValue(Inverter.POWERED, true), false);
        scene.world().modifyBlock(wireRight, state -> state.setValue(RedStoneWireBlock.POWER, 15), false);
        scene.world().modifyBlock(lampPos, state -> state.setValue(RedstoneLampBlock.LIT, true), false);

        scene.overlay().showText(50)
                .text("Inverter activates...")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(inverter.west(), Direction.UP));
        scene.idle(60);

        scene.overlay().showText(50)
                .colored(PonderPalette.GREEN)
                .text("Inverter always stays activated")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(inverter.west(), Direction.UP));
        scene.idle(60);
    }
}
