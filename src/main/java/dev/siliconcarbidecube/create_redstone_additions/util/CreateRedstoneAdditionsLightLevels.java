package dev.siliconcarbidecube.create_redstone_additions.util;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Conjunctor;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Crossroad;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Inverter;

public class CreateRedstoneAdditionsLightLevels {
    public static int computeLightLevel(BlockState state) {
        if (state.hasProperty(BlockStateProperties.POWER)) {
            return state.getValue(BlockStateProperties.POWER);
        }

        if (state.hasProperty(Conjunctor.POWER_TYPE)) {
            return state.getValue(Conjunctor.POWER_TYPE);
        }

        if (state.hasProperty(Crossroad.POWER) && state.hasProperty(Crossroad.FLANK_POWER)) {
            return Math.max(state.getValue(Crossroad.POWER), state.getValue(Crossroad.FLANK_POWER));
        }

        if (state.hasProperty(BlockStateProperties.POWERED)) {
            return state.getValue(BlockStateProperties.POWERED) ? 5 : 0;
        }

        if (state.getBlock() instanceof Inverter && state.hasProperty(BlockStateProperties.POWERED)) {
            return 5;
        }

        return 0;
    }
}
