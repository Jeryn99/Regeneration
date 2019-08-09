package me.swirtzly.regeneration.common.traits.positive;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.traits.DnaHandler.IDna;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

/**
 * Created by Suffril
 * on 24/01/2019.
 */
public class DnaTough extends IDna {

    private final UUID TOUGH_ID = UUID.fromString("b57c85ba-e5c5-4361-a2cf-3c2fb7347f16");
    private final AttributeModifier TOUGH_MODIFIER = new AttributeModifier(TOUGH_ID, "TOUGH", 0.95, 1);
    private final UUID ATTACK_ID = UUID.fromString("e9e9b6a4-1f41-4569-88a4-34a4b06693bb");
    private final AttributeModifier ATTACK_MODIFIER = new AttributeModifier(ATTACK_ID, "ATTACK", 1, 1);
    private ResourceLocation LOCATION = new ResourceLocation(RegenerationMod.MODID, "tough");

    public DnaTough() {
        super("tough");
    }

    @Override
    public void onUpdate(IRegeneration cap) {

    }

    @Override
    public void onAdded(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (!player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).hasModifier(TOUGH_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(TOUGH_MODIFIER);
        }

        if (!player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(ATTACK_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(ATTACK_MODIFIER);
        }
    }

    @Override
    public void onRemoved(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if (player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).hasModifier(TOUGH_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).removeModifier(TOUGH_MODIFIER);
        }

        if (player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).hasModifier(ATTACK_MODIFIER)) {
            player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(ATTACK_MODIFIER);
        }
    }

    @Override
    public String getLangKey() {
        return "traits." + LOCATION.getPath() + ".name";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return LOCATION;
    }
}
