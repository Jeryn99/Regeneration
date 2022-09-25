package me.craig.software.regen.common.traits;

import me.craig.software.regen.common.regen.IRegen;

public class TraitBase extends AbstractTrait {
    private int color;

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
