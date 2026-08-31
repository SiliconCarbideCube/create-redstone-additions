package dev.siliconcarbidecube.create_redstone_additions.init;

import com.simibubi.create.AllCreativeModeTabs;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.util.ResourcePathfinder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(
                    Registries.CREATIVE_MODE_TAB,
                    CreateRedstoneAdditions.MOD_ID
            );

    public static final ResourceKey<CreativeModeTab>
            ITEMS_TAB_KEY =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    ResourcePathfinder.locate("items_tab")
            ),

            BLOCKS_TAB_KEY =
            ResourceKey.create(
                    Registries.CREATIVE_MODE_TAB,
                    ResourcePathfinder.locate("blocks_tab")
            );

    public static final Supplier<CreativeModeTab>
            ITEMS_TAB = CREATIVE_MODE_TAB.register(
            "items_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.CYAN_QUARTZ.get()))
                    .withTabsAfter(BLOCKS_TAB_KEY)
                    .title(Component.translatable("itemGroup.create_redstone_additions.items_tab"))
                    .build()),

            BLOCKS_TAB = CREATIVE_MODE_TAB.register(
            "blocks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.DIODE.get()))
                    .title(Component.translatable("itemGroup.create_redstone_additions.blocks_tab"))
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }

}
