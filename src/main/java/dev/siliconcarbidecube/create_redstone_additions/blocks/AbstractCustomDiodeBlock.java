package dev.siliconcarbidecube.create_redstone_additions.blocks;

import com.mojang.serialization.MapCodec;
import dev.siliconcarbidecube.create_redstone_additions.util.LightLevels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class AbstractCustomDiodeBlock extends DiodeBlock {
    private final MapCodec<? extends DiodeBlock> CODEC;
    protected static final Properties DEFAULT_PROPERTIES =
            Properties.of()
                    .mapColor(MapColor.NONE)
                    .sound(SoundType.STONE)
                    .strength(0.0F, 0.0F)
                    .lightLevel(LightLevels::computeLightLevel);

    protected AbstractCustomDiodeBlock(Function<Properties, ? extends DiodeBlock> constructor) {
        super(DEFAULT_PROPERTIES);
        this.CODEC = Block.simpleCodec(constructor);
    }

    @Override
    protected @NotNull MapCodec<? extends DiodeBlock> codec() {
        return CODEC;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isActive(state) && random.nextFloat() > 0.4F) {
            makeParticle(state, level, pos);
        }
    }

    protected boolean isActive(BlockState state) {
        if (state.hasProperty(BlockStateProperties.POWER) && state.getValue(BlockStateProperties.POWER) > 0) return true;
        if (state.hasProperty(Conjunctor.POWER_TYPE) && state.getValue(Conjunctor.POWER_TYPE) > 0) return true;
        if (state.hasProperty(Crossroad.MODEL_TYPE) && state.getValue(Crossroad.MODEL_TYPE) > 0) return true;

        if (state.hasProperty(BlockStateProperties.POWERED) && state.getValue(BlockStateProperties.POWERED)) return true;

        return false;
    }

    protected static void makeParticle(BlockState state, LevelAccessor level, BlockPos pos) {
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

    @Override
    protected int getDelay(BlockState blockState) {
        return 0;
    }

}
