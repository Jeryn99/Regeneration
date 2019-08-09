package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaLucky extends DnaHandler.IDna {

    private final UUID LUCKY_ID = UUID.fromString("9aaf3f7c-264e-4c19-8485-49503b6940b7");
    private final AttributeModifier LUCKY_MODIFIER = new AttributeModifier(LUCKY_ID, "LUCK", 0.95, 2);

    public DnaLucky() {
        super("lucky");
    }

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (!player.getEntityAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.LUCK).applyModifier(LUCKY_MODIFIER);
        }
    }

    @Override
    public void onAdded(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (!player.getEntityAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.LUCK).applyModifier(LUCKY_MODIFIER);
        }
    }

    @Override
    public void onRemoved(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (player.getEntityAttribute(SharedMonsterAttributes.LUCK).hasModifier(LUCKY_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.LUCK).removeModifier(LUCKY_MODIFIER);
        }
    }

}
