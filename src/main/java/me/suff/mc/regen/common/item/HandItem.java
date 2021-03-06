package me.suff.mc.regen.common.item;

import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.traits.TraitBase;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RegenSources;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/* Created by Craig on 05/03/2021 */
public class HandItem extends Item {
    public HandItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList< ItemStack > items) {
        if (isInGroup(group)) {
            for (PlayerUtil.SkinType skinType : PlayerUtil.SkinType.values()) {
                if (skinType != PlayerUtil.SkinType.EITHER) {
                    ItemStack itemstack = new ItemStack(this);
                    setSkinType(skinType, itemstack);
                    items.add(itemstack);
                }
            }
        }
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
    public static void setTrait(TraitBase traitBase, ItemStack stack) {
        stack.getOrCreateTag().putString("trait", traitBase.getRegistryName().toString());
    }

    public static Traits.ITrait getTrait(ItemStack stack) {
        return Traits.fromID(stack.getOrCreateTag().getString("trait"));
    }

    public static void createHand(LivingEntity livingEntity) {
        ItemStack itemStack = new ItemStack(RItems.HAND.get());
        RegenCap.get(livingEntity).ifPresent(iRegen -> {
            setSkinType(iRegen.isAlexSkinCurrently() ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE, itemStack);
            if(iRegen.isSkinValidForUse()){
                setSkin(iRegen.getSkin(), itemStack);
            }
            iRegen.setHandState(IRegen.Hand.LEFT_GONE);
        });
        livingEntity.attackEntityFrom(RegenSources.REGEN_DMG_HAND, 3);
        InventoryHelper.spawnItemStack(livingEntity.world, livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(), itemStack);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("Trait: %s", getTrait(stack).getTranslation()));
    }
}
