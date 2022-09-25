package me.craig.software.regen.common.item;

import me.craig.software.regen.common.objects.RItems;
import me.craig.software.regen.common.regen.IRegen;
import me.craig.software.regen.common.regen.RegenCap;
import me.craig.software.regen.common.traits.AbstractTrait;
import me.craig.software.regen.common.traits.RegenTraitRegistry;
import me.craig.software.regen.util.PlayerUtil;
import me.craig.software.regen.util.RegenSources;
import me.craig.software.regen.util.RegenUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

/* Created by Craig on 05/03/2021 */
public class HandItem extends Item {
    public HandItem(Properties properties) {
        super(properties);
    }

    //Skin Type
    public static void setSkinType(PlayerUtil.SkinType skinType, ItemStack stack) {
        stack.getOrCreateTag().putBoolean("is_alex", skinType.isAlex());
    }

    public static void setSkin(byte[] skin, ItemStack stack) {
        stack.getOrCreateTag().putByteArray("skin", skin);
    }

    public static byte[] getSkin(ItemStack stack) {
        return stack.getOrCreateTag().getByteArray("skin");
    }

    public static boolean isAlex(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("is_alex");
    }

    //Trait
    public static void setTrait(AbstractTrait traitBase, ItemStack stack) {
        stack.getOrCreateTag().putString("trait", traitBase.getRegistryName().toString());
    }

    public static AbstractTrait getTrait(ItemStack stack) {
        return RegenTraitRegistry.fromID(stack.getOrCreateTag().getString("trait"));
    }

    public static void setEnergy(float energy, ItemStack stack) {
        stack.getOrCreateTag().putFloat("energy", energy);
    }

    public static float getEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getFloat("energy");
    }

    public static void setUUID(UUID uuid, ItemStack stack) {
        stack.getOrCreateTag().putUUID("user", uuid);
    }

    public static UUID getUUID(ItemStack stack) {
        if (stack.getOrCreateTag().contains("user")) {
            return stack.getOrCreateTag().getUUID("user");
        }
        return null;
    }

    public static void createHand(LivingEntity livingEntity) {
        ItemStack itemStack = new ItemStack(RItems.HAND.get());
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            setUUID(livingEntity.getUUID(), itemStack);
            setSkinType(iRegen.currentlyAlex() ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE, itemStack);
            setTrait(iRegen.trait(), itemStack);
            setEnergy(0, itemStack);
            if (iRegen.isSkinValidForUse()) {
                setSkin(iRegen.skin(), itemStack);
            }
            iRegen.setHandState(IRegen.Hand.LEFT_GONE);
        });
        livingEntity.hurt(RegenSources.REGEN_DMG_HAND, 3);
        InventoryHelper.dropItemStack(livingEntity.level, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), itemStack);
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        if (stack.getOrCreateTag().contains("user")) {
            return new TranslationTextComponent("item.regen.hand_with_arg", UsernameCache.getLastKnownUsername(getUUID(stack)) + "'s");
        }
        return super.getName(stack);
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
            for (PlayerUtil.SkinType skinType : PlayerUtil.SkinType.values()) {
                if (skinType != PlayerUtil.SkinType.EITHER) {
                    ItemStack itemstack = new ItemStack(this);
                    setSkinType(skinType, itemstack);
                    items.add(itemstack);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Trait: %s", TextFormatting.GRAY + TextFormatting.ITALIC.toString() + getTrait(stack).translation().getString()));
        tooltip.add(new TranslationTextComponent(TextFormatting.WHITE + "Energy: %s", TextFormatting.GRAY + TextFormatting.ITALIC.toString() + RegenUtil.round(getEnergy(stack), 2)));
    }
}
