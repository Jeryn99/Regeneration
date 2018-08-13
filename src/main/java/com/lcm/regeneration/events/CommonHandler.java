package com.lcm.regeneration.events;

import com.lcm.regeneration.Regeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationEvent;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationFinishEvent;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationStartEvent;
import com.lcm.regeneration.common.trait.TraitHandler;
import com.lcm.regeneration.utils.RegenConfig;
import com.lcm.regeneration.utils.RegenUtils;

import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class CommonHandler {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.PlayerTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END))
            return;
        if (!event.player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null))
            return;
        IRegenerationCapability handler = event.player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
        handler.update();
    }

    @SubscribeEvent
    public static void onRegeneration(RegenerationEvent event) {
        event.getEntityPlayer().extinguish();
        event.getEntityPlayer().setArrowCountInEntity(0);
    }


    @SubscribeEvent
    public static void onRegenerationExplosion(RegenerationEvent.RegenerationExplosionEvent event) {
        event.getEntityPlayer().extinguish();
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote)
            return;

        if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
            player.world.setBlockToAir(player.getPosition());
        double x = player.posX + player.getRNG().nextGaussian() * 2;
        double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
        double z = player.posZ + player.getRNG().nextGaussian() * 2;

        player.world.newExplosion(player, x, y, z, 1, RegenConfig.REGENERATION.fieryRegen, false);
        for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east()))
            if (player.world.getBlockState(bs).getBlock() instanceof BlockFire)
                player.world.setBlockToAir(bs);
    }

    @SubscribeEvent
    public static void onRegenerationFinish(RegenerationFinishEvent event) {
        event.getEntityPlayer().extinguish();
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote)
            return;

        IRegenerationCapability handler = event.getHandler();
        player.setHealth(player.getMaxHealth());
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.REGENERATION.postRegenerationDuration * 2, RegenConfig.REGENERATION.postRegenerationLevel - 1, false, false)); // 180 seconds of 20 ticks of Regeneration 4

        handler.setRegensLeft(handler.getRegensLeft() - 1);
        handler.setTimesRegenerated(handler.getTimesRegenerated() + 1);

        handler.setTrait(TraitHandler.getRandomTrait());
        player.sendStatusMessage(new TextComponentTranslation(handler.getTrait().getMessage()), true);
        player.clearActivePotions();
        handler.syncToAll();
    }

    @SubscribeEvent
    public static void onRegenerationStart(RegenerationStartEvent event) {
        event.getEntityPlayer().extinguish();
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote)
            return;

        IRegenerationCapability handler = event.getHandler();
        player.setHealth(.5f);
        player.setAbsorptionAmount(RegenConfig.REGENERATION.absorbtionLevel * 2);
        if (RegenConfig.REGENERATION.resetOxygen)
            player.setAir(300);
        if (RegenConfig.REGENERATION.resetHunger)
            player.getFoodStats().setFoodLevel(20);
        player.clearActivePotions();
        player.addPotionEffect(new PotionEffect(Potion.getPotionById(10), 10 * 20, RegenConfig.REGENERATION.regenerationLevel - 1, false, false)); // 10 seconds of 20 ticks of Regeneration 2
        player.extinguish();

        String time = "" + (handler.getTimesRegenerated() + 1);
        int lastDigit = handler.getTimesRegenerated();
        if (lastDigit > 20)
            while (lastDigit > 10)
                lastDigit -= 10;

        if (lastDigit < 3)
            time = time + I18n.translateToLocalFormatted("lcm-regen.messages.numsuffix." + lastDigit);
        else
            time = time + I18n.translateToLocalFormatted("lcm-regen.messages.numsuffix.ext");

        player.sendStatusMessage(new TextComponentString(I18n.translateToLocalFormatted("lcm-regen.messages.regenLeftExt", time, (handler.getRegensLeft() - 1))), true);
        player.world.playSound(null, player.posX, player.posY, player.posZ, RObjects.SoundEvents.regeneration, SoundCategory.PLAYERS, 1.0F, 1.0F);
        RegenUtils.regenerationExplosion(player);
    }

    @SubscribeEvent
    public static void registerLoot(LootTableLoadEvent e) { // CHECK can this loot table actually be overriden in resource packs?
        if (!e.getName().toString().toLowerCase().matches(RegenConfig.LOOT.lootRegex) || RegenConfig.REGENERATION.disableArch)
            return;

        LootCondition[] condAlways = new LootCondition[]{new RandomChance(1F)};
        LootEntry entry = new LootEntryTable(new ResourceLocation(Regeneration.MODID, "inject/arch_loot"), 1, 1, condAlways, "lcm-regen:arch-entry");
        LootPool lootPool = new LootPool(new LootEntry[]{entry}, condAlways, new RandomValueRange(1), new RandomValueRange(1), "lcm-regen:arch-pool");
        e.getTable().addPool(lootPool);
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!RegenConfig.REGENERATION.startAsTimelord || e.player.world.isRemote)
            return;

        NBTTagCompound nbt = e.player.getEntityData();
        boolean loggedInBefore = nbt.getBoolean("loggedInBefore");
        if (!loggedInBefore) {
            e.player.inventory.addItemStackToInventory(new ItemStack(RObjects.Items.chameleonArch));
            nbt.setBoolean("loggedInBefore", true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onHurt(LivingHurtEvent e) {
        if (!(e.getEntity() instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) e.getEntity();
        if (player.getHealth() + player.getAbsorptionAmount() - e.getAmount() > 0 || !e.getEntity().hasCapability(CapabilityRegeneration.TIMELORD_CAP, null) || !e.getEntity().getCapability(CapabilityRegeneration.TIMELORD_CAP, null).isTimelord())
            return;

        IRegenerationCapability handler = e.getEntity().getCapability(CapabilityRegeneration.TIMELORD_CAP, null);

        if ((handler.getState() != CapabilityRegeneration.RegenerationState.NONE || player.posY < 0 || handler.getRegensLeft() <= 0) && !RegenConfig.REGENERATION.dontLoseUponDeath || player.isInLava()) {
            handler.setTimelord(false);
            handler.setRegensLeft(0);
            handler.setRegenTicks(0);
        } else {
            e.setCanceled(true);
            handler.setRegenTicks(1);
        }
    }

    //Capability Events

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer) || event.getObject().hasCapability(CapabilityRegeneration.TIMELORD_CAP, null))
            return;
        event.addCapability(new ResourceLocation(Regeneration.MODID, "timelord"), new CapabilityRegeneration.CapabilityTimelordProvider(new CapabilityRegeneration((EntityPlayer) event.getObject())));

    }

    @SubscribeEvent
    public static void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
        NBTTagCompound nbt = (NBTTagCompound) CapabilityRegeneration.TIMELORD_CAP.getStorage().writeNBT(CapabilityRegeneration.TIMELORD_CAP, event.getOriginal().getCapability(CapabilityRegeneration.TIMELORD_CAP, null), null);
        CapabilityRegeneration.TIMELORD_CAP.getStorage().readNBT(CapabilityRegeneration.TIMELORD_CAP, event.getEntityPlayer().getCapability(CapabilityRegeneration.TIMELORD_CAP, null), null, nbt);
    }
    
    @SubscribeEvent
    public static void playerTrackingEvent(net.minecraftforge.event.entity.player.PlayerEvent.StartTracking event) {
    	if(event.getEntityPlayer().getCapability(CapabilityRegeneration.TIMELORD_CAP, null) != null) {
    		event.getEntityPlayer().getCapability(CapabilityRegeneration.TIMELORD_CAP, null).syncToAll();
    	}
    }

}
