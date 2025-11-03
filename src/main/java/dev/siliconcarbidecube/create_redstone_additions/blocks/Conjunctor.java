package dev.siliconcarbidecube.create_redstone_additions.blocks;

import dev.siliconcarbidecube.create_redstone_additions.util.CreateRedstoneAdditionsLightLevels;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.ticks.TickPriority;
import org.jetbrains.annotations.NotNull;

public class Conjunctor extends DiodeBlock {
    public static final IntegerProperty POWER_TYPE = IntegerProperty.create("power_type", 0, 3);

    public Conjunctor() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.NONE)
                .sound(SoundType.COPPER)
                .strength(0.0F, 0.0F)
                .lightLevel(CreateRedstoneAdditionsLightLevels::computeLightLevel)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER_TYPE);
    }

    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWER_TYPE, (shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state)) ? 3 : 0);
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (direction != null) {
            Direction dirFront = state.getValue(FACING);
            Direction dirRight = dirFront.getClockWise();
            Direction dirLeft = dirFront.getCounterClockWise();
            return (direction == dirFront || direction == dirRight || direction == dirLeft);
        }
        return true;
    }

    public int getSignal(BlockState blockState, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull Direction dir) {
        if (blockState.getValue(POWER_TYPE) != 3) {
            return 0;
        } else {
            return blockState.getValue(FACING) == dir ? this.getOutputSignal(getter, pos, blockState) : 0;
        }
    }

    protected int getOutputSignal(@NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        return 15;
    }

    protected boolean shouldTurnOn(@NotNull Level p_52502_, @NotNull BlockPos p_52503_, @NotNull BlockState p_52504_) {
        return this.getInputSignal(p_52502_, p_52503_, p_52504_) == 3;
    }

    protected int getInputSignal(Level p_52544_, BlockPos p_52545_, BlockState p_52546_) {
        Direction direction = p_52546_.getValue(FACING);
        Direction directionRight = direction.getCounterClockWise();
        Direction directionLeft = direction.getClockWise();
        BlockPos blockposRight = p_52545_.relative(directionRight);
        BlockPos blockposLeft = p_52545_.relative(directionLeft);
        if ((p_52544_.getSignal(blockposRight, directionRight) >= 1) && (p_52544_.getSignal(blockposLeft, directionLeft) >= 1)) {
            p_52544_.setBlock(p_52545_, p_52546_.setValue(POWER_TYPE, 3), 2);
            return 3;
        } else {
            if (p_52544_.getSignal(blockposLeft, directionLeft) >= 1) {
                p_52544_.setBlock(p_52545_, p_52546_.setValue(POWER_TYPE, 2), 2);
                return 2;
            } else if (p_52544_.getSignal(blockposRight, directionRight) >= 1) {
                p_52544_.setBlock(p_52545_, p_52546_.setValue(POWER_TYPE, 1), 2);
                return 1;
            }
            return 0;
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


    public void tick(@NotNull BlockState p_221065_, @NotNull ServerLevel p_221066_, @NotNull BlockPos p_221067_, @NotNull RandomSource p_221068_) {
        if (!this.isLocked(p_221066_, p_221067_, p_221065_)) {
            int flag = p_221065_.getValue(POWER_TYPE);
            int flag1 = this.getInputSignal(p_221066_, p_221067_, p_221065_);
            if (flag != flag1) {
                p_221066_.setBlock(p_221067_, p_221065_.setValue(POWER_TYPE, flag1), 2);
            } else {
                p_221066_.scheduleTick(p_221067_, this, this.getDelay(p_221065_), TickPriority.VERY_HIGH);
            }

        }
    }

    protected void checkTickOnNeighbor(@NotNull Level p_52577_, @NotNull BlockPos p_52578_, @NotNull BlockState p_52579_) {
        if (!this.isLocked(p_52577_, p_52578_, p_52579_)) {
            int flag = p_52579_.getValue(POWER_TYPE);
            int flag1 = this.getInputSignal(p_52577_, p_52578_, p_52579_);
            if (flag != flag1 && !p_52577_.getBlockTicks().willTickThisTick(p_52578_, this)) {
                TickPriority tickpriority = TickPriority.HIGH;
                if (this.shouldPrioritize(p_52577_, p_52578_, p_52579_)) {
                    tickpriority = TickPriority.EXTREMELY_HIGH;
                } else if (flag == 3) {
                    tickpriority = TickPriority.VERY_HIGH;
                }

                p_52577_.scheduleTick(p_52578_, this, this.getDelay(p_52579_), tickpriority);
            }
        }

    }

    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(POWER_TYPE) == 3 && random.nextFloat() > 0.4F) {
            makeParticle(state, level, pos);
        }
    }

    @Override
    protected int getDelay(@NotNull BlockState blockState) {
        return 0;
    }
}
