package dev.siliconcarbidecube.create_redstone_additions.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class OxidizedCopperNugget extends Item {
    public OxidizedCopperNugget() {
        super((new Properties()).stacksTo(64).rarity(Rarity.COMMON));
    }
}
