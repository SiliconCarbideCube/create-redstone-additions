package dev.siliconcarbidecube.create_redstone_additions.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class QuartzSemiconductor extends Item {
    public QuartzSemiconductor(Properties properties) {
        super(properties.stacksTo(64).rarity(Rarity.COMMON));
    }
}
