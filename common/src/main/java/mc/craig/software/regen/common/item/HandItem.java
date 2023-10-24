package mc.craig.software.regen.common.item;

import mc.craig.software.regen.common.item.tooltip.hand.HandSkinToolTip;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
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

    public static void setPlayerName(String name, ItemStack stack) {
        stack.getOrCreateTag().putString("name", name);
    }

    public static String getPlayerName(ItemStack stack) {
        return stack.getOrCreateTag().getString("name");
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
        stack.getOrCreateTag().putString("trait", TraitRegistry.TRAITS_REGISTRY.getKey(traitBase).toString());
    }

    public static TraitBase getTrait(ItemStack stack) {
        return TraitRegistry.TRAITS_REGISTRY.get(new ResourceLocation(stack.getOrCreateTag().getString("trait")));
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
        RegenerationData.get(livingEntity).ifPresent(iRegen -> {
            setUUID(livingEntity.getUUID(), itemStack);
            setSkinType(iRegen.currentlyAlex() ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE, itemStack);
            setTrait(iRegen.getCurrentTrait(), itemStack);
            setEnergy(0, itemStack);
            setPlayerName(livingEntity.getName().getString(), itemStack);
            if (iRegen.isSkinValidForUse()) {
                setSkin(iRegen.skin(), itemStack);
            }
            iRegen.setHandState(IRegen.Hand.CUT);
        });
        livingEntity.hurt(new DamageSource(RegenDamageTypes.getHolder(livingEntity, RegenDamageTypes.REGEN_DMG_HAND)), 3);
        Containers.dropItemStack(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), itemStack);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        if (stack.getOrCreateTag().contains("user") && stack.getOrCreateTag().contains("name")) {
            return Component.translatable("item.regen.hand_with_name", stack.getOrCreateTag().getString("name") + "'s");
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (getTrait(stack) != null) {
            tooltip.add(Component.translatable("item.regen.tooltip.trait", ChatFormatting.GRAY + ChatFormatting.ITALIC.toString() + getTrait(stack).getTitle().getString()));
        }
        tooltip.add(Component.translatable("item.regen.tooltip.energy", ChatFormatting.GRAY + ChatFormatting.ITALIC.toString() + RegenUtil.round(getEnergy(stack), 2)));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new HandSkinToolTip(getSkin(stack), isAlex(stack)));
    }
}