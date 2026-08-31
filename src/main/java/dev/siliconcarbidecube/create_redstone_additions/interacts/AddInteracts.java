package dev.siliconcarbidecube.create_redstone_additions.interacts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.function.Consumer;

public class AddInteracts {

    public static final Consumer<PlayerInteractEvent.RightClickBlock> ON_RIGHT_CLICK_BLOCK =
            event -> {
                Level level = event.getLevel();
                BlockPos pos = event.getPos();
                BlockState state = level.getBlockState(pos);

                VerdigrisDrop.tryDrop(level, state, pos, event);
            };

    public static void add() {
        NeoForge.EVENT_BUS.addListener(ON_RIGHT_CLICK_BLOCK);
    }

}
