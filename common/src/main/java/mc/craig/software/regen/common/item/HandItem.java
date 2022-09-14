package mc.craig.software.regen.common.item;

import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenSources;
import mc.craig.software.regen.util.RegenUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
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
        CompoundTag stackTag = stack.getOrCreateTag();
        if (stackTag.contains("user")) {
            return stackTag.getUUID("user");
        }
        return null;
    }

    public static void createHand(LivingEntity livingEntity) {
        ItemStack itemStack = new ItemStack(RItems.HAND.get());
        RegenerationData.get(livingEntity).ifPresent(iRegen -> {
            setUUID(livingEntity.getUUID(), itemStack);
            setSkinType(iRegen.currentlyAlex() ? PlayerUtil.SkinType.ALEX : PlayerUtil.SkinType.STEVE, itemStack);
            setEnergy(0, itemStack);
            if (iRegen.isSkinValidForUse()) {
                setSkin(iRegen.skin(), itemStack);
            }
            iRegen.setHandState(IRegen.Hand.LEFT_GONE);
        });
        livingEntity.hurt(RegenSources.REGEN_DMG_HAND, 3);
        Containers.dropItemStack(livingEntity.level, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), itemStack);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
       /* if (stack.getOrCreateTag().contains("user")) {
            return Component.translatable("item.regen.hand_with_arg", UsernameCache.getLastKnownUsername(getUUID(stack)) + "'s");
        }*///TODO
        return super.getName(stack);
    }

    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (allowedIn(group)) {
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
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
       //TODO  tooltip.add(Component.translatable(ChatFormatting.WHITE + "Trait: %s", ChatFormatting.GRAY + ChatFormatting.ITALIC.toString() + getTrait(stack).translation().getString()));
        tooltip.add(Component.translatable(ChatFormatting.WHITE + "Energy: %s", ChatFormatting.GRAY + ChatFormatting.ITALIC.toString() + RegenUtil.round(getEnergy(stack), 2)));
    }
}
