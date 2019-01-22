package me.fril.regeneration.common.item;

import me.fril.regeneration.common.capability.CapabilityRegeneration;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.handlers.RegenObjects;
import me.fril.regeneration.network.MessageRemovePlayer;
import me.fril.regeneration.network.NetworkHandler;
import me.fril.regeneration.util.PlayerUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//@Mod.EventBusSubscriber(modid = RegenerationMod.MODID)
public class ItemImNotSure extends Item {
	
	@SubscribeEvent
	public static void onKilled(LivingDeathEvent e) {
		if (e.getEntity().world.isRemote) return;
		if (e.getEntityLiving() instanceof EntityPlayer && e.getSource().getTrueSource() instanceof EntityPlayer && e.getSource() == RegenObjects.REGEN_DMG_THEFT) {
			EntityPlayer victim = (EntityPlayer) e.getEntityLiving();
			IRegeneration dataVictim = CapabilityRegeneration.getForPlayer(victim);
			EntityPlayer attacker = (EntityPlayer) e.getSource().getTrueSource();
			
			//	if (!dataVictim.canRegenerate() && attacker.getHeldItemMainhand().getItem() == RegenObjects.Items.IDKYET) {
				if (attacker != null) {
					IRegeneration dataAttacker = CapabilityRegeneration.getForPlayer(attacker);
					
					if (!dataAttacker.canRegenerate()) {
						PlayerUtil.sendMessage(attacker, new TextComponentTranslation("regeneration.messages.theft", victim.getName()), true);
						dataAttacker.receiveRegenerations(1);
						attacker.getHeldItemMainhand().setCount(0);
						dataAttacker.setEncodedSkin(dataVictim.getEncodedSkin());
						dataAttacker.setSkinType(dataVictim.getSkinType().name());
						NetworkHandler.INSTANCE.sendToAll(new MessageRemovePlayer(attacker.getUniqueID()));
						attacker.world.playSound(null, attacker.getPosition(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, 1, 1);
					}
				}
			//	}
		}
	}
	
	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (target instanceof EntityPlayer) {
			EntityPlayer targetPlayer = (EntityPlayer) target;
			IRegeneration data = CapabilityRegeneration.getForPlayer(targetPlayer);
			targetPlayer.attackEntityFrom(RegenObjects.REGEN_DMG_THEFT, 1F);
			stack.damageItem(1, attacker);
			return true;
		}
		return false;
	}
	
}
