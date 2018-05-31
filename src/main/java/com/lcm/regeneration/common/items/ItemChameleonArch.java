package com.lcm.regeneration.common.items;

import com.lcm.regeneration.Regeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.ITimelordCapability;
import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityTimelord;
import com.lcm.regeneration.events.RObjects;
import com.lcm.regeneration.utils.RegenConfig;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Created by AFlyingGrayson on 8/28/17
 */
public class ItemChameleonArch extends Item {

	public ItemChameleonArch() { // CHECK how should combining/repairing work out?
		setUnlocalizedName("chameleonArch");
		setRegistryName(Regeneration.MODID, "chameleonarch");
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
		setMaxDamage(RegenConfig.regenCapacity);
	}

	@Override public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack arch = player.getHeldItem(hand);
		ITimelordCapability handler = player.getCapability(CapabilityTimelord.TIMELORD_CAP, null);
		if(handler == null) return new ActionResult<>(EnumActionResult.PASS, arch);
		player.world.playSound(null, player.posX, player.posY, player.posZ, RObjects.SoundEvents.timeyWimey, SoundCategory.PLAYERS, 1.0F, 1.0F);

		if (arch.getTagCompound() == null) {
			arch.setTagCompound(new NBTTagCompound());
			arch.getTagCompound().setBoolean("open", true);
		}
		if (arch.getItemDamage() == RegenConfig.regenCapacity) {
			player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.transfer.emptyArch")), true);
			return new ActionResult<>(EnumActionResult.FAIL, arch);
		}

		if (!handler.isTimelord()) {
			handler.setTimelord(true);
			doUsageDamage(arch, handler);
			player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.becomeTimelord")), true);
		} else {

			if (!player.isSneaking()) {
				int used = doUsageDamage(arch, handler);
				if (used == 0) {
					if (handler.getRegensLeft() == RegenConfig.regenCapacity) {
						arch.getTagCompound().setBoolean("open", false);
						player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.transfer.fullCycle", used)), true);
					} else if (arch.getItemDamage() == RegenConfig.regenCapacity)
						player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.transfer.emptyArch", used)), true);
					return new ActionResult<>(EnumActionResult.FAIL, arch);
				}
				player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.gainedRegenerations", used)), true); // too lazy to fix a single/plural issue here
			} else {
				if (arch.getItemDamage() == 0 && !player.isCreative()) {
					player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.transfer.fullArch")), true);
					return new ActionResult<>(EnumActionResult.FAIL, arch);
				} else if (handler.getRegensLeft() < 1) {
					player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.transfer.emptyCycle")), true);
					return new ActionResult<>(EnumActionResult.FAIL, arch);
				}

				// TODO sound effect?
				arch.setItemDamage(arch.getItemDamage() - 1);
				handler.setRegensLeft(handler.getRegensLeft() - 1);
				player.sendStatusMessage(new TextComponentString(StringHelper.translateToLocal("lcm-atg.messages.transfer")), true);
				arch.getTagCompound().setBoolean("open", false);
				return new ActionResult<>(EnumActionResult.PASS, arch);
			}
		}
		return new ActionResult<>(EnumActionResult.PASS, arch);
	}

	private int doUsageDamage(ItemStack stack, ITimelordCapability handler) {
		int supply = RegenConfig.regenCapacity - stack.getItemDamage(), needed = RegenConfig.regenCapacity - handler.getRegensLeft(), used = Math.min(supply, needed);
		if (used == 0)
			return 0;

		handler.setRegensLeft(handler.getRegensLeft() + used);
		handler.syncToAll();

		if (!handler.getPlayer().isCreative())
			stack.setItemDamage(stack.getItemDamage() + used);
		return used;
	}
}
