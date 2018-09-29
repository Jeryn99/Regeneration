package me.sub.common.traits.negative;

import me.sub.common.traits.Trait;

public class TraitSentimental extends Trait {

    public TraitSentimental() {
        super("sentimental", "5b62d755-958b-411d-8175-1c4d7205a7a8", 0, 0);
    }

    @Override
    public boolean isPositive() {
        return false;
    }
}
