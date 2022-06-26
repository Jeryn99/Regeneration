package craig.software.mc.regen.common.traits;

import craig.software.mc.regen.common.regen.IRegen;

public class TraitBase extends AbstractTrait {
    private final int color;

    public TraitBase(int color) {
        this.color = color;
    }

    @Override
    public void apply(IRegen data) {

    }

    @Override
    public void remove(IRegen data) {

    }

    @Override
    public void tick(IRegen data) {

    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public int color() {
        return color;
    }
}
