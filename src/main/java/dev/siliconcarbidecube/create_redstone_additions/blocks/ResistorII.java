package dev.siliconcarbidecube.create_redstone_additions.blocks;

public class ResistorII extends AbstractResistor {
    public ResistorII(Properties props) {
        super(ResistorII::new);
    }

    @Override
    protected int attenuation() { return 2; }
}