package dev.siliconcarbidecube.create_redstone_additions;

import com.simibubi.create.AllItems;
import dev.siliconcarbidecube.create_redstone_additions.blocks.AbstractResistor;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Conjunctor;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Crossroad;
import dev.siliconcarbidecube.create_redstone_additions.blocks.Diode;
import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.test.TestArea;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class WrenchTest {
    @Test
    void sneakWrenchPicksUpIntoInventory() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = area.player(AllItems.WRENCH.asStack());
            player.setShiftKeyDown(true);
            BlockPos pos = new BlockPos(3, 1, 3);
            for (Supplier<? extends Block> block : DropsTest.BLOCKS) {
                area.set(pos, block.get().defaultBlockState());
                area.useItemOn(player, pos, Direction.UP);
                assertTrue(area.get(pos).isAir(), block.get() + " removed");
                assertEquals(1, player.getInventory().countItem(block.get().asItem()), block.get() + " in inventory");
            }
            assertTrue(area.items(pos, 2).isEmpty());
        }
    }

    @Test
    void sneakWrenchInCreativeRemovesWithoutItem() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = area.player(AllItems.WRENCH.asStack());
            player.setGameMode(GameType.CREATIVE);
            player.setShiftKeyDown(true);
            BlockPos pos = new BlockPos(3, 1, 3);
            area.set(pos, ModBlocks.DIODE.get().defaultBlockState());
            area.useItemOn(player, pos, Direction.UP);
            assertTrue(area.get(pos).isAir());
            assertEquals(0, player.getInventory().countItem(ModBlocks.DIODE.get().asItem()));
            assertTrue(area.items(pos, 2).isEmpty());
        }
    }

    @Test
    void wrenchRotatesEveryBlock() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = area.player(AllItems.WRENCH.asStack());
            BlockPos pos = new BlockPos(3, 1, 3);
            area.redstoneBlock(pos.north());
            for (Supplier<? extends Block> block : DropsTest.BLOCKS) {
                area.set(pos, block.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.NORTH));
                area.useItemOn(player, pos, Direction.UP);
                assertEquals(Direction.EAST, area.get(pos).getValue(DiodeBlock.FACING), block.get() + " rotated");
                area.tick(1);
                assertTrue(area.get(pos).is(block.get()));
            }
        }
    }

    @Test
    void wrenchRotatesDiodeAndUpdatesRedstone() {
        try (TestArea area = TestArea.create()) {
            BlockPos pos = new BlockPos(5, 1, 5);
            BlockPos oldLamp = pos.east(2);
            BlockPos newLamp = pos.south();
            area.set(pos, ModBlocks.DIODE.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.WEST));
            area.set(pos.east(), Blocks.STONE.defaultBlockState());
            area.set(oldLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(newLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.redstoneBlock(pos.west());
            assertEquals(15, area.get(pos).getValue(Diode.POWER));
            assertTrue(area.get(oldLamp).getValue(RedstoneLampBlock.LIT));

            area.useItemOn(area.player(AllItems.WRENCH.asStack()), pos, Direction.UP);
            assertEquals(Direction.NORTH, area.get(pos).getValue(DiodeBlock.FACING));
            assertEquals(0, area.get(pos).getValue(Diode.POWER));
            area.tick(5);
            assertFalse(area.get(newLamp).getValue(RedstoneLampBlock.LIT));
            assertFalse(area.get(oldLamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void wrenchRotatesInverterAndUpdatesRedstone() {
        try (TestArea area = TestArea.create()) {
            BlockPos pos = new BlockPos(5, 1, 5);
            BlockPos newLamp = pos.south();
            area.redstoneBlock(pos.west());
            area.set(pos, ModBlocks.INVERTER.get().defaultBlockState()
                    .setValue(DiodeBlock.FACING, Direction.WEST).setValue(DiodeBlock.POWERED, false));
            area.set(newLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.tick(2);
            assertFalse(area.get(pos).getValue(DiodeBlock.POWERED));

            area.useItemOn(area.player(AllItems.WRENCH.asStack()), pos, Direction.UP);
            assertEquals(Direction.NORTH, area.get(pos).getValue(DiodeBlock.FACING));
            area.tick(2);
            assertTrue(area.get(pos).getValue(DiodeBlock.POWERED));
            assertTrue(area.get(newLamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void wrenchRotatesConjunctorAndSwapsItsSides() {
        try (TestArea area = TestArea.create()) {
            BlockPos pos = new BlockPos(5, 1, 5);
            BlockPos northLamp = pos.north();
            BlockPos southLamp = pos.south();
            area.set(northLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(southLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(pos, ModBlocks.CONJUNCTOR.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.SOUTH));
            area.redstoneBlock(pos.east());
            assertEquals(1, area.get(pos).getValue(Conjunctor.POWER_TYPE));

            FakePlayer player = area.player(AllItems.WRENCH.asStack());
            area.useItemOn(player, pos, Direction.UP);
            assertEquals(Direction.WEST, area.get(pos).getValue(DiodeBlock.FACING));
            assertEquals(0, area.get(pos).getValue(Conjunctor.POWER_TYPE), "sides are now north and south");

            area.useItemOn(player, pos, Direction.UP);
            assertEquals(Direction.NORTH, area.get(pos).getValue(DiodeBlock.FACING));
            assertEquals(2, area.get(pos).getValue(Conjunctor.POWER_TYPE), "east is now the left side");
            assertFalse(area.get(southLamp).getValue(RedstoneLampBlock.LIT));

            area.redstoneBlock(pos.west());
            assertEquals(3, area.get(pos).getValue(Conjunctor.POWER_TYPE));
            assertTrue(area.get(southLamp).getValue(RedstoneLampBlock.LIT));
            assertFalse(area.get(northLamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void wrenchRotatesCrossroadAndSwapsItsAxes() {
        try (TestArea area = TestArea.create()) {
            BlockPos pos = new BlockPos(5, 1, 5);
            BlockPos westLamp = pos.west();
            area.set(westLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(pos, ModBlocks.CROSSROAD.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.SOUTH));
            area.redstoneBlock(pos.east());
            assertEquals(15, area.get(pos).getValue(Crossroad.FLANK_POWER));
            assertTrue(area.get(westLamp).getValue(RedstoneLampBlock.LIT));

            area.useItemOn(area.player(AllItems.WRENCH.asStack()), pos, Direction.UP);
            assertEquals(Direction.WEST, area.get(pos).getValue(DiodeBlock.FACING));
            assertEquals(15, area.get(pos).getValue(Crossroad.POWER), "east is now on the axis");
            assertEquals(0, area.get(pos).getValue(Crossroad.FLANK_POWER));
            assertEquals(2, area.get(pos).getValue(Crossroad.MODEL_TYPE));
            assertTrue(area.get(westLamp).getValue(RedstoneLampBlock.LIT));

            area.clear(pos.east());
            area.tick(5);
            assertEquals(0, area.get(pos).getValue(Crossroad.POWER));
            assertFalse(area.get(westLamp).getValue(RedstoneLampBlock.LIT));
        }
    }

    @Test
    void wrenchRotatesResistorAndUpdatesRedstone() {
        try (TestArea area = TestArea.create()) {
            BlockPos pos = new BlockPos(5, 1, 5);
            BlockPos oldLamp = pos.east();
            BlockPos newLamp = pos.south();
            area.set(oldLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(newLamp, Blocks.REDSTONE_LAMP.defaultBlockState());
            area.set(pos, ModBlocks.IRON_RESISTOR.get().defaultBlockState().setValue(DiodeBlock.FACING, Direction.WEST));
            area.redstoneBlock(pos.north());
            area.redstoneBlock(pos.west());
            assertEquals(13, area.get(pos).getValue(AbstractResistor.POWER));
            assertTrue(area.get(oldLamp).getValue(RedstoneLampBlock.LIT));
            assertFalse(area.get(newLamp).getValue(RedstoneLampBlock.LIT));

            area.useItemOn(area.player(AllItems.WRENCH.asStack()), pos, Direction.UP);
            assertEquals(Direction.NORTH, area.get(pos).getValue(DiodeBlock.FACING));
            assertEquals(13, area.get(pos).getValue(AbstractResistor.POWER));
            assertTrue(area.get(newLamp).getValue(RedstoneLampBlock.LIT));
            area.tick(5);
            assertFalse(area.get(oldLamp).getValue(RedstoneLampBlock.LIT));
        }
    }
}
