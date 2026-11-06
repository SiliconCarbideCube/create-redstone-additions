package dev.siliconcarbidecube.create_redstone_additions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.simibubi.create.AllTags;
import dev.siliconcarbidecube.create_redstone_additions.ponder.PonderTags;
import dev.siliconcarbidecube.create_redstone_additions.test.TestArea;
import dev.siliconcarbidecube.create_redstone_additions.test.TestServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SmokeTest {
    private static final List<String> BLOCKS = List.of("conjunctor", "disjunctor", "diode", "crossroad", "inverter",
            "golden_resistor", "iron_resistor", "ceramic_resistor", "glass_resistor");
    private static final List<String> ITEMS = List.of("cyan_quartz", "polished_cyan_quartz", "verdigris", "quartz_semiconductor");

    @Test
    void modContentIsRegisteredAndCreateIsLoaded() {
        assertTrue(ModList.get().isLoaded("create"));
        assertTrue(ModList.get().isLoaded(CreateRedstoneAdditions.MOD_ID));
        for (String id : BLOCKS) {
            ResourceLocation key = ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, id);
            assertTrue(BuiltInRegistries.BLOCK.containsKey(key), "block " + id);
            assertTrue(BuiltInRegistries.ITEM.containsKey(key), "block item " + id);
        }
        for (String id : ITEMS) {
            assertTrue(BuiltInRegistries.ITEM.containsKey(ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, id)), "item " + id);
        }
        assertNotNull(TestServer.level());
        assertTrue(TestServer.call(() -> TestServer.get().getRecipeManager()
                .byKey(ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, "crafting/diode")).isPresent()), "mod datapack loaded");
        assertTrue(Blocks.REPEATER.defaultBlockState().is(AllTags.AllBlockTags.WRENCH_PICKUP.tag), "Create tags bound");
    }

    @Test
    void everyRecipeLoadsWithItsResult() {
        Map<String, String> results = Map.ofEntries(
                Map.entry("crafting/ceramic_resistor", "ceramic_resistor"),
                Map.entry("crafting/conjunctor", "conjunctor"),
                Map.entry("crafting/crossroad", "crossroad"),
                Map.entry("crafting/cyan_quartz", "cyan_quartz"),
                Map.entry("crafting/diode", "diode"),
                Map.entry("crafting/disjunctor", "disjunctor"),
                Map.entry("crafting/glass_resistor", "glass_resistor"),
                Map.entry("crafting/golden_resistor", "golden_resistor"),
                Map.entry("crafting/inverter", "inverter"),
                Map.entry("crafting/iron_resistor", "iron_resistor"),
                Map.entry("crafting/quartz_semiconductor", "quartz_semiconductor"),
                Map.entry("crafting/quartz_semiconductor_alt", "quartz_semiconductor"),
                Map.entry("sandpaper_polishing/cyan_quartz", "polished_cyan_quartz"));
        results.forEach((recipe, result) -> {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, recipe);
            ItemStack stack = TestServer.call(() -> TestServer.get().getRecipeManager().byKey(id)
                    .map(RecipeHolder::value).map(r -> r.getResultItem(TestServer.get().registryAccess())).orElse(ItemStack.EMPTY));
            assertEquals(ResourceLocation.fromNamespaceAndPath(CreateRedstoneAdditions.MOD_ID, result),
                    BuiltInRegistries.ITEM.getKey(stack.getItem()), "result of " + recipe);
        });
        long count = TestServer.call(() -> TestServer.get().getRecipeManager().getRecipes().stream()
                .filter(r -> r.id().getNamespace().equals(CreateRedstoneAdditions.MOD_ID)).count());
        assertEquals(results.size(), count, "mod recipe count");
    }

    @Test
    void ponderTagHasLangInEveryLanguage() throws Exception {
        ResourceLocation tag = PonderTags.CREATE_REDSTONE_ADDITIONS_PONDER;
        String key = tag.getNamespace() + ".ponder.tag." + tag.getPath();
        for (String lang : List.of("en_us", "ru_ru")) {
            try (InputStream in = SmokeTest.class.getResourceAsStream("/assets/" + CreateRedstoneAdditions.MOD_ID + "/lang/" + lang + ".json")) {
                assertNotNull(in, lang);
                JsonObject json = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
                assertTrue(json.has(key), lang + " " + key);
                assertTrue(json.has(key + ".description"), lang + " " + key + ".description");
            }
        }
    }

    @Test
    void leverPowersWireAndRepeaterNeedsTicks() {
        try (TestArea area = TestArea.create()) {
            BlockPos lever = new BlockPos(1, 1, 1);
            BlockPos wire = lever.east();
            BlockPos repeater = wire.east();
            BlockPos out = repeater.east();
            area.lever(lever, false);
            area.wire(wire);
            area.set(repeater, Blocks.REPEATER.defaultBlockState().setValue(RepeaterBlock.FACING, Direction.WEST));
            area.wire(out);
            assertEquals(0, area.wirePower(wire));

            area.setLever(lever, true);
            assertEquals(15, area.wirePower(wire));
            assertEquals(15, area.signalInto(wire, Direction.WEST));
            assertEquals(15, area.signalFrom(lever, Direction.EAST));
            assertEquals(0, area.wirePower(out));

            area.tick(1);
            assertEquals(0, area.wirePower(out));
            area.tick(2);
            assertEquals(15, area.wirePower(out));
            assertEquals(15, area.power(out));

            area.setLever(lever, false);
            assertEquals(0, area.wirePower(wire));
            area.tick(3);
            assertEquals(0, area.wirePower(out));
        }
    }

    @Test
    void playerPlacesAndBreaksBlock() {
        try (TestArea area = TestArea.create()) {
            BlockPos pos = new BlockPos(3, 1, 3);
            FakePlayer player = area.player(new ItemStack(Items.DIRT, 2));
            area.useItemOn(player, pos.below(), Direction.UP);
            assertTrue(area.get(pos).is(Blocks.DIRT));
            assertEquals(1, player.getMainHandItem().getCount());

            assertEquals(1, area.drops(pos).size());
            assertTrue(area.breakBlock(player, pos));
            assertTrue(area.get(pos).isAir());
            assertEquals(1, area.countItems(Items.DIRT, pos, 1));
        }
    }
}
