package dev.siliconcarbidecube.create_redstone_additions.blocks;

import dev.siliconcarbidecube.create_redstone_additions.util.CreateRedstoneAdditionsLightLevels;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class Disjunctor extends DiodeBlock {
    public Disjunctor() {
        super(
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.NONE)
                        .sound(SoundType.COPPER)
                        .strength(0.0F, 0.0F)
                        .lightLevel(CreateRedstoneAdditionsLightLevels::computeLightLevel)
        );
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED);
    }

    public @NotNull BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state.setValue(POWERED, shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state));
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
        if (!(Boolean)blockState.getValue(POWERED)) {
            return 0;
        } else {
            return blockState.getValue(FACING) == dir ? this.getOutputSignal(getter, pos, blockState) : 0;
        }
    }
    protected int getOutputSignal(@NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull BlockState blockState) {
        return 15;
    }

    protected boolean shouldTurnOn(@NotNull Level p_52502_, @NotNull BlockPos p_52503_, @NotNull BlockState p_52504_) {
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
        if (state.getValue(POWERED) && random.nextFloat() > 0.4F) {
            makeParticle(state, level, pos);
        }
    }

    @Override
    protected int getDelay(@NotNull BlockState blockState) {
        return 0;
    }
}