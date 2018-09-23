package me.sub.common.traits.positive;

import me.sub.common.traits.Trait;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by Sub
 * on 23/09/2018.
 */
public class TraitSpeed extends Trait {

    public TraitSpeed() {
        super("speed", "e2575f6c-7c0c-4a58-b73e-24210ff487cd", 0.20000000298023224D, 2);
    }

    @Override
    public String getName() {
        return "speed";
    }

    @Override
    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    @Override
    public void update(EntityPlayer player) {

    }

    @Override
    public IAttribute getAttributeToEdit() {
        return SharedMonsterAttributes.MOVEMENT_SPEED;
    }

}
