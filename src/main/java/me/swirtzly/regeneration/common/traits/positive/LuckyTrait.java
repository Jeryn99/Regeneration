package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegen;
import me.swirtzly.regeneration.common.traits.TraitManager;
import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.UUID;

/**
 * Created by Suffril on 24/01/2019.
 */
public class LuckyTrait extends TraitManager.IDna {
	
	private final UUID LUCKY_ID = UUID.fromString("9aaf3f7c-264e-4c19-8485-49503b6940b7");
    private final AttributeModifier LUCKY_MODIFIER = new AttributeModifier(LUCKY_ID, "LUCK", 0.95, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public LuckyTrait() {
        super("lucky");
    }
	
	@Override
    public void onUpdate(IRegen cap) {
        /*LivingEntity player = cap.getLivingEntity();
        if (!player.getAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.LUCK).applyModifier(LUCKY_MODIFIER);
		}*/
	}
	
	@Override
    public void onAdded(IRegen cap) {
      /*  LivingEntity player = cap.getLivingEntity();
        registerAttributeIfAbsent(player, SharedMonsterAttributes.LUCK);
        if (!player.getAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.LUCK).applyModifier(LUCKY_MODIFIER);
		}*/
	}
	
	@Override
    public void onRemoved(IRegen cap) {
     /*   LivingEntity player = cap.getLivingEntity();
        if (player.getAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
            player.getAttribute(SharedMonsterAttributes.LUCK).removeModifier(LUCKY_MODIFIER);
		}*/
	}

}
