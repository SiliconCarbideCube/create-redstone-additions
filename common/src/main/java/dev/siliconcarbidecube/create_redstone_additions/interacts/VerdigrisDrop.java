// VerdigrisDrop.java
package dev.siliconcarbidecube.create_redstone_additions.interacts;

import net.minecraft.world.Containers;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import dev.siliconcarbidecube.create_redstone_additions.init.ModItems;

public class VerdigrisDrop {
    public static void tryDrop(Level level, BlockState state, ItemStack stack, int x, int y, int z) {
        if (stack.getItem() instanceof AxeItem && WeatheringCopper.getPrevious(state).isPresent() && !level.isClientSide) {
            int count = 3 + level.random.nextInt(4);
            ItemStack drop = new ItemStack(ModItems.VERDIGRIS.get(), count);
            Containers.dropItemStack(level, x, y, z, drop);
        }
    }
}
