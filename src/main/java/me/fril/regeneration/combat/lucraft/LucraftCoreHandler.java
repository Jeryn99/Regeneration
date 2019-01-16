package me.fril.regeneration.combat.lucraft;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.lucraftcore.util.abilitybar.AbilityBarHandler;
import me.fril.regeneration.api.IActingHandler;
import me.fril.regeneration.common.capability.IRegeneration;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Random;

public class LucraftCoreHandler implements IActingHandler {

    public static void registerEntry() {
        AbilityBarHandler.registerProvider(new LCCoreBarEntry());
    }


    @Override
    public void onRegenTick(IRegeneration cap) {

    }

    @Override
    public void onEnterGrace(IRegeneration cap) {

    }

    @Override
    public void onRegenFinish(IRegeneration cap) {

    }

    public static float randFloat(float min, float max) {
        Random rand = new Random();
        float result = rand.nextFloat() * (max - min) + min;
        return result;
    }

    @Override
    public void onRegenTrigger(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        ISizeChanging sizeCap = player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null);
        sizeCap.setSize(randFloat(0.796544F, 1F));
    }

    @Override
    public void onGoCritical(IRegeneration cap) {

    }
}
