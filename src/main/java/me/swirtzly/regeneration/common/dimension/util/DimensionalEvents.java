package me.swirtzly.regeneration.common.dimension.util;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Created by Swirtzly
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
                if (!living.world.isRemote) {
                    if (living.world.getBiome(living.getPosition()) == RegenObjects.Biomes.WASTELANDS.get() && living.isInWater()) {
                        PlayerUtil.applyPotionIfAbsent((PlayerEntity) living, Effects.NAUSEA, 200, 1, false, false);
                        PlayerUtil.applyPotionIfAbsent((PlayerEntity) living, Effects.POISON, 100, 1, false, false);
                    }
                }
            }
        }));
    }
}
