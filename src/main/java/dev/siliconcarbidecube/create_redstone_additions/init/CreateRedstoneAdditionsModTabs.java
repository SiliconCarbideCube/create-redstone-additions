package dev.siliconcarbidecube.create_redstone_additions.init;

//import com.simibubi.create.Create;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.CreativeModeTabs;
//import net.minecraft.world.level.ItemLike;
//import net.minecraft.world.level.block.Block;
//import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CreateRedstoneAdditionsModTabs {
//    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "create_redstone_additions");
//
//    @SubscribeEvent
//    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
//        if (tabData.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.INVERTER_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.CONJUNCTOR_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.DIODE_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.GOLDEN_RESISTOR_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.IRON_RESISTOR_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.CERAMIC_RESISTOR_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.GLASS_RESISTOR_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.DISJUNCTOR_BLOCK.get()).asItem());
//            //tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.EXCLUSIVE_DISJUNCTOR_BLOCK.get()).asItem());
//            tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK.get()).asItem());
//            //tabData.accept((ItemLike)((Block)CreateRedstoneAdditionsModBlocks.METRONOME_BLOCK.get()).asItem());
//        } else
//        if (tabData.getTabKey() == CreativeModeTabs.INGREDIENTS) {
//            tabData.accept((ItemLike)CreateRedstoneAdditionsModItems.CYAN_QUARTZ.get());
//            tabData.accept((ItemLike)CreateRedstoneAdditionsModItems.POLISHED_CYAN_QUARTZ.get());
//            tabData.accept((ItemLike)CreateRedstoneAdditionsModItems.INTEGRATED_CIRCUIT.get());
//        }
//    }

    public static final DeferredRegister<CreativeModeTab> REGISTRY =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateRedstoneAdditions.MODID);

    public static final RegistryObject<CreativeModeTab> REDSTONE_ADDITIONS_TAB = REGISTRY.register("redstone_additions", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("tab.create_redstone_additions"))
                    .icon(() -> new ItemStack(CreateRedstoneAdditionsModItems.CYAN_QUARTZ.get()))
                    .displayItems((parameters, output) -> {
                        // Blocks
                        output.accept(CreateRedstoneAdditionsModBlocks.INVERTER_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.CONJUNCTOR_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.DIODE_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.GOLDEN_RESISTOR_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.IRON_RESISTOR_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.CERAMIC_RESISTOR_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.GLASS_RESISTOR_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.DISJUNCTOR_BLOCK.get());
                        output.accept(CreateRedstoneAdditionsModBlocks.CROSSROAD_BLOCK.get());
                        // Items
                        output.accept(CreateRedstoneAdditionsModItems.CYAN_QUARTZ.get());
                        output.accept(CreateRedstoneAdditionsModItems.POLISHED_CYAN_QUARTZ.get());
//                        output.accept(CreateRedstoneAdditionsModItems.INTEGRATED_CIRCUIT.get());
                        output.accept(CreateRedstoneAdditionsModItems.OXIDIZED_COPPER_INGOT.get());
                        output.accept(CreateRedstoneAdditionsModItems.OXIDIZED_COPPER_NUGGET.get());
                    })
                    .build()
    );
}