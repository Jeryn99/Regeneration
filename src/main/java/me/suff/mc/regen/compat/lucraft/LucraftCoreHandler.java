package me.suff.mc.regen.compat.lucraft;

import lucraft.mods.lucraftcore.materials.potions.PotionRadiation;
import lucraft.mods.lucraftcore.superpowers.SuperpowerHandler;
import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarHandler;
import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarKeys;
import lucraft.mods.lucraftcore.util.events.RenderModelEvent;
import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.client.animation.AnimationContext;
import me.suff.mc.regen.client.animation.AnimationHandler;
import me.suff.mc.regen.handlers.IActingHandler;
import me.suff.mc.regen.util.ClientUtil;
import me.suff.mc.regen.common.capability.CapabilityRegeneration;
import me.suff.mc.regen.common.capability.IRegeneration;
import me.suff.mc.regen.common.types.IRegenType;
import me.suff.mc.regen.common.types.TypeHandler;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static me.suff.mc.regen.util.PlayerUtil.RegenState.REGENERATING;

public class LucraftCoreHandler implements IActingHandler {

    public static void registerEntry() {
        AbilityBarHandler.registerProvider(new LCCoreBarEntry());
    }

    public static void registerEventBus() {
        MinecraftForge.EVENT_BUS.register(new LucraftCoreHandler());
    }

    public static String getKeyBindDisplayName() {
        for (int i = 0; i < AbilityBarHandler.ENTRY_SHOW_AMOUNT; i++) {
            if (AbilityBarHandler.getEntryFromKey(i) instanceof LCCoreBarEntry) {
                return AbilityBarKeys.KEYS.get(i).getDisplayName();
            }
        }
        return "???";
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onInput(InputUpdateEvent tickEvent) {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.currentScreen == null && minecraft.player != null) {
            ClientUtil.keyBind = getKeyBindDisplayName();
        }
    }

    @Override
    public void onRegenTick(IRegeneration cap) {

    }

    @Override
    public void onEnterGrace(IRegeneration cap) {

    }

    @Override
    public void onHandsStartGlowing(IRegeneration cap) {

    }

    @Override
    public void onRegenFinish(IRegeneration cap) {

    }

    @Override
    public void onStartPost(IRegeneration cap) {

    }

    @Override
    public void onProcessDone(IRegeneration cap) {

    }

    @Override
    public void onRegenTrigger(IRegeneration cap) {
    }

    @Override
    public void onGoCritical(IRegeneration cap) {

    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            IRegeneration data = CapabilityRegeneration.getForPlayer(player);
            boolean flag = data.canRegenerate() && e.getSource() == PotionRadiation.RADIATION && RegenConfig.modIntegrations.lucraftcore.immuneToRadiation;
            e.setCanceled(flag);
        }
    }

    @SubscribeEvent
    public void onCanRegen(PlayerCanRegenEvent e) {
        boolean flag = RegenConfig.modIntegrations.lucraftcore.superpowerDisable && SuperpowerHandler.hasSuperpower(e.getEntityPlayer());
        if (flag) {
            e.setCanceled(true);
            PlayerUtil.sendMessage(e.getEntityPlayer(), "You cannot Regenerate with a superpower", true);
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    @SideOnly(Side.CLIENT)
    public void onAnimation(RenderModelEvent.SetRotationAngels ev) {
        if (ev.getEntity() instanceof EntityPlayer) {
            AnimationContext context = new AnimationContext(ev.model, (EntityPlayer) ev.getEntity(), ev.limbSwing, ev.limbSwingAmount, ev.ageInTicks, ev.netHeadYaw, ev.headPitch);
            IRegeneration data = CapabilityRegeneration.getForPlayer((EntityPlayer) ev.getEntity());
            if (data.getState() == REGENERATING) {
                IRegenType type = TypeHandler.getTypeInstance(data.getType());
                ev.setCanceled(type.getRenderer().onAnimateRegen(context));
            } else {
                AnimationHandler.animate(context);
            }
        }
    }
}
