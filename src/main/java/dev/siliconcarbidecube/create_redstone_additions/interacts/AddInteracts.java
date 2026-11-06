package dev.siliconcarbidecube.create_redstone_additions.interacts;

import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

public class AddInteracts {

    public static void add() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, BlockEvent.BlockToolModificationEvent.class, VerdigrisDrop::tryDrop);
    }

}
