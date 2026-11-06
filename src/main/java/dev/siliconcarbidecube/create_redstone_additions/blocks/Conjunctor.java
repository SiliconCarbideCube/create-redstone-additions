package dev.siliconcarbidecube.create_redstone_additions.blocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Conjunctor extends AbstractCustomDiodeBlock {
    public static final IntegerProperty POWER_TYPE = IntegerProperty.create("power_type", 0, 3);

    public Conjunctor(Properties properties) {
        super(Conjunctor::new);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER_TYPE);
    }

    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWER_TYPE, this.getInputSignal(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        if (direction != null) {
            Direction dirFront = state.getValue(FACING);
            Direction dirRight = dirFront.getClockWise();
            Direction dirLeft = dirFront.getCounterClockWise();
            return (direction == dirFront || direction == dirRight || direction == dirLeft);
        }
        return true;
    }

    public int getSignal(BlockState blockState, BlockGetter getter, BlockPos pos, Direction dir) {
        if (blockState.getValue(POWER_TYPE) != 3) {
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
        if ((p_52544_.getSignal(blockposRight, directionRight) >= 1) && (p_52544_.getSignal(blockposLeft, directionLeft) >= 1)) {
            return 3;
        } else {
            if (p_52544_.getSignal(blockposLeft, directionLeft) >= 1) {
                return 2;
            } else if (p_52544_.getSignal(blockposRight, directionRight) >= 1) {
                return 1;
            }
            return 0;
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(level, pos)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
            return;
        }
        if (!level.isClientSide && !this.isLocked(level, pos, state)) {
            int powerType = this.getInputSignal(level, pos, state);
            if (state.getValue(POWER_TYPE) != powerType) {
                level.setBlock(pos, state.setValue(POWER_TYPE, powerType), 2);
            }
        }
    }

    public void tick(BlockState p_221065_, ServerLevel p_221066_, BlockPos p_221067_, RandomSource p_221068_) {
        if (!this.isLocked(p_221066_, p_221067_, p_221065_)) {
            int flag = p_221065_.getValue(POWER_TYPE);
            int flag1 = this.getInputSignal(p_221066_, p_221067_, p_221065_);
            if (flag != flag1) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(POWER_TYPE, flag1), 2);
            }

        }
    }

}
