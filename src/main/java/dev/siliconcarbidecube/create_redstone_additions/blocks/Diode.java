package dev.siliconcarbidecube.create_redstone_additions.blocks;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Diode extends AbstractCustomDiodeBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public Diode(Properties properties) {
        super(Diode::new);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER);
    }

    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWER, this.getInputSignal(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        Direction dirFront = state.getValue(FACING);
        Direction dirBack = dirFront.getOpposite();
        return (direction == dirFront || direction == dirBack);
    }

    public int getSignal(BlockState blockState, BlockGetter getter, BlockPos pos, Direction dir) {
        if (blockState.getValue(POWER) == 0) {
            return 0;
        } else {
            int i = this.getOutputSignal(getter, pos, blockState);
            BlockPos gpos = pos.relative(dir.getOpposite());
            BlockState gbs = getter.getBlockState(gpos);
            //return blockState.getValue(FACING) == dir ? ((blockStateS.is(Blocks.REDSTONE_WIRE) ? this.getOutputSignal(getter, pos, blockState) - 1 : this.getOutputSignal(getter, pos, blockState))) : 0;
            return blockState.getValue(FACING) == dir ? (gbs.is(Blocks.REDSTONE_WIRE) ? (i > 0 ? i - 1 : 0) : i) : 0;
        }
    }
    protected int getOutputSignal(BlockGetter getter, BlockPos pos, BlockState blockState) {
        return blockState.getValue(POWER);
    }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        return state.getValue(POWER) > 0;
    }
    
    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction);
        int i = level.getSignal(blockpos, direction);
        BlockState blockstate = level.getBlockState(blockpos);
        if (blockstate.is(ModBlocks.DIODE.get()) || blockstate.is(ModBlocks.CROSSROAD.get())){
            i = i!=0 ? i-1 : 0;
        }
        return blockstate.is(Blocks.REDSTONE_WIRE) ? (blockstate.getValue(RedStoneWireBlock.POWER) == 0 ? 0 : blockstate.getValue(RedStoneWireBlock.POWER) - 1) : i;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {
        if (!state.canSurvive(level, pos)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
            return;
        }
        if (!level.isClientSide && !this.isLocked(level, pos, state)) {
            int newPower = this.getInputSignal(level, pos, state);
            int oldPower = state.getValue(POWER);

            if (oldPower != newPower) {
                BlockState updated = state.setValue(POWER, newPower);
                level.setBlock(pos, updated, 2);
                this.updateNeighborsInFront(level, pos, updated);
            }

        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level,
                     BlockPos pos, RandomSource random) {
        if (!this.isLocked(level, pos, state)) {
            int newPower = this.getInputSignal(level, pos, state);
            if (state.getValue(POWER) != newPower) {
                level.setBlock(pos, state.setValue(POWER, newPower), 2);
                this.updateNeighborsInFront(level, pos, state);
            }
        }
    }

    protected void checkTickOnNeighbor(Level p_52577_, BlockPos p_52578_, BlockState p_52579_) {
        if (!this.isLocked(p_52577_, p_52578_, p_52579_)) {
            int flag = p_52579_.getValue(POWER);
            int flag1 = this.getInputSignal(p_52577_, p_52578_, p_52579_);
            if (flag != flag1 && !p_52577_.getBlockTicks().willTickThisTick(p_52578_, this)) {
                TickPriority tickpriority = TickPriority.HIGH;
                if (this.shouldPrioritize(p_52577_, p_52578_, p_52579_)) {
                    tickpriority = TickPriority.EXTREMELY_HIGH;
                } else if (flag > 0) {
                    tickpriority = TickPriority.VERY_HIGH;
                }

                p_52577_.scheduleTick(p_52578_, this, this.getDelay(p_52579_), tickpriority);
            }
        }

    }


}