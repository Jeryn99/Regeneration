package me.swirtzly.regeneration.util;

import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.network.NetworkDispatcher;
import me.swirtzly.regeneration.network.ThirdPersonMessage;
import me.swirtzly.regeneration.network.UpdateSkinMapMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {
	
	public static ArrayList<Effect> POTIONS = new ArrayList<>();
	
	public static void createPostList() {
		POTIONS.add(Effects.WEAKNESS);
		POTIONS.add(Effects.BLINDNESS);
		POTIONS.add(Effects.MINING_FATIGUE);
		POTIONS.add(Effects.RESISTANCE);
		POTIONS.add(Effects.HEALTH_BOOST);
		POTIONS.add(Effects.HUNGER);
		POTIONS.add(Effects.WATER_BREATHING);
		POTIONS.add(Effects.HASTE);
	}
	
	public static void sendMessage(PlayerEntity player, String message, boolean hotBar) {
		if (!player.world.isRemote) {
			player.sendStatusMessage(new TranslationTextComponent(message), hotBar);
		}
	}
	
	public static void sendMessage(PlayerEntity player, TranslationTextComponent translation, boolean hotBar) {
		if (!player.world.isRemote) {
			player.sendStatusMessage(translation, hotBar);
		}
	}
	
	public static void sendMessageToAll(TranslationTextComponent translation) {
		List<ServerPlayerEntity> players = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
		players.forEach(playerMP -> sendMessage(playerMP, translation, false));
	}
	
	public static void setPerspective(ServerPlayerEntity player, boolean thirdperson, boolean resetPitch) {
		NetworkDispatcher.sendTo(new ThirdPersonMessage(thirdperson), player);
	}


    public static void updateModel(SkinManipulation.EnumChoices choice) {
		NetworkDispatcher.INSTANCE.sendToServer(new UpdateSkinMapMessage(choice.name()));
	}
	
	public static boolean applyPotionIfAbsent(PlayerEntity player, Effect potion, int length, int amplifier, boolean ambient, boolean showParticles) {
		if (potion == null) return false;
		if (player.getActivePotionEffect(potion) == null) {
			player.addPotionEffect(new EffectInstance(potion, length, amplifier, ambient, showParticles));
			return true;
		}
		return false;
	}
	
	public enum RegenState {
		
		ALIVE,
		GRACE, GRACE_CRIT, POST,
		REGENERATING;
		
		public boolean isGraceful() {
			return this == GRACE || this == GRACE_CRIT;
		}
		
		public enum Transition {
			HAND_GLOW_START(Color.YELLOW.darker()), HAND_GLOW_TRIGGER(Color.ORANGE),
			ENTER_CRITICAL(Color.BLUE),
			CRITICAL_DEATH(Color.RED),
			FINISH_REGENERATION(Color.GREEN.darker()),
			END_POST(Color.PINK.darker());
			
			public final Color color;
			
			Transition(Color col) {
				this.color = col;
			}
		}
		
	}
}
