package me.swirtzly.regeneration.common.item;

import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import me.swirtzly.regeneration.common.traits.DnaHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;

import javax.annotation.Nullable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class ItemHand extends Item {

    public ItemHand() {
        setMaxStackSize(1);

        addPropertyOverride(new ResourceLocation("skin_type"), (stack, worldIn, entityIn) -> getSkinType(stack).equals("ALEX") ? 1 : 0);

    }

    public static void setTimeCreated(ItemStack stack, long created) {
        getStackTag(stack).setLong("created", created);
    }

    public static long getTimeCreated(ItemStack stack) {
        return getStackTag(stack).getLong("created");
    }

    public static void setTextureString(ItemStack stack, String textureString) {
        getStackTag(stack).setString("textureString", textureString);
    }

    public static String getTextureString(ItemStack stack) {
        return getStackTag(stack).getString("textureString");
    }

    public static void setSkinType(ItemStack stack, String skinType) {
        getStackTag(stack).setString("skinType", skinType);
    }

    public static String getSkinType(ItemStack stack) {
        return getStackTag(stack).getString("skinType");
    }

    public static void setTrait(ItemStack stack, String trait) {
        getStackTag(stack).setString("trait", trait);
    }

    public static String getTrait(ItemStack stack) {
        return getStackTag(stack).getString("trait");
    }


    public static void setOwner(ItemStack stack, UUID owner) {
        getStackTag(stack).setUniqueId("owner", owner);
    }

    public static UUID getOwner(ItemStack stack) {
        return getStackTag(stack).getUniqueId("owner");
    }

    public static NBTTagCompound getStackTag(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setString("textureString", "NONE");
            stack.getTagCompound().setString("skinType", SkinInfo.SkinType.ALEX.name());
            stack.getTagCompound().setUniqueId("owner", UUID.fromString("96511168-1bb3-4ff0-a894-271e42606a39"));
            stack.getTagCompound().setLong("created", 0);
            stack.getTagCompound().setString("trait", DnaHandler.DNA_BORING.resourceLocation.toString());
        }
        return stack.getTagCompound();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return new TextComponentTranslation("item.hand.name", UsernameCache.getLastKnownUsername(getOwner(stack))).getUnformattedComponentText();
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Date date = new Date(ItemHand.getTimeCreated(stack));
        DateFormat formatter = new SimpleDateFormat("dd/MM/YYYY @ HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateFormatted = formatter.format(date);
        tooltip.add(new TextComponentTranslation("nbt.created", dateFormatted).getUnformattedComponentText());
        tooltip.add("Trait: " + new TextComponentTranslation(DnaHandler.getDnaEntry(new ResourceLocation(getTrait(stack))).getLangKey()).getUnformattedComponentText());
    }
}
