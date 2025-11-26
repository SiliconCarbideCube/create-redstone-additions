package dev.siliconcarbidecube.create_redstone_additions.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CyanQuartz extends Item {
    public CyanQuartz(Item.Properties properties) {
        super(properties.stacksTo(64).rarity(Rarity.COMMON));
    }
}
