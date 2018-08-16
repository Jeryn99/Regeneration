package me.sub.regeneration.events;

import me.sub.regeneration.Regeneration;
import me.sub.regeneration.client.render.entity.layers.LayerItemsAlt;
import me.sub.regeneration.client.render.entity.layers.LayerRegeneration;
import me.sub.regeneration.common.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capability.IRegenerationCapability;
import me.sub.regeneration.common.events.RegenerationFinishEvent;
import me.sub.regeneration.utils.LimbManipulationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;


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
            e.getRenderer().addLayer(new LayerItemsAlt(e.getRenderer()));
        }
    }
	
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


    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre e) {

        RenderPlayer renderer = e.getRenderer();

        IRegenerationCapability handler = e.getEntityPlayer().getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
        if (handler != null && handler.isTimelord() && handler.getState() != CapabilityRegeneration.RegenerationState.NONE) {
            int arm_shake = e.getEntityPlayer().world.rand.nextInt(7);

            LimbManipulationUtil.getLimbManipulator(renderer, LimbManipulationUtil.Limb.LEFT_ARM).setAngles(0, 0, -75 + arm_shake);
            LimbManipulationUtil.getLimbManipulator(renderer, LimbManipulationUtil.Limb.RIGHT_ARM).setAngles(0, 0, 75 + arm_shake);
            LimbManipulationUtil.getLimbManipulator(renderer, LimbManipulationUtil.Limb.HEAD).setAngles(-50, 0, 0);
            LimbManipulationUtil.getLimbManipulator(renderer, LimbManipulationUtil.Limb.LEFT_LEG).setAngles(0, 0, -10);
            LimbManipulationUtil.getLimbManipulator(renderer, LimbManipulationUtil.Limb.RIGHT_LEG).setAngles(0, 0, 10);

            List<LayerRenderer> list = ReflectionHelper.getPrivateValue(RenderLivingBase.class, renderer, "layerRenderers", "field_177097_h");

            for (LayerRenderer layer : list)
                if (layer instanceof LayerBipedArmor) {
                    LayerBipedArmor layerArmorBase = (LayerBipedArmor) layer;
                    ModelBiped modelBipedLegs = ReflectionHelper.getPrivateValue(LayerArmorBase.class, layerArmorBase, 1);
                    ModelBiped modelBiped = ReflectionHelper.getPrivateValue(LayerArmorBase.class, layerArmorBase, 2);
                    animateArmor(modelBiped, modelBipedLegs, arm_shake);
                }
        }
    }

    private static void animateArmor(ModelBiped modelBiped, ModelBiped legs, int arm_shake) {
        LimbManipulationUtil.getLimbManipulator(modelBiped, LimbManipulationUtil.BipedLimb.LEFT_ARM).setAngles(0, 0, -75 + arm_shake);
        LimbManipulationUtil.getLimbManipulator(modelBiped, LimbManipulationUtil.BipedLimb.RIGHT_ARM).setAngles(0, 0, 75 + arm_shake);
        LimbManipulationUtil.getLimbManipulator(modelBiped, LimbManipulationUtil.BipedLimb.HEAD).setAngles(-50, 0, 0);
        LimbManipulationUtil.getLimbManipulator(legs, LimbManipulationUtil.BipedLimb.LEFT_LEG).setAngles(0, 0, -10);
        LimbManipulationUtil.getLimbManipulator(legs, LimbManipulationUtil.BipedLimb.RIGHT_LEG).setAngles(0, 0, 10);
    }

}
