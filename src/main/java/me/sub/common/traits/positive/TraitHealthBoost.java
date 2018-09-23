package me.sub.common.traits.positive;

import me.sub.common.traits.Trait;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;

/**
 * Created by Sub
 * on 23/09/2018.
 */
public class TraitHealthBoost extends Trait {

    public TraitHealthBoost() {
        super("health", "5d04b5cd-2dbb-4fb2-80c9-9f12ccd69187", 4.0D, 0);
    }

    @Override
    public IAttribute getAttributeToEdit() {
        return SharedMonsterAttributes.MAX_HEALTH;
    }
}
