package me.suff.regeneration.util;

import me.suff.regeneration.client.skinhandling.SkinChangingHandler;
import me.suff.regeneration.network.MessageSetPerspective;
import me.suff.regeneration.network.MessageUpdateModel;
import me.suff.regeneration.network.NetworkHandler;
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
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {
	
	public static ArrayList<Potion> POTIONS = new ArrayList<>();
	
	public static void createPostList() {
		POTIONS.add(MobEffects.WEAKNESS);
		POTIONS.add(MobEffects.BLINDNESS);
		POTIONS.add(MobEffects.MINING_FATIGUE);
		POTIONS.add(MobEffects.RESISTANCE);
		POTIONS.add(MobEffects.HEALTH_BOOST);
		POTIONS.add(MobEffects.HUNGER);
		POTIONS.add(MobEffects.WATER_BREATHING);
		POTIONS.add(MobEffects.HASTE);
	}
	
	public static void sendMessage(EntityPlayer player, String message, boolean hotBar) {
		if (!player.world.isRemote) {
			player.sendStatusMessage(new TextComponentTranslation(message), hotBar);
		}
	}
	
	public static void sendMessage(EntityPlayer player, TextComponentTranslation translation, boolean hotBar) {
		if (!player.world.isRemote) {
			player.sendStatusMessage(translation, hotBar);
		}
	}
	
	public static void sendMessageToAll(TextComponentTranslation translation) {
		List<EntityPlayerMP> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
		players.forEach(playerMP -> sendMessage(playerMP, translation, false));
	}
	
	public static void setPerspective(EntityPlayerMP player, boolean thirdperson, boolean resetPitch) {
		NetworkHandler.sendTo(new MessageSetPerspective(thirdperson, resetPitch), player);
	}
	
	public static boolean canEntityAttack(Entity entity) { // NOTE unused
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
	
	public static void updateModel(SkinChangingHandler.EnumChoices choice) {
		NetworkHandler.sendToServer(new MessageUpdateModel(choice.name()));
	}
	
	public static boolean applyPotionIfAbsent(EntityPlayer player, Potion potion, int length, int amplifier, boolean ambient, boolean showParticles) {
		if (potion == null) return false;
		if (player.getActivePotionEffect(potion) == null) {
			player.addPotionEffect(new PotionEffect(potion, length, amplifier, ambient, showParticles));
			return true;
		}
		return false;
	}
	
}
