package mc.craig.software.regen.util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayerUtil {

    // Send a client message to the player
    public static void sendMessage(Player player, Component component, boolean isHorBar){
        player.displayClientMessage(component, isHorBar);
    }

    public static void sendMessage(Player player, Component component){
        player.sendSystemMessage(component);
    }

    public static void applyPotionIfAbsent(LivingEntity player, MobEffect potion, int length, int amplifier, boolean ambient, boolean showParticles) {
        if (potion == null) return;
        if (player.getEffect(potion) == null) {
            player.addEffect(new MobEffectInstance(potion, length, amplifier, ambient, showParticles));
        }
    }


    public static void explodeKnockback(LivingEntity player, Level level, BlockPos blockPos, double v, Integer integer) {
    }
}
