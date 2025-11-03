package dev.siliconcarbidecube.create_redstone_additions.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.TickPriority;


public class Metronome extends DiodeBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private int time = 0;
    public static final IntegerProperty FREQUENCY = IntegerProperty.create("frequency", 0, 2);

    public VoxelShape getShape(BlockState p_52556_, BlockGetter p_52557_, BlockPos p_52558_, CollisionContext p_52559_) {
        return SHAPE;
    }
    
    public Metronome() {
        super(
                Properties.of()
                        .mapColor(MapColor.NONE)
                        .sound(SoundType.COPPER)
                        .strength(0.0F, 0.0F));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[] { (Property)FACING, (Property)POWERED, (Property)FREQUENCY});
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return (BlockState)state.setValue((Property)POWERED, Boolean.valueOf(shouldTurnOn(ctx.getLevel(), ctx.getClickedPos(), state)));
        //return (BlockState)state.setValue((Property)POWERED, this.getInputSignal(ctx.getLevel(), ctx.getClickedPos(), state));
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (direction != null) {
            Direction dirFront = (Direction)state.getValue(FACING);
            Direction dirRight = dirFront.getCounterClockWise();
            return (direction == dirRight.getOpposite() || direction == dirRight);
        }
        return true;
    }

    public int getSignal(BlockState blockState, BlockGetter getter, BlockPos pos, Direction dir) {
        if (!blockState.getValue(POWERED)) {
            return blockState.getValue(FACING) == dir.getCounterClockWise() ? getOutputSignal(getter, pos, blockState) : 0;
        } else {
            return blockState.getValue(FACING) == dir.getClockWise() ? getOutputSignal(getter, pos, blockState) : 0;
        }
    }
    protected int getOutputSignal(BlockGetter getter, BlockPos pos, BlockState blockState) {
        return 15;
    }

    protected boolean shouldTurnOn(Level level, BlockPos pos, BlockState state) {
        return state.getValue(POWERED);
    }
    
    protected int getInputSignal(Level level, BlockPos pos, BlockState state) {
        Direction direction = (Direction)state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction);
        int i = level.getSignal(blockpos, direction);
        BlockState blockstate = level.getBlockState(blockpos);
        return (blockstate.is(Blocks.REDSTONE_WIRE)) ? ((Integer)blockstate.getValue(RedStoneWireBlock.POWER) == 0 ? 0 : blockstate.getValue(RedStoneWireBlock.POWER) - 1) : i;
    }

    public void tick(BlockState p_221065_, ServerLevel p_221066_, BlockPos p_221067_, RandomSource p_221068_) {
        if (!this.isLocked(p_221066_, p_221067_, p_221065_)) {
            boolean flag = (Boolean)p_221065_.getValue(POWERED);
            this.time++;
            if (p_221065_.getValue(FREQUENCY) == 0 && time == 4){
                this.time = 0;
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.cycle(POWERED), 2);
            } else if (p_221065_.getValue(FREQUENCY) == 1 && time == 10){
                this.time = 0;
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.cycle(POWERED), 2);
            } else if (p_221065_.getValue(FREQUENCY) == 2 && time == 20){
                this.time = 0;
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.cycle(POWERED), 2);
            }
            boolean flag1 = this.shouldTurnOn(p_221066_, p_221067_, p_221065_);
            if (flag && !flag1) {
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.setValue(POWERED, false), 2);
            } else if (!flag) {
                p_221066_.setBlock(p_221067_, (BlockState)p_221065_.setValue(POWERED, true), 2);
                if (!flag1) {
                    p_221066_.scheduleTick(p_221067_, this, this.getDelay(p_221065_), TickPriority.VERY_HIGH);
                }
            }
        }

    }

    protected void checkTickOnNeighbor(Level p_52577_, BlockPos p_52578_, BlockState p_52579_) {
        if (!this.isLocked(p_52577_, p_52578_, p_52579_)) {
            boolean flag = (Boolean)p_52579_.getValue(POWERED);
            boolean flag1 = this.shouldTurnOn(p_52577_, p_52578_, p_52579_);
            if (flag != flag1 && !p_52577_.getBlockTicks().willTickThisTick(p_52578_, this)) {
                TickPriority tickpriority = TickPriority.HIGH;
                if (this.shouldPrioritize(p_52577_, p_52578_, p_52579_)) {
                    tickpriority = TickPriority.EXTREMELY_HIGH;
                } else if (flag) {
                    tickpriority = TickPriority.VERY_HIGH;
                }

                p_52577_.scheduleTick(p_52578_, this, this.getDelay(p_52579_), tickpriority);
            }
        }

    }

    @Override

    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult result){
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            level.setBlock(pos, state.cycle(FREQUENCY), 3);
        }

        return  super.use(state, level, pos, player, hand, result);
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

    protected int getDelay(BlockState blockState) {
        return 0;
    }
}