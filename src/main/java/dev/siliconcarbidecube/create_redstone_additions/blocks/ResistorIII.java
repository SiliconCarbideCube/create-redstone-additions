package dev.siliconcarbidecube.create_redstone_additions.blocks;

public class ResistorIII extends AbstractResistor {
    public ResistorIII(Properties props) {
        super(ResistorIII::new);
    }

    @Override
    protected int attenuation() { return 5; }
}