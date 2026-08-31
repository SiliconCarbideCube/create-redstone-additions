package dev.siliconcarbidecube.create_redstone_additions.blocks;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Inverter extends AbstractCustomDiodeBlock {
    public Inverter(Properties properties) {
        super(Inverter::new);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWERED, shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction != null) {
            Direction dirFront = state.getValue(FACING);
            Direction dirBack = dirFront.getOpposite();
            return (direction == dirFront || direction == dirBack);
        }
        return true;
    }

    public int getSignal(BlockState blockState, BlockGetter getter, BlockPos pos, Direction dir) {
        if (!(Boolean)blockState.getValue(POWERED)) {
            return 0;
        } else {
            return blockState.getValue(FACING) == dir ? this.getOutputSignal(getter, pos, blockState) : 0;
        }
    }

    protected int getOutputSignal(BlockGetter getter, BlockPos pos, BlockState blockState) {
        return 15;
    }

    protected boolean shouldTurnOn(Level p_52502_, BlockPos p_52503_, BlockState p_52504_) {
        return !(this.getInputSignal(p_52502_, p_52503_, p_52504_) > 0);
    }
    protected int getInputSignal(Level p_52544_, BlockPos p_52545_, BlockState p_52546_) {
        Direction direction = p_52546_.getValue(FACING);
        BlockPos blockpos = p_52545_.relative(direction);
        int i = p_52544_.getSignal(blockpos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState blockstate = p_52544_.getBlockState(blockpos);
            return Math.max(i, blockstate.is(Blocks.REDSTONE_WIRE) ? blockstate.getValue(RedStoneWireBlock.POWER) : 0);
        }
    }


}