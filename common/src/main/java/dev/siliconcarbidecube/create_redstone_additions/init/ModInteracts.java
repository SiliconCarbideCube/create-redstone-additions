// ModInteracts.java
package dev.siliconcarbidecube.create_redstone_additions.init;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.siliconcarbidecube.create_redstone_additions.interacts.VerdigrisDrop;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

public class ModInteracts {
    public static void register() {
        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, pos, face) -> {
            Level level = player.level();
            BlockState state = level.getBlockState(pos);
            ItemStack stack = player.getItemInHand(hand);

            VerdigrisDrop.tryDrop(level, state, stack, pos.getX(), pos.getY(), pos.getZ());

            return EventResult.pass();
        });
    }
}
