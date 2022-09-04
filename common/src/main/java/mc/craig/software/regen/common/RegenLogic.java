package mc.craig.software.regen.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RegenLogic {

    public static boolean isSafeToRegenHere(LivingEntity livingEntity) {
        Level level = livingEntity.level;
        return !level.isOutsideBuildHeight(livingEntity.blockPosition()) && livingEntity.blockPosition().getY() > level.getMinBuildHeight();
    }

}
