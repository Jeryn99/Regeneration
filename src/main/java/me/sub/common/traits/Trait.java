package me.sub.common.traits;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

/**
 * Created by Sub
 * on 23/09/2018.
 */
public class Trait {

    public AttributeModifier modifier;
    public String uuid;
    private String name;
    private double amount;
    private int operation;

    public Trait(String name, String uuid, double amount, int operation) {
        this.name = name;
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
        this.modifier = new AttributeModifier(UUID.fromString(uuid), name, amount, operation);
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return UUID.fromString(uuid);
    }

    public TextComponentTranslation getTranslatedName() {
        return new TextComponentTranslation("trait.messages." + getName());
    }

    public boolean doesEdit() {
        return true;
    }

    public boolean isPositive() {
        return true;
    }

    public IAttribute getAttributeToEdit() {
        return null;
    }

    public void onTraitRemove(EntityPlayer player) {
        if (!doesEdit()) {
            if (!player.getEntityAttribute(getAttributeToEdit()).hasModifier(modifier)) {
                player.getEntityAttribute(getAttributeToEdit()).removeModifier(modifier);
            }
        }
    }

    public void onTraitAdd(EntityPlayer player) {
        if (!doesEdit()) {
            if (!player.getEntityAttribute(getAttributeToEdit()).hasModifier(modifier)) {
                player.getEntityAttribute(getAttributeToEdit()).applyModifier(modifier);
            }
        }
    }

    public void update(EntityPlayer player) {
    }
}
