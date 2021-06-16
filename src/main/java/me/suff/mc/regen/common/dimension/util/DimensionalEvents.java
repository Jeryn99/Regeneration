package me.suff.mc.regen.common.dimension.util;

import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Craig
 * on 01/05/2020 @ 11:38
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DimensionalEvents {

    @SubscribeEvent
    public static void onLive(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        RegenCap.get(living).ifPresent((data -> {

            //Stuff to do if the player can't regenerate
            if (!data.canRegenerate()) {
                if (!living.level.isClientSide) {
                    if (living.level.getBiome(living.getCommandSenderBlockPosition()) == RegenObjects.GallifreyBiomes.WASTELANDS.get() && living.isInWater()) {
                        PlayerUtil.applyPotionIfAbsent(living, Effects.CONFUSION, 200, 1, false, false);
                        PlayerUtil.applyPotionIfAbsent(living, Effects.POISON, 100, 1, false, false);
                    }
                }
            }
        }));
    }
}
