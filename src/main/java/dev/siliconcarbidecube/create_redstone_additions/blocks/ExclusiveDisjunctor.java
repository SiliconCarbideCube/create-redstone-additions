package dev.siliconcarbidecube.create_redstone_additions.blocks;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;


    public class ExclusiveDisjunctor extends DiodeBlock {
        public ExclusiveDisjunctor() {
            super(
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.NONE)
                            .sound(SoundType.COPPER)
                            .strength(0.0F, 0.0F));
        }

        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(new Property[] { (Property)FACING, (Property)POWERED });
        }

        public BlockState getStateForPlacement(BlockPlaceContext ctx) {
            BlockState state = super.getStateForPlacement(ctx);
            return (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state)));
        }

        public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            if (direction != null) {
                Direction dirFront = (Direction)state.getValue(FACING);
                Direction dirRight = dirFront.getClockWise();
                Direction dirLeft = dirFront.getCounterClockWise();
                return (direction == dirFront || direction == dirRight || direction == dirLeft);
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
            return this.getInputSignal(p_52502_, p_52503_, p_52504_) > 0;
        }
        protected int getInputSignal(Level p_52544_, BlockPos p_52545_, BlockState p_52546_) {
            Direction direction = (Direction)p_52546_.getValue(FACING);
            Direction directionRight = direction.getCounterClockWise();
            Direction directionLeft = direction.getClockWise();
            BlockPos blockposRight = p_52545_.relative(directionRight);
            BlockPos blockposLeft = p_52545_.relative(directionLeft);
            if ((p_52544_.getSignal(blockposRight, directionRight) >= 1) != (p_52544_.getSignal(blockposLeft, directionLeft) >= 1)) {
                return 15;
            } else {
                return 0;
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
            if (state.getValue(POWERED) && random.nextFloat() > 0.4F) {
                makeParticle(state, (LevelAccessor) level, pos);
            }
        }

        @Override
        protected int getDelay(BlockState blockState) {
            return 0;
        }
    }