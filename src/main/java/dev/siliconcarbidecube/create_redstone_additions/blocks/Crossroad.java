package dev.siliconcarbidecube.create_redstone_additions.blocks;

import dev.siliconcarbidecube.create_redstone_additions.init.CreateRedstoneAdditionsModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.util.CreateRedstoneAdditionsLightLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Crossroad extends DiodeBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
    public static final IntegerProperty FLANK_POWER = IntegerProperty.create("flank_power", 0, 15);
    public static final IntegerProperty MODEL_TYPE = IntegerProperty.create("model_type", 0, 3);

    public Crossroad() {
        super(
                Properties.of()
                        .mapColor(MapColor.NONE)
                        .sound(SoundType.COPPER)
                        .strength(0.0F, 0.0F)
                        .lightLevel(CreateRedstoneAdditionsLightLevels::computeLightLevel)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER, FLANK_POWER, MODEL_TYPE);
    }

    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        //return (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state)));
        return state.setValue(POWER, this.getInputSignal(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    public int getSignal(BlockState blockState, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull Direction dir) {
        if (blockState.getValue(POWER) == 0 && blockState.getValue(FLANK_POWER) == 0) {
            return 0;
        } else {
            int power = this.getOutputSignal(getter, pos, blockState);
            int flankPower = this.getFlankOutputSignal(blockState);
            BlockPos gpos = pos.relative(dir.getOpposite());
            BlockState gbs = getter.getBlockState(gpos);
            //return blockState.getValue(FACING) == dir ? ((blockStateS.is(Blocks.REDSTONE_WIRE) ? this.getOutputSignal(getter, pos, blockState) - 1 : this.getOutputSignal(getter, pos, blockState))) : 0;
            if (blockState.getValue(FACING) == dir || blockState.getValue(FACING).getOpposite() == dir) {
                return gbs.is(Blocks.REDSTONE_WIRE) ? (power > 0 ? power - 1 : 0) : power;
            } else {
                if (blockState.getValue(FACING).getCounterClockWise() == dir || blockState.getValue(FACING).getClockWise() == dir) {
                    return gbs.is(Blocks.REDSTONE_WIRE) ? (flankPower > 0 ? flankPower - 1 : 0) : flankPower;
                } else {
                    return 0;
                }
            }
        }
    }

    protected int getOutputSignal(@NotNull BlockGetter getter, @NotNull BlockPos pos, BlockState blockState) {
        return blockState.getValue(POWER);
    }

    protected int getFlankOutputSignal(BlockState blockState) {
        return blockState.getValue(FLANK_POWER);
    }

    protected boolean shouldTurnOn(@NotNull Level level, @NotNull BlockPos pos, BlockState state) {
        return state.getValue(POWER) > 0;
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction directionB = state.getValue(FACING);
        BlockPos blockposB = pos.relative(directionB);
        Direction directionF = directionB.getOpposite();
        BlockPos blockposF = pos.relative(directionF);
        BlockPos MaxFB = level.getSignal(blockposF, directionF) > level.getSignal(blockposB, directionB) ? blockposF : blockposB;
        int i = Math.max(level.getSignal(blockposF, directionF), level.getSignal(blockposB, directionB));
        BlockState blockstate = level.getBlockState(MaxFB);
        if (blockstate.is(CreateRedstoneAdditionsModBlocks.DIODE_BLOCK.get()) || blockstate.is(CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK.get())) {
            i = i != 0 ? i - 1 : 0;
        }
        return blockstate.is(Blocks.REDSTONE_WIRE) ? (blockstate.getValue(RedStoneWireBlock.POWER) == 0 ? 0 : blockstate.getValue(RedStoneWireBlock.POWER) - 1) : i;
    }

    protected int getFlankInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction directionL = state.getValue(FACING).getClockWise();
        BlockPos blockposL = pos.relative(directionL);
        Direction directionR = directionL.getOpposite();
        BlockPos blockposR = pos.relative(directionR);
        BlockPos MaxRL = level.getSignal(blockposR, directionR) > level.getSignal(blockposL, directionL) ? blockposR : blockposL;
        int i = Math.max(level.getSignal(blockposR, directionR), level.getSignal(blockposL, directionL));
        BlockState blockstate = level.getBlockState(MaxRL);
        if (blockstate.is(CreateRedstoneAdditionsModBlocks.DIODE_BLOCK.get()) || blockstate.is(CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK.get())) {
            i = i != 0 ? i - 1 : 0;
        }
        return blockstate.is(Blocks.REDSTONE_WIRE) ? (blockstate.getValue(RedStoneWireBlock.POWER) == 0 ? 0 : blockstate.getValue(RedStoneWireBlock.POWER) - 1) : i;
    }

    public void tick(@NotNull BlockState p_221065_, @NotNull ServerLevel p_221066_, @NotNull BlockPos p_221067_, @NotNull RandomSource p_221068_) {
        if (!this.isLocked(p_221066_, p_221067_, p_221065_)) {
            int flag = p_221065_.getValue(POWER);
            int flag1 = this.getInputSignal(p_221066_, p_221067_, p_221065_);
            int flankFlag = p_221065_.getValue(FLANK_POWER);
            int flankFlag1 = this.getFlankInputSignal(p_221066_, p_221067_, p_221065_);
            if (flankFlag != 0 && flankFlag1 == 0) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(FLANK_POWER, 0), 2);
            } else {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(FLANK_POWER, flankFlag1), 2);
            }
            p_221065_ = p_221066_.getBlockState(p_221067_);
            if (flag != 0 && flag1 == 0) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(POWER, 0), 2);
            } else {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(POWER, flag1), 2);
            }
            p_221065_ = p_221066_.getBlockState(p_221067_);
            flag = p_221065_.getValue(POWER);
            flankFlag = p_221065_.getValue(FLANK_POWER);
            if (flag != 0 && flankFlag != 0) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(MODEL_TYPE, 3), 2);
            } else if (flag != 0) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(MODEL_TYPE, 2), 2);
            } else if (flankFlag != 0) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(MODEL_TYPE, 1), 2);
            } else {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(MODEL_TYPE, 0), 2);
            }
        }
    }

    protected void checkTickOnNeighbor(@NotNull Level p_52577_, @NotNull BlockPos p_52578_, @NotNull BlockState p_52579_) {
        if (!this.isLocked(p_52577_, p_52578_, p_52579_)) {
            int flag = p_52579_.getValue(POWER);
            int flag1 = this.getInputSignal(p_52577_, p_52578_, p_52579_);
            int flankFlag = p_52579_.getValue(FLANK_POWER);
            int flankFlag1 = this.getFlankInputSignal(p_52577_, p_52578_, p_52579_);
            if ((flag != flag1 || flankFlag != flankFlag1) && !p_52577_.getBlockTicks().willTickThisTick(p_52578_, this)) {
                TickPriority tickpriority = TickPriority.HIGH;
                if (this.shouldPrioritize(p_52577_, p_52578_, p_52579_)) {
                    tickpriority = TickPriority.EXTREMELY_HIGH;
                } else if (flag != 0 || flankFlag != 0) {
                    tickpriority = TickPriority.VERY_HIGH;
                }

                p_52577_.scheduleTick(p_52578_, this, this.getDelay(p_52579_), tickpriority);
            }

        }

    }

    protected void updateNeighborsInFront(@NotNull Level p_52581_, BlockPos p_52582_, BlockState p_52583_) {
        Direction directionF = p_52583_.getValue(FACING);
        Direction directionL = directionF.getCounterClockWise();
        Direction directionR = directionF.getClockWise();
        Direction directionB = directionF.getOpposite();
        BlockPos blockposF = p_52582_.relative(directionF.getOpposite());
        BlockPos blockposL = p_52582_.relative(directionL.getOpposite());
        BlockPos blockposR = p_52582_.relative(directionR.getOpposite());
        BlockPos blockposB = p_52582_.relative(directionB.getOpposite());
        if (!ForgeEventFactory.onNeighborNotify(p_52581_, p_52582_, p_52581_.getBlockState(p_52582_), EnumSet.of(directionF.getOpposite()), false).isCanceled()) {
            p_52581_.neighborChanged(blockposF, this, p_52582_);
            p_52581_.updateNeighborsAtExceptFromFacing(blockposF, this, directionF);
            p_52581_.neighborChanged(blockposL, this, p_52582_);
            p_52581_.updateNeighborsAtExceptFromFacing(blockposL, this, directionL);
            p_52581_.neighborChanged(blockposR, this, p_52582_);
            p_52581_.updateNeighborsAtExceptFromFacing(blockposR, this, directionR);
            p_52581_.neighborChanged(blockposB, this, p_52582_);
            p_52581_.updateNeighborsAtExceptFromFacing(blockposB, this, directionB);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide && !this.isLocked(level, pos, state)) {
            int newPower = this.getInputSignal(level, pos, state);
            int oldPower = state.getValue(POWER);
            if (oldPower != newPower) {
                state = state.setValue(POWER, newPower);
                level.setBlock(pos, state, 2);
                this.updateNeighborsInFront(level, pos, state);
            }

            int newFlank = this.getFlankInputSignal(level, pos, state);
            int oldFlank = state.getValue(FLANK_POWER);
            if (oldFlank != newFlank) {
                state = state.setValue(FLANK_POWER, newFlank);
                level.setBlock(pos, state, 2);
                this.updateNeighborsInFront(level, pos, state);
            }

            int modelType = (state.getValue(POWER) > 0 && state.getValue(FLANK_POWER) > 0) ? 3
                    : (state.getValue(POWER) > 0) ? 2
                    : (state.getValue(FLANK_POWER) > 0) ? 1
                    : 0;
            if (state.getValue(MODEL_TYPE) != modelType) {
                state = state.setValue(MODEL_TYPE, modelType);
                level.setBlock(pos, state, 2);
            }
        }
    }


    private static void makeParticle(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction direction = state.getValue(FACING).getOpposite();
        double x = pos.getX() + 0.5D - 0.1D * direction.getStepX();
        double y = pos.getY() + 0.35D;
        double z = pos.getZ() + 0.5D - 0.1D * direction.getStepZ();
        level.addParticle(
                new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 0.9F),
                x, y, z,
                0.0D, 0.0D, 0.0D
        );
    }

    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (((state.getValue(POWER) > 0) || (state.getValue(FLANK_POWER) > 0)) && random.nextFloat() > 0.4F) {
            makeParticle(state, level, pos);
        }
    }

    @Override
    protected int getDelay(@NotNull BlockState blockState) {
        return 0;
    }
}