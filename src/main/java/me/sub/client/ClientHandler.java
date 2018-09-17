package me.sub.client;

import me.sub.Regeneration;
import me.sub.client.layers.LayerRegeneration;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import me.sub.common.init.RObjects;
import me.sub.common.states.EnumRegenType;
import me.sub.network.NetworkHandler;
import me.sub.network.packets.MessageNoDiePls;
import me.sub.util.LimbManipulationUtil;
import me.sub.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Regeneration.MODID)
public class ClientHandler {

    private static ArrayList<EntityPlayer> layersAddedTo = new ArrayList<>();
    private static World lastWorld;

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post e) {

        EntityPlayer player = e.getEntityPlayer();
        if (lastWorld != player.world) {
            lastWorld = player.world;
            layersAddedTo.clear();
        }
        if (!layersAddedTo.contains(player)) {
            layersAddedTo.add(player);
            e.getRenderer().addLayer(new LayerRegeneration(e.getRenderer()));
            //e.getRenderer().addLayer(new LayerItemsAlt(e.getRenderer()));
        }

    }

    @SubscribeEvent
    public static void onUpdate(LivingEvent.LivingUpdateEvent e) {

        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            IRegeneration regeneration = CapabilityRegeneration.get(player);
            if (regeneration != null && player != null && Minecraft.getMinecraft().player != null) {
                if (regeneration.isRegenerating() && !regeneration.isInGracePeriod() && regeneration.getSolaceTicks() >= 200 && Minecraft.getMinecraft().player.getEntityId() == player.getEntityId()) {
                    Minecraft.getMinecraft().gameSettings.thirdPersonView = 2;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent e) {
        if (Minecraft.getMinecraft().world != null) {
            if (RKeyBinds.GRACE.isPressed()) {
                EntityPlayer player = Minecraft.getMinecraft().player;
                if (player != null) {
                    NetworkHandler.INSTANCE.sendToServer(new MessageNoDiePls(player));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre e) {

        IRegeneration handler = CapabilityRegeneration.get(e.getEntityPlayer());

        if (handler != null && handler.isRegenerating() && handler.getSolaceTicks() >= 190 && !handler.isInGracePeriod()) {

            //Fiery Regen T-Posing
            if (handler.getType().equals(EnumRegenType.FIERY)) {
                int arm_shake = e.getEntityPlayer().getRNG().nextInt(7);

                LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75 + arm_shake);
                LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75 + arm_shake);
                LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.HEAD).setAngles(-50, 0, 0);
                LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.LEFT_LEG).setAngles(0, 0, -10);
                LimbManipulationUtil.getLimbManipulator(e.getRenderer(), LimbManipulationUtil.Limb.RIGHT_LEG).setAngles(0, 0, 10);
            }
        }
    }

    @SubscribeEvent
    public static void keyInput(InputUpdateEvent e) {
        if (Minecraft.getMinecraft().player == null || !Minecraft.getMinecraft().player.hasCapability(CapabilityRegeneration.CAPABILITY, null) || !CapabilityRegeneration.get(Minecraft.getMinecraft().player).isCapable())
            return;

        IRegeneration capability = CapabilityRegeneration.get(Minecraft.getMinecraft().player);

        if (capability.isRegenerating() && !capability.isInGracePeriod() && capability.getType().getType().blockMovement() && capability.getSolaceTicks() >= 200) {
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


    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent ev) {
        for (Item item : RObjects.ITEMS) {
            RenderUtil.setItemRender(item);
        }
        RObjects.ITEMS = new ArrayList<>();
    }

}
