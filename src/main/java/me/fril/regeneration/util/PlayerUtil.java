package me.fril.regeneration.util;

import me.fril.regeneration.client.sound.MovingSoundPlayer;
import me.fril.regeneration.network.MessageSetPerspective;
import me.fril.regeneration.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {
	
	public static void sendHotbarMessage(EntityPlayer player, String message, boolean hotBar) {
		if (!player.world.isRemote) {
			player.sendStatusMessage(new TextComponentTranslation(message), hotBar);
		}
	}
	
	public static void sendHotbarMessage(EntityPlayer player, TextComponentTranslation translation, boolean hotBar) {
		if (!player.world.isRemote) {
			player.sendStatusMessage(translation, hotBar);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void playMovingSound(EntityPlayer player, SoundEvent soundIn, SoundCategory categoryIn) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(player, soundIn, categoryIn));
	}
	
	
	public static void setPerspective(EntityPlayerMP player, boolean thirdperson) {
		NetworkHandler.INSTANCE.sendTo(new MessageSetPerspective(thirdperson), player);
	}
	
	
	public static boolean canEntityAttack(Entity entity) { //NOTE unused
		if (entity instanceof EntityLiving) {
			EntityLiving ent = (EntityLiving) entity;
			for (EntityAITasks.EntityAITaskEntry task : ent.tasks.taskEntries) {
				if (task.action instanceof EntityAIAttackMelee || task.action instanceof EntityAIAttackRanged || task.action instanceof EntityAIAttackRangedBow
						|| task.action instanceof EntityAINearestAttackableTarget || task.action instanceof EntityAIZombieAttack || task.action instanceof EntityAIOwnerHurtByTarget)
					return true;
			}
		}
		return false;
	}
	
	
	public static void damagePlayerArmor(EntityPlayerMP playerMP, int amount) {
		for (EntityEquipmentSlot type : EntityEquipmentSlot.values()) {
			if (!type.equals(EntityEquipmentSlot.MAINHAND) && !type.equals(EntityEquipmentSlot.OFFHAND)) {
				if (playerMP.getItemStackFromSlot(type).getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) playerMP.getItemStackFromSlot(type).getItem();
					armor.setDamage(playerMP.getItemStackFromSlot(type), playerMP.getItemStackFromSlot(type).getItemDamage() - amount);
				}
			}
		}
	}
	
}
