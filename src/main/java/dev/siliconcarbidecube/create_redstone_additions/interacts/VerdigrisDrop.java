package dev.siliconcarbidecube.create_redstone_additions.interacts;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;

public class VerdigrisDrop {
    public static void tryDrop(BlockEvent.BlockToolModificationEvent event) {
        if (event.isSimulated() || event.getItemAbility() != ItemAbilities.AXE_SCRAPE || event.getFinalState() == null) return;
        if (event.getLevel() instanceof ServerLevel level
                && WeatheringCopper.getPrevious(event.getState()).isPresent()
                && event.getHeldItemStack().canPerformAction(ItemAbilities.AXE_SCRAPE)) {
            int count = 3 + level.random.nextInt(4);
            ItemStack drop = new ItemStack(ModItems.VERDIGRIS.get(), count);
            Block.popResourceFromFace(level, event.getPos(), event.getContext().getClickedFace(), drop);
        }
    }
}
