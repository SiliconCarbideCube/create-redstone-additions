package dev.siliconcarbidecube.create_redstone_additions.interacts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class VerdigrisDrop {
    public static void tryDrop(Level level, BlockState state, BlockPos pos, PlayerInteractEvent.RightClickBlock event) {
        if (!level.isClientSide && WeatheringCopper.getPrevious(state).isPresent()) {
            assert event.getFace() != null;
            UseOnContext ctx = new UseOnContext(
                    event.getEntity(),
                    event.getHand(),
                    new BlockHitResult(event.getHitVec().getLocation(), event.getFace(), pos, false)
            );

            BlockState newState = state.getToolModifiedState(ctx, ItemAbilities.AXE_SCRAPE, false);

            if (newState != null) {
                int count = 3 + level.random.nextInt(4);
                ItemStack drop = new ItemStack(ModItems.VERDIGRIS.get(), count);
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), drop);
            }
        }
    }
}

