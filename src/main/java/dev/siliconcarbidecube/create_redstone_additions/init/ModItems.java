package dev.siliconcarbidecube.create_redstone_additions.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.items.CyanQuartz;
import dev.siliconcarbidecube.create_redstone_additions.items.PolishedCyanQuartz;
import dev.siliconcarbidecube.create_redstone_additions.items.QuartzSemiconductor;
import dev.siliconcarbidecube.create_redstone_additions.items.Verdigris;

public class ModItems {
    public static final Registrate REGISTRATE = CreateRedstoneAdditions.REGISTRATE.defaultCreativeTab(ModTabs.ITEMS_TAB_KEY);

    public static final ItemEntry<CyanQuartz> CYAN_QUARTZ =
            REGISTRATE.item("cyan_quartz", CyanQuartz::new)
                    .register();

    public static final ItemEntry<PolishedCyanQuartz> POLISHED_CYAN_QUARTZ =
            REGISTRATE.item("polished_cyan_quartz", PolishedCyanQuartz::new)
                    .register();

    public static final ItemEntry<Verdigris> VERDIGRIS =
            REGISTRATE.item("verdigris", Verdigris::new)
                    .register();

    public static final ItemEntry<QuartzSemiconductor> QUARTZ_SEMICONDUCTOR =
            REGISTRATE.item("quartz_semiconductor", QuartzSemiconductor::new)
                    .register();

    public static void register() {}
}