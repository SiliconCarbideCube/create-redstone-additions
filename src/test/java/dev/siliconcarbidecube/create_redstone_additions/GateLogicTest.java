package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.blocks.AbstractResistor;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Conjunctor;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Crossroad;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Diode;
import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.test.TestArea;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Gates are placed by a player looking north, so FACING is SOUTH: input (or axis back) at south, output at north,
 * and the side inputs are east and west.
 */
class GateLogicTest {
    private static final BlockPos POS = new BlockPos(5, 1, 5);

    private static BlockState placeGate(TestArea area, Block block, BlockPos pos) {
        FakePlayer player = area.player(new ItemStack(block, 2), Direction.NORTH);
        assertTrue(area.useItemOn(player, pos.below(), Direction.UP).consumesAction(), block + " placed");
        assertEquals(1, player.getMainHandItem().getCount(), block + " item used");
        BlockState state = area.get(pos);
        assertTrue(state.is(block));
        assertEquals(Direction.SOUTH, state.getValue(DiodeBlock.FACING));
        return state;
    }

    private static void assertOutputOnlyNorth(TestArea area, BlockPos pos, int expected) {
        assertEquals(expected, area.signalFrom(pos, Direction.NORTH), "output north");
        for (Direction side : List.of(Direction.SOUTH, Direction.EAST, Direction.WEST)) {
            assertEquals(0, area.signalFrom(pos, side), "output " + side);
        }
    }

    @Test
    void diodePassesOneWayOnly() {
        try (TestArea area = TestArea.create()) {
            placeGate(area, ModBlocks.DIODE.get(), POS);
            area.redstoneBlock(POS.south());
            assertEquals(15, area.get(POS).getValue(Diode.POWER));
            assertOutputOnlyNorth(area, POS, 15);

            area.clear(POS.south());
            assertEquals(0, area.get(POS).getValue(Diode.POWER));
            assertOutputOnlyNorth(area, POS, 0);

            area.redstoneBlock(POS.north());
            assertEquals(0, area.get(POS).getValue(Diode.POWER));
            assertEquals(0, area.signalFrom(POS, Direction.SOUTH));
        }
    }

    @Test
    void diodeOnWireLineCostsOneLikeAWire() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.south(2));
            area.wire(POS.south());
            area.wire(POS.north());
            placeGate(area, ModBlocks.DIODE.get(), POS);
            assertEquals(14, area.get(POS).getValue(Diode.POWER));
            assertEquals(13, area.wirePower(POS.north()));

            area.clear(POS.south(2));
            assertEquals(0, area.get(POS).getValue(Diode.POWER));
            assertEquals(0, area.wirePower(POS.north()));
        }
    }

    @Test
    void conjunctorIsAnd() {
        try (TestArea area = TestArea.create()) {
            BlockPos right = POS.east();
            BlockPos left = POS.west();
            placeGate(area, ModBlocks.CONJUNCTOR.get(), POS);
            assertEquals(0, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 0);

            area.redstoneBlock(right);
            assertEquals(1, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 0);

            area.redstoneBlock(left);
            assertEquals(3, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 15);

            area.clear(right);
            assertEquals(2, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 0);

            area.clear(left);
            assertEquals(0, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 0);

            area.redstoneBlock(POS.south());
            assertEquals(0, area.get(POS).getValue(Conjunctor.POWER_TYPE), "back is not an input");
        }
    }

    @Test
    void conjunctorDrivesWireAndLampInFront() {
        try (TestArea area = TestArea.create()) {
            BlockPos lamp = POS.north(2);
            area.set(lamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            placeGate(area, ModBlocks.CONJUNCTOR.get(), POS);
            area.wire(POS.north());

            area.redstoneBlock(POS.east());
            assertEquals(0, area.wirePower(POS.north()));
            assertFalse(area.get(lamp).getValue(RedstoneLampBlock.LIT));

            area.redstoneBlock(POS.west());
            assertEquals(15, area.wirePower(POS.north()));
            assertTrue(area.get(lamp).getValue(RedstoneLampBlock.LIT));

            area.clear(POS.east());
            area.tick(5);
            assertEquals(0, area.wirePower(POS.north()));
            assertFalse(area.get(lamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void conjunctorPlacedBetweenPoweredSidesUsesTheItem() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.east());
            area.redstoneBlock(POS.west());
            assertEquals(3, placeGate(area, ModBlocks.CONJUNCTOR.get(), POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 15);
        }
    }

    @Test
    void conjunctorPlacedNextToOnePoweredSideShowsIt() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.east());
            assertEquals(1, placeGate(area, ModBlocks.CONJUNCTOR.get(), POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 0);
        }
    }

    @Test
    void conjunctorPlacedBesideWireLinesPicksThemUp() {
        try (TestArea area = TestArea.create()) {
            for (BlockPos side : List.of(POS.east(), POS.west(), POS.east(4))) {
                area.redstoneBlock(side.north(2));
                area.wire(side.north());
                area.wire(side);
                area.wire(side.south());
            }
            placeGate(area, ModBlocks.CONJUNCTOR.get(), POS);
            placeGate(area, ModBlocks.CONJUNCTOR.get(), POS.east(3));
            area.tick(1);
            assertEquals(3, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            assertOutputOnlyNorth(area, POS, 15);
            assertEquals(1, area.get(POS.east(3)).getValue(Conjunctor.POWER_TYPE));
        }
    }

    @Test
    void conjunctorDoesNotKeepRescheduling() {
        try (TestArea area = TestArea.create()) {
            area.set(POS, ModBlocks.CONJUNCTOR.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.SOUTH));
            area.redstoneBlock(POS.east());
            area.tick(3);
            assertEquals(1, area.get(POS).getValue(Conjunctor.POWER_TYPE));
            boolean scheduled = area.call(level -> level.getBlockTicks().hasScheduledTick(area.abs(POS), ModBlocks.CONJUNCTOR.get()));
            assertFalse(scheduled, "tick still scheduled");
        }
    }

    @Test
    void disjunctorIsOr() {
        try (TestArea area = TestArea.create()) {
            placeGate(area, ModBlocks.DISJUNCTOR.get(), POS);
            assertFalse(area.get(POS).getValue(DiodeBlock.POWERED));

            area.redstoneBlock(POS.east());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 15);

            area.clear(POS.east());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 0);

            area.redstoneBlock(POS.west());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 15);

            area.redstoneBlock(POS.east());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 15);

            area.clear(POS.east());
            area.clear(POS.west());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 0);

            area.redstoneBlock(POS.south());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 0);
        }
    }

    @Test
    void disjunctorDrivesWireAndLampInFront() {
        try (TestArea area = TestArea.create()) {
            BlockPos lamp = POS.north(2);
            area.set(lamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            placeGate(area, ModBlocks.DISJUNCTOR.get(), POS);
            area.wire(POS.north());
            assertEquals(0, area.wirePower(POS.north()));
            assertFalse(area.get(lamp).getValue(RedstoneLampBlock.LIT));

            area.redstoneBlock(POS.east());
            area.tick(2);
            assertEquals(15, area.wirePower(POS.north()));
            assertTrue(area.get(lamp).getValue(RedstoneLampBlock.LIT));

            area.redstoneBlock(POS.west());
            area.clear(POS.east());
            area.tick(2);
            assertEquals(15, area.wirePower(POS.north()));
            assertTrue(area.get(lamp).getValue(RedstoneLampBlock.LIT));

            area.clear(POS.west());
            area.tick(5);
            assertEquals(0, area.wirePower(POS.north()));
            assertFalse(area.get(lamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void disjunctorPlacedNextToPoweredSideIsOn() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.west());
            assertTrue(placeGate(area, ModBlocks.DISJUNCTOR.get(), POS).getValue(DiodeBlock.POWERED));
            assertOutputOnlyNorth(area, POS, 15);
        }
    }

    @Test
    void disjunctorDefaultStateIsOff() {
        try (TestArea area = TestArea.create()) {
            area.set(POS, ModBlocks.DISJUNCTOR.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.SOUTH));
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 0);
        }
    }

    @Test
    void inverterIsNot() {
        try (TestArea area = TestArea.create()) {
            assertTrue(placeGate(area, ModBlocks.INVERTER.get(), POS).getValue(DiodeBlock.POWERED), "on with no input");
            assertOutputOnlyNorth(area, POS, 15);

            area.redstoneBlock(POS.south());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 0);

            area.clear(POS.south());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 15);

            area.redstoneBlock(POS.east());
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 15);
        }
    }

    @Test
    void inverterPlacedNextToPoweredInputIsOff() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.south());
            assertFalse(placeGate(area, ModBlocks.INVERTER.get(), POS).getValue(DiodeBlock.POWERED));
            area.tick(2);
            assertOutputOnlyNorth(area, POS, 0);
        }
    }

    @Test
    void crossroadPassesBothAxesIndependently() {
        try (TestArea area = TestArea.create()) {
            area.wire(POS.north());
            area.wire(POS.west());
            placeGate(area, ModBlocks.CROSSROAD.get(), POS);

            area.redstoneBlock(POS.south());
            BlockState state = area.get(POS);
            assertEquals(15, state.getValue(Crossroad.POWER));
            assertEquals(0, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(2, state.getValue(Crossroad.MODEL_TYPE));
            assertEquals(14, area.wirePower(POS.north()));
            assertEquals(0, area.wirePower(POS.west()));

            area.redstoneBlock(POS.east());
            state = area.get(POS);
            assertEquals(15, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(3, state.getValue(Crossroad.MODEL_TYPE));
            assertEquals(14, area.wirePower(POS.west()));

            area.clear(POS.south());
            state = area.get(POS);
            assertEquals(0, state.getValue(Crossroad.POWER));
            assertEquals(15, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(1, state.getValue(Crossroad.MODEL_TYPE));
            assertEquals(0, area.wirePower(POS.north()));
            assertEquals(14, area.wirePower(POS.west()));

            area.clear(POS.east());
            state = area.get(POS);
            assertEquals(0, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(0, state.getValue(Crossroad.MODEL_TYPE));
            assertEquals(0, area.wirePower(POS.west()));
        }
    }

    @Test
    void crossroadPlacedNextToPoweredSourcesIsOn() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.east());
            area.wire(POS.west());
            BlockState state = placeGate(area, ModBlocks.CROSSROAD.get(), POS);
            assertEquals(0, state.getValue(Crossroad.POWER));
            assertEquals(15, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(1, state.getValue(Crossroad.MODEL_TYPE));
            assertEquals(14, area.wirePower(POS.west()));

            BlockPos both = POS.east(4);
            area.redstoneBlock(both.south());
            area.redstoneBlock(both.east());
            state = placeGate(area, ModBlocks.CROSSROAD.get(), both);
            assertEquals(15, state.getValue(Crossroad.POWER));
            assertEquals(15, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(3, state.getValue(Crossroad.MODEL_TYPE));
        }
    }

    @Test
    void crossroadPlacedBesideWireLinesPicksThemUp() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.north().west(2));
            area.wire(POS.north().west());
            area.wire(POS.north());
            area.redstoneBlock(POS.east().south(2));
            area.wire(POS.east().south());
            area.wire(POS.east());
            placeGate(area, ModBlocks.CROSSROAD.get(), POS);
            area.tick(1);
            BlockState state = area.get(POS);
            assertEquals(14, state.getValue(Crossroad.POWER));
            assertEquals(14, state.getValue(Crossroad.FLANK_POWER));
            assertEquals(3, state.getValue(Crossroad.MODEL_TYPE));
        }
    }

    @Test
    void crossroadDoesNotLatchThroughBlockItPowers() {
        try (TestArea area = TestArea.create()) {
            area.set(POS, ModBlocks.CROSSROAD.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.SOUTH));
            area.set(POS.north(), Blocks.STONE.defaultBlockState());
            area.set(POS.north(2), Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(POS.west(), Blocks.STONE.defaultBlockState());
            area.set(POS.west(2), Blocks.REDSTONE_LAMP.defaultBlockState());

            area.redstoneBlock(POS.south());
            area.redstoneBlock(POS.east());
            assertEquals(15, area.get(POS).getValue(Crossroad.POWER));
            assertEquals(15, area.get(POS).getValue(Crossroad.FLANK_POWER));
            assertTrue(area.get(POS.north(2)).getValue(RedstoneLampBlock.LIT), "axis output strongly powers the block");
            assertTrue(area.get(POS.west(2)).getValue(RedstoneLampBlock.LIT), "flank output strongly powers the block");

            area.clear(POS.south());
            area.clear(POS.east());
            area.tick(5);
            assertEquals(0, area.get(POS).getValue(Crossroad.POWER));
            assertEquals(0, area.get(POS).getValue(Crossroad.FLANK_POWER));
            assertEquals(0, area.power(POS.north(2)));
            assertEquals(0, area.power(POS.west(2)));

            area.lever(POS.north().above(), true);
            assertEquals(15, area.get(POS).getValue(Crossroad.POWER), "still reads blocks powered by other sources");
        }
    }

    @Test
    void crossroadPowersAnotherCrossroadThroughABlock() {
        try (TestArea area = TestArea.create()) {
            BlockPos other = POS.north(2);
            BlockPos lamp = POS.north(3);
            BlockState crossroad = ModBlocks.CROSSROAD.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.SOUTH);
            area.set(POS, crossroad);
            area.set(POS.north(), Blocks.STONE.defaultBlockState());
            area.set(other, crossroad);
            area.set(lamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.lever(POS.south(), false);

            area.setLever(POS.south(), true);
            area.tick(1);
            assertEquals(15, area.get(POS).getValue(Crossroad.POWER));
            assertEquals(14, area.get(other).getValue(Crossroad.POWER), "a block costs one, like a wire");
            assertTrue(area.get(lamp).getValue(RedstoneLampBlock.LIT));

            area.setLever(POS.south(), false);
            area.tick(5);
            assertEquals(0, area.get(POS).getValue(Crossroad.POWER));
            assertEquals(0, area.get(other).getValue(Crossroad.POWER));
            assertFalse(area.get(lamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void crossroadGlowsForEitherAxis() {
        BlockState crossroad = ModBlocks.CROSSROAD.get().defaultBlockState();
        assertEquals(0, crossroad.getLightEmission());
        assertEquals(15, crossroad.setValue(Crossroad.POWER, 15).getLightEmission());
        assertEquals(14, crossroad.setValue(Crossroad.FLANK_POWER, 14).getLightEmission());
        assertEquals(15, crossroad.setValue(Crossroad.POWER, 15).setValue(Crossroad.FLANK_POWER, 14).getLightEmission());
        assertEquals(5, ModBlocks.INVERTER.get().defaultBlockState().setValue(DiodeBlock.POWERED, true).getLightEmission());
    }

    @Test
    void resistorsSubtractTheirAttenuation() {
        Map<Block, Integer> attenuation = Map.of(ModBlocks.GOLDEN_RESISTOR.get(), 1, ModBlocks.IRON_RESISTOR.get(), 2,
                ModBlocks.CERAMIC_RESISTOR.get(), 5, ModBlocks.GLASS_RESISTOR.get(), 10);
        try (TestArea area = TestArea.create()) {
            // wire at x has power 16 - x; resistors sit south of it
            area.redstoneBlock(new BlockPos(0, 1, 1));
            for (int x = 1; x < TestArea.SIZE; x++) area.wire(new BlockPos(x, 1, 1));
            assertEquals(1, area.wirePower(new BlockPos(15, 1, 1)));

            attenuation.forEach((block, a) -> {
                FakePlayer player = area.player(new ItemStack(block, 3), Direction.NORTH);
                for (int input : List.of(15, a + 1, a)) {
                    BlockPos pos = new BlockPos(16 - input, 1, 2);
                    area.useItemOn(player, pos.below(), Direction.UP);
                    area.tick(1);
                    assertTrue(area.get(pos).is(block));
                    int expected = Math.max(input - a, 0);
                    assertEquals(expected, area.get(pos).getValue(AbstractResistor.POWER), block + " input " + input);
                    assertEquals(expected, area.signalFrom(pos, Direction.SOUTH), block + " output " + input);
                }
                area.clear(new BlockPos(1, 1, 2));
                area.clear(new BlockPos(16 - a, 1, 2));
                area.clear(new BlockPos(15 - a, 1, 2));
            });
            area.tick(1);
            assertEquals(1, area.wirePower(new BlockPos(15, 1, 1)), "resistors do not boost the line");
        }
    }

    @Test
    void resistorTurnsOffWhenSourceIsRemoved() {
        try (TestArea area = TestArea.create()) {
            area.redstoneBlock(POS.south(2));
            area.wire(POS.south());
            area.wire(POS.north());
            placeGate(area, ModBlocks.IRON_RESISTOR.get(), POS);
            assertEquals(13, area.get(POS).getValue(AbstractResistor.POWER));
            assertEquals(13, area.wirePower(POS.north()));

            area.clear(POS.south(2));
            assertEquals(0, area.get(POS).getValue(AbstractResistor.POWER));
            assertEquals(0, area.wirePower(POS.north()));
            assertEquals(0, area.wirePower(POS.south()));
        }
    }
}
