package me.sub.common.traits.negative;

import me.sub.common.traits.Trait;

/**
 * Created by Sub
 * on 23/09/2018.
 */
public class TraitNone extends Trait {

    public TraitNone() {
        super("no_trait", "814d6541-90b5-4f62-a000-c7fa4a9a3783", 0, 0);
    }


    @Override
    public boolean doesEdit() {
        return false;
    }

}
