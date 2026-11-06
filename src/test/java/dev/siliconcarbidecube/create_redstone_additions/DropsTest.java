package dev.siliconcarbidecube.create_redstone_additions;

import dev.siliconcarbidecube.create_redstone_additions.init.ModBlocks;
import dev.siliconcarbidecube.create_redstone_additions.test.TestArea;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class DropsTest {
    static final List<Supplier<? extends Block>> BLOCKS = List.of(ModBlocks.CONJUNCTOR, ModBlocks.DISJUNCTOR, ModBlocks.DIODE,
            ModBlocks.CROSSROAD, ModBlocks.INVERTER, ModBlocks.GOLDEN_RESISTOR, ModBlocks.IRON_RESISTOR,
            ModBlocks.CERAMIC_RESISTOR, ModBlocks.GLASS_RESISTOR);

    @Test
    void survivalBreakDropsTheBlock() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = area.player(ItemStack.EMPTY);
            BlockPos pos = new BlockPos(3, 1, 3);
            for (Supplier<? extends Block> block : BLOCKS) {
                area.set(pos, block.get().defaultBlockState());
                assertEquals(1, area.drops(pos).size(), block.get() + " loot");
                assertTrue(area.breakBlock(player, pos));
                assertTrue(area.get(pos).isAir());
                assertEquals(1, area.countItems(block.get().asItem(), pos, 1), block.get() + " dropped");
                area.clearItems();
            }
        }
    }

    @Test
    void popsOffWhenSupportIsRemoved() {
        try (TestArea area = TestArea.create()) {
            BlockPos support = new BlockPos(3, 1, 3);
            BlockPos pos = support.above();
            for (Supplier<? extends Block> block : BLOCKS) {
                area.set(support, Blocks.STONE.defaultBlockState());
                area.set(pos, block.get().defaultBlockState());
                area.clear(support);
                assertTrue(area.get(pos).isAir(), block.get() + " removed");
                assertEquals(1, area.countItems(block.get().asItem(), pos, 1.5), block.get() + " dropped");
                area.clearItems();
            }
        }
    }

    @Test
    void creativeBreakDropsNothing() {
        try (TestArea area = TestArea.create()) {
            FakePlayer player = area.player(ItemStack.EMPTY);
            player.setGameMode(GameType.CREATIVE);
            BlockPos pos = new BlockPos(3, 1, 3);
            area.set(pos, ModBlocks.DIODE.get().defaultBlockState());
            assertTrue(area.breakBlock(player, pos));
            assertTrue(area.get(pos).isAir());
            assertTrue(area.items(pos, 1).isEmpty());
        }
    }

    @Test
    void silkTouchAndExplosionsDropLikeRepeater() {
        try (TestArea area = TestArea.create()) {
            area.call(level -> {
                ItemStack silkPick = new ItemStack(Items.DIAMOND_PICKAXE);
                silkPick.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH), 1);
                assertEquals(1, drops(level, Blocks.REPEATER, silkPick, null));
                assertEquals(1, drops(level, Blocks.REPEATER, ItemStack.EMPTY, 1F));
                assertEquals(0, drops(level, Blocks.REPEATER, ItemStack.EMPTY, Float.MAX_VALUE));
                for (Supplier<? extends Block> block : BLOCKS) {
                    assertTrue(block.get().defaultBlockState().is(BlockTags.MINEABLE_WITH_PICKAXE), block.get() + " pickaxe");
                    assertEquals(1, drops(level, block.get(), silkPick, null), block.get() + " silk touch");
                    assertEquals(1, drops(level, block.get(), ItemStack.EMPTY, 1F), block.get() + " small explosion");
                    assertEquals(0, drops(level, block.get(), ItemStack.EMPTY, Float.MAX_VALUE), block.get() + " huge explosion");
                }
                return null;
            });
        }
    }

    private static int drops(ServerLevel level, Block block, ItemStack tool, Float explosionRadius) {
        LootParams.Builder params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                .withParameter(LootContextParams.TOOL, tool)
                .withOptionalParameter(LootContextParams.EXPLOSION_RADIUS, explosionRadius);
        return block.defaultBlockState().getDrops(params).stream()
                .filter(stack -> stack.is(block.asItem())).mapToInt(ItemStack::getCount).sum();
    }
}
