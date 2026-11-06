package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import dev.siliconcarbidecube.create_redstone_additions.test.TestArea;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VerdigrisTest {
    private static final String CANCEL_TAG = "crt_cancel_click";
    private static final BlockPos POS = new BlockPos(3, 1, 3);

    static {
        NeoForge.EVENT_BUS.addListener(PlayerInteractEvent.RightClickBlock.class, event -> {
            if (event.getEntity().getTags().contains(CANCEL_TAG)) event.setCanceled(true);
        });
    }

    /** Right click as the client sends it: main hand first, off hand only if the main hand did nothing. */
    private static int click(TestArea area, FakePlayer player, BlockPos pos) {
        area.clearItems();
        InteractionResult result = area.useItemOn(player, pos, Direction.UP, InteractionHand.MAIN_HAND);
        if (!result.consumesAction()) area.useItemOn(player, pos, Direction.UP, InteractionHand.OFF_HAND);
        int count = area.countItems(ModItems.VERDIGRIS.get(), pos, 2);
        area.clearItems();
        return count;
    }

    private static void assertOneDrop(int count, String what) {
        assertTrue(count >= 3 && count <= 6, what + ": expected 3..6 verdigris, got " + count);
    }

    private static void assertScrapeChain(TestArea area, FakePlayer player, List<Block> chain) {
        area.set(POS, chain.get(0).defaultBlockState());
        for (int i = 1; i < chain.size(); i++) {
            assertOneDrop(click(area, player, POS), chain.get(i - 1) + " scrape");
            assertTrue(area.get(POS).is(chain.get(i)), chain.get(i - 1) + " -> " + chain.get(i) + ", got " + area.get(POS));
        }
        assertEquals(0, click(area, player, POS), chain.get(chain.size() - 1) + " is not scrapable");
        assertTrue(area.get(POS).is(chain.get(chain.size() - 1)));
    }

    private static void setDoor(TestArea area, Block door) {
        area.set(POS, door.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        area.set(POS.above(), door.defaultBlockState().setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
    }

    private static FakePlayer axePlayer(TestArea area) {
        return area.player(new ItemStack(Items.IRON_AXE));
    }

    @Test
    void scrapeDropsOncePerStage() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = axePlayer(area);
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_COPPER, Blocks.WEATHERED_COPPER, Blocks.EXPOSED_COPPER, Blocks.COPPER_BLOCK));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_CUT_COPPER, Blocks.WEATHERED_CUT_COPPER, Blocks.EXPOSED_CUT_COPPER, Blocks.CUT_COPPER));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_CUT_COPPER_STAIRS, Blocks.WEATHERED_CUT_COPPER_STAIRS, Blocks.EXPOSED_CUT_COPPER_STAIRS, Blocks.CUT_COPPER_STAIRS));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.CUT_COPPER_SLAB));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_CHISELED_COPPER, Blocks.WEATHERED_CHISELED_COPPER, Blocks.EXPOSED_CHISELED_COPPER, Blocks.CHISELED_COPPER));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_COPPER_GRATE, Blocks.WEATHERED_COPPER_GRATE, Blocks.EXPOSED_COPPER_GRATE, Blocks.COPPER_GRATE));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_COPPER_BULB, Blocks.WEATHERED_COPPER_BULB, Blocks.EXPOSED_COPPER_BULB, Blocks.COPPER_BULB));
            player.setShiftKeyDown(true);
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_COPPER_TRAPDOOR, Blocks.WEATHERED_COPPER_TRAPDOOR, Blocks.EXPOSED_COPPER_TRAPDOOR, Blocks.COPPER_TRAPDOOR));
            assertScrapeChain(area, player, List.of(Blocks.OXIDIZED_COPPER, Blocks.WEATHERED_COPPER, Blocks.EXPOSED_COPPER, Blocks.COPPER_BLOCK));
        }
    }

    @Test
    void sneakScrapingDoorDropsOncePerClick() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = axePlayer(area);
            player.setShiftKeyDown(true);
            setDoor(area, Blocks.OXIDIZED_COPPER_DOOR);
            assertOneDrop(click(area, player, POS), "door scrape");
            assertTrue(area.get(POS).is(Blocks.WEATHERED_COPPER_DOOR));
            assertTrue(area.get(POS.above()).is(Blocks.WEATHERED_COPPER_DOOR));
            assertFalse(area.get(POS).getValue(DoorBlock.OPEN));
        }
    }

    @Test
    void openingCopperDoorOrTrapdoorDropsNothing() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = axePlayer(area);
            setDoor(area, Blocks.OXIDIZED_COPPER_DOOR);
            for (int i = 0; i < 3; i++) {
                assertEquals(0, click(area, player, POS), "door click " + i);
                assertEquals(i % 2 == 0, area.get(POS).getValue(DoorBlock.OPEN));
                assertTrue(area.get(POS).is(Blocks.OXIDIZED_COPPER_DOOR));
            }
            area.clear(POS.above());
            area.set(POS, Blocks.OXIDIZED_COPPER_TRAPDOOR.defaultBlockState());
            assertEquals(0, click(area, player, POS), "trapdoor click");
            assertTrue(area.get(POS).getValue(TrapDoorBlock.OPEN));
            assertTrue(area.get(POS).is(Blocks.OXIDIZED_COPPER_TRAPDOOR));
        }
    }

    @Test
    void waxedAndPlainCopperDropNothing() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = axePlayer(area);
            area.set(POS, Blocks.WAXED_OXIDIZED_COPPER.defaultBlockState());
            assertEquals(0, click(area, player, POS), "wax off");
            assertTrue(area.get(POS).is(Blocks.OXIDIZED_COPPER));
            assertOneDrop(click(area, player, POS), "scrape after wax off");
            assertTrue(area.get(POS).is(Blocks.WEATHERED_COPPER));
            area.set(POS, Blocks.COPPER_BLOCK.defaultBlockState());
            assertEquals(0, click(area, player, POS));
            assertTrue(area.get(POS).is(Blocks.COPPER_BLOCK));
        }
    }

    @Test
    void noDropWhenScrapeDoesNotHappen() {
        try (TestArea area = TestArea.create()) {
            FakePlayer shield = axePlayer(area);
            shield.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.SHIELD));
            FakePlayer adventure = axePlayer(area);
            adventure.setGameMode(GameType.ADVENTURE);
            FakePlayer spectator = axePlayer(area);
            spectator.setGameMode(GameType.SPECTATOR);
            FakePlayer cooldown = axePlayer(area);
            cooldown.getCooldowns().addCooldown(Items.IRON_AXE, 100);
            FakePlayer cancelled = axePlayer(area);
            cancelled.addTag(CANCEL_TAG);
            FakePlayer pickaxe = area.player(new ItemStack(Items.IRON_PICKAXE));

            BlockState oxidized = Blocks.OXIDIZED_COPPER.defaultBlockState();
            area.set(POS, oxidized);
            assertAll(Stream.of(shield, adventure, spectator, cooldown, cancelled, pickaxe).map(player -> () -> {
                String what = player == shield ? "shield" : player == adventure ? "adventure" : player == spectator ? "spectator"
                        : player == cooldown ? "cooldown" : player == cancelled ? "cancelled" : "pickaxe";
                assertEquals(0, click(area, player, POS), what + " drop");
                assertEquals(oxidized, area.get(POS), what + " state");
            }));
        }
    }

    @Test
    void axeInEitherHandDropsOncePerClick() {
        try (TestArea area = TestArea.create()) {
            FakePlayer offhand = area.player(ItemStack.EMPTY);
            offhand.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.IRON_AXE));
            area.set(POS, Blocks.OXIDIZED_COPPER.defaultBlockState());
            assertOneDrop(click(area, offhand, POS), "off hand axe");
            assertTrue(area.get(POS).is(Blocks.WEATHERED_COPPER));

            FakePlayer both = axePlayer(area);
            both.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.IRON_AXE));
            assertOneDrop(click(area, both, POS), "axe in both hands");
            assertTrue(area.get(POS).is(Blocks.EXPOSED_COPPER));
        }
    }
}
