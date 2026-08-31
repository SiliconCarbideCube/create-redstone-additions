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

public class Disjunctor extends AbstractCustomDiodeBlock {
    public Disjunctor(Properties properties) {
        super(Disjunctor::new);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWERED, shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        Direction dirFront = state.getValue(FACING);
        Direction dirRight = dirFront.getClockWise();
        Direction dirLeft = dirFront.getCounterClockWise();
        return (direction == dirFront || direction == dirRight || direction == dirLeft);
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
        return this.getInputSignal(p_52502_, p_52503_, p_52504_) > 0;
    }
    protected int getInputSignal(Level p_52544_, BlockPos p_52545_, BlockState p_52546_) {
        Direction direction = p_52546_.getValue(FACING);
        Direction directionRight = direction.getCounterClockWise();
        Direction directionLeft = direction.getClockWise();
        BlockPos blockposRight = p_52545_.relative(directionRight);
        BlockPos blockposLeft = p_52545_.relative(directionLeft);
        if ((p_52544_.getSignal(blockposRight, directionRight) >= 1) || (p_52544_.getSignal(blockposLeft, directionLeft) >= 1)) {
            return 15;
        } else {
            return 0;
        }
    }

}