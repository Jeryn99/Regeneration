package com.lcm.regeneration.events;

import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityRegeneration;
import com.lcm.regeneration.common.capabilities.timelord.capability.IRegenerationCapability;
import com.lcm.regeneration.common.capabilities.timelord.events.RegenerationFinishEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;


@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void onAttacked(LivingAttackEvent e) {
        if (!e.getEntity().world.isRemote) return;
        if (!e.getEntity().world.isRemote || !(e.getEntity() instanceof EntityPlayer) || !e.getEntity().hasCapability(CapabilityRegeneration.TIMELORD_CAP, null) || !e.getEntity().getCapability(CapabilityRegeneration.TIMELORD_CAP, null).isTimelord())
            return;
        EntityPlayer player = (EntityPlayer) e.getEntity();
        if (player.getHealth() - e.getAmount() < 0 && e.getEntity().getCapability(CapabilityRegeneration.TIMELORD_CAP, null).getState() != CapabilityRegeneration.RegenerationState.NONE)
            MinecraftForge.EVENT_BUS.post(new RegenerationFinishEvent(player, player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null)));
    }

    @SubscribeEvent
    public static void keyInput(InputUpdateEvent e) {
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
