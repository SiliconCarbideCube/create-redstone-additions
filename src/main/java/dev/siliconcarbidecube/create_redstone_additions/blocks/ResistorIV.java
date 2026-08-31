package dev.siliconcarbidecube.create_redstone_additions.blocks;

public class ResistorIV extends AbstractResistor {
    public ResistorIV(Properties props) {
        super(ResistorIV::new);
    }

    @Override
    protected int attenuation() { return 10; }
}