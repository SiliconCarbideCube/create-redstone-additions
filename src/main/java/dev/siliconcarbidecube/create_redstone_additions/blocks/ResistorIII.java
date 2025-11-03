package dev.siliconcarbidecube.create_redstone_additions.blocks;

import dev.siliconcarbidecube.create_redstone_additions.util.CreateRedstoneAdditionsLightLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.ticks.TickPriority;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;


public class ResistorIII extends DiodeBlock {
    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    public ResistorIII() {
        super(
                Properties.of()
                        .mapColor(MapColor.NONE)
                        .sound(SoundType.COPPER)
                        .strength(0.0F, 0.0F)
                        .lightLevel(CreateRedstoneAdditionsLightLevels::computeLightLevel)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { (Property)FACING, (Property)POWER});
    }

    @Override
    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        // set initial power
        state = state.setValue(POWER, this.getInputSignal(level, pos, state));

        // schedule a tick so it recalculates once dust has updated
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, this.getDelay(state), TickPriority.HIGH);
        }

        return state;
    }


    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (direction != null) {
            Direction dirFront = (Direction)state.getValue(FACING);
            Direction dirBack = dirFront.getOpposite();
            return (direction == dirFront || direction == dirBack);
        }
        return true;
    }

    public int getSignal(BlockState blockState, BlockGetter getter, BlockPos pos, Direction dir) {
        if (blockState.getValue(POWER) == 0) {
            return 0;
        } else {
            return blockState.getValue(FACING) == dir || blockState.getValue(FACING) == dir.getOpposite() ? this.getOutputSignal(getter, pos, blockState) : 0;
        }
    }
    protected int getOutputSignal(BlockGetter getter, BlockPos pos, BlockState blockState) {
        return (Integer) blockState.getValue(POWER);
    }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        return state.getValue(POWER) > 0;
    }

    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = (Direction)state.getValue(FACING);
        Direction directionS = direction.getOpposite();
        BlockPos blockpos = pos.relative(direction);
        BlockPos blockposS = pos.relative(directionS);
        int i = level.getSignal(blockpos, direction);
        int j = level.getSignal(blockposS, directionS);
        i = Math.max(i, j);
        return i>4 ? i-5 : 0;
    }

    public void tick(BlockState p_221065_, ServerLevel p_221066_, BlockPos p_221067_, RandomSource p_221068_) {
        if (!this.isLocked(p_221066_, p_221067_, p_221065_)) {
            int flag = p_221065_.getValue(POWER);
            int flag1 = this.getInputSignal(p_221066_, p_221067_, p_221065_);
            if (flag > 0 && flag1 == 0) {
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.setValue(POWER, 0), 2);
            } else{
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.setValue(POWER, flag1), 2);
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

    protected void updateNeighborsInFront(Level p_52581_, BlockPos p_52582_, BlockState p_52583_) {
        Direction directionF = (Direction)p_52583_.getValue(FACING);
        Direction directionB = directionF.getOpposite();
        BlockPos blockposF = p_52582_.relative(directionF.getOpposite());
        BlockPos blockposB = p_52582_.relative(directionB.getOpposite());
        if (!ForgeEventFactory.onNeighborNotify(p_52581_, p_52582_, p_52581_.getBlockState(p_52582_), EnumSet.of(directionF.getOpposite()), false).isCanceled()) {
            p_52581_.neighborChanged(blockposF, this, p_52582_);
            p_52581_.updateNeighborsAtExceptFromFacing(blockposF, this, directionF);
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
                BlockState updated = state.setValue(POWER, newPower);
                level.setBlock(pos, updated, 2);
                this.updateNeighborsInFront(level, pos, updated);
            }
        }
    }

    private static void makeParticle(BlockState state, LevelAccessor level, BlockPos pos) {
        Direction direction = ((Direction)state.getValue((Property)FACING)).getOpposite();
        double x = pos.getX() + 0.5D - 0.1D * direction.getStepX();
        double y = pos.getY() + 0.35D;
        double z = pos.getZ() + 0.5D - 0.1D * direction.getStepZ();
        level.addParticle((ParticleOptions)new DustParticleOptions(DustParticleOptions.REDSTONE_PARTICLE_COLOR, 0.9F), x, y, z, 0.0D, 0.0D, 0.0D);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if ((state.getValue(POWER)>0) && random.nextFloat() > 0.4F) {
            makeParticle(state, (LevelAccessor) level, pos);
        }
    }

    @Override
    protected int getDelay(BlockState blockState) {
        return 0;
    }
}