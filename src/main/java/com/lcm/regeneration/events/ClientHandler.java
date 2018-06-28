package com.lcm.regeneration.events;

import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationEvent;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationFinishEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Nictogen on 3/16/18.
 */

public class ClientHandler {

    @SubscribeEvent
    public void onRegeneration(RegenerationEvent event) {
        if (!event.getEntityPlayer().world.isRemote) return;
        if (Minecraft.getMinecraft().player.getUniqueID() == event.getEntityPlayer().getUniqueID())
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
        event.getHandler().setRegenTicks(event.getHandler().getRegenTicks() + 1);
    }

    @SubscribeEvent
    public void onRegenerationFinish(RegenerationFinishEvent event) {
        if (!event.getEntityPlayer().world.isRemote) return;
        if (Minecraft.getMinecraft().player.getUniqueID() == event.getEntityPlayer().getUniqueID())
            Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
        event.getHandler().setRegenTicks(0);
    }

    @SubscribeEvent
    public void onAttacked(LivingAttackEvent e) {
        if (!e.getEntity().world.isRemote) return;
        if (!e.getEntity().world.isRemote || !(e.getEntity() instanceof EntityPlayer) || !e.getEntity().hasCapability(CapabilityRegeneration.TIMELORD_CAP, null) || !e.getEntity().getCapability(CapabilityRegeneration.TIMELORD_CAP, null).isTimelord())
            return;
        EntityPlayer player = (EntityPlayer) e.getEntity();
        if (player.getHealth() - e.getAmount() < 0 && e.getEntity().getCapability(CapabilityRegeneration.TIMELORD_CAP, null).getState() != CapabilityRegeneration.RegenerationState.NONE)
            MinecraftForge.EVENT_BUS.post(new RegenerationFinishEvent(player, player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null)));
    }

    @SubscribeEvent
    public void keyInput(InputUpdateEvent e) {
        if (Minecraft.getMinecraft().player == null || !Minecraft.getMinecraft().player.hasCapability(CapabilityRegeneration.TIMELORD_CAP, null) || !Minecraft.getMinecraft().player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null).isTimelord())
            return;

        IRegenerationCapability capability = Minecraft.getMinecraft().player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);

        if (capability.getState() != CapabilityRegeneration.RegenerationState.NONE) {
            MovementInput moveType = e.getMovementInput();
            moveType.rightKeyDown = false;
            moveType.leftKeyDown = false;
            moveType.backKeyDown = false;
            moveType.jump = false;
            moveType.moveForward = 0.0F;
            moveType.sneak = false;
            moveType.moveStrafe = 0.0F;
        }
    }
}
