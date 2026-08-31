package dev.siliconcarbidecube.create_redstone_additions.blocks;

public class ResistorI extends AbstractResistor {
    public ResistorI(Properties props) {
        super(ResistorI::new);
    }

    @Override
    protected int attenuation() { return 1; }
}