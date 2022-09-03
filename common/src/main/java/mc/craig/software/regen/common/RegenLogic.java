package mc.craig.software.regen.common;

import mc.craig.software.regen.common.regeneration.RegenerationData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class RegenLogic {

    public static boolean canRegenerate(LivingEntity livingEntity) {


        if (livingEntity instanceof Player player) {
            Level level = livingEntity.level;
            boolean isSafe = !level.isOutsideBuildHeight(livingEntity.blockPosition()) && livingEntity.blockPosition().getY() > level.getMinBuildHeight();
            RegenerationData data = RegenerationData.get(player).orElse(null);
            return isSafe && data.getRemainingRegens() > 0;
        }
        return false;
    }

}
