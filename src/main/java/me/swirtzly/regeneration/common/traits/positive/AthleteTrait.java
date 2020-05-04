package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

/**
 * Created by Suffril on 24/01/2019.
 */
public class AthleteTrait extends TraitManager.IDna {
	
	private final UUID SPEED_ID = UUID.fromString("a22a9515-90d7-479d-9153-07268f2a1714");
    private final AttributeModifier SPEED_MODIFIER = new AttributeModifier(SPEED_ID, "SANIC_FAST", 0.95, AttributeModifier.Operation.MULTIPLY_BASE);
	private final UUID KNOCKBACK_ID = UUID.fromString("49906f69-7b9d-4967-aba8-901621ee76a5");
    private final AttributeModifier KNOCKBACK_MODIFIER = new AttributeModifier(KNOCKBACK_ID, "JUMPY", 0.95, AttributeModifier.Operation.MULTIPLY_BASE);

    public AthleteTrait() {
        super("athlete");
    }
	
	@Override
    public void onUpdate(IRegen cap) {
		LivingEntity player = cap.getPlayer();
		if (player.isSprinting()) {
			onAdded(cap);
		} else {
			onRemoved(cap);
		}
	}
	
	@Override
    public void onAdded(IRegen cap) {
		LivingEntity player = cap.getPlayer();
        if (!player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(SPEED_MODIFIER);
        }

        if (!player.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).hasModifier(KNOCKBACK_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(KNOCKBACK_MODIFIER);
		}
	}
	
	@Override
    public void onRemoved(IRegen cap) {
		LivingEntity player = cap.getPlayer();
        if (player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SPEED_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER);
        }

        if (player.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).hasModifier(KNOCKBACK_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(KNOCKBACK_MODIFIER);
		}
	}

    @Override
	public String getLocalDesc() {
		return "asdsa";
	}

}
