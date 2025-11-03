package dev.siliconcarbidecube.create_redstone_additions.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class IntegratedCircuit extends Item {
    public IntegratedCircuit() {
        super((new Properties()).stacksTo(64).rarity(Rarity.COMMON));
    }
}
