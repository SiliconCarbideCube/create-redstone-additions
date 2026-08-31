package dev.siliconcarbidecube.create_redstone_additions.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.siliconcarbidecube.create_redstone_additions.CreateRedstoneAdditions;
import dev.siliconcarbidecube.create_redstone_additions.blocks.*;

public class ModBlocks {
    public static final Registrate REGISTRATE = CreateRedstoneAdditions.REGISTRATE.defaultCreativeTab(ModTabs.BLOCKS_TAB_KEY);

    public static final BlockEntry<Conjunctor> CONJUNCTOR =
            REGISTRATE.block("conjunctor", Conjunctor::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<Disjunctor> DISJUNCTOR =
            REGISTRATE.block("disjunctor", Disjunctor::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<Diode> DIODE =
            REGISTRATE.block("diode", Diode::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<Crossroad> CROSSROAD =
            REGISTRATE.block("crossroad", Crossroad::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<Inverter> INVERTER =
            REGISTRATE.block("inverter", Inverter::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<ResistorI> GOLDEN_RESISTOR =
            REGISTRATE.block("golden_resistor", ResistorI::new)
                    .item()
//                        .tab(ModTabs.CREATE_REDSTONE_ADDITIONS_TAB_KEY)
                    .build()
                    .register();

    public static final BlockEntry<ResistorII> IRON_RESISTOR =
            REGISTRATE.block("iron_resistor", ResistorII::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<ResistorIII> CERAMIC_RESISTOR =
            REGISTRATE.block("ceramic_resistor", ResistorIII::new)
                    .item()
                    .build()
                    .register();

    public static final BlockEntry<ResistorIV> GLASS_RESISTOR =
            REGISTRATE.block("glass_resistor", ResistorIV::new)
                    .item()
                    .build()
                    .register();

    public static void register() {
    }
}
