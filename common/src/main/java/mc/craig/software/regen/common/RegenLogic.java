package mc.craig.software.regen.common;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class RegenLogic {

    public static boolean canRegenerate(LivingEntity livingEntity) {
        Level level = livingEntity.level;
        boolean isSafe = !level.isOutsideBuildHeight(livingEntity.blockPosition()) && livingEntity.blockPosition().getY() > level.getMinBuildHeight();
        System.out.println("Safe: " + isSafe);
        System.out.println("has Cookie: " + (livingEntity.getMainHandItem().getItem() == Items.COOKIE));
        return isSafe && livingEntity.getMainHandItem().getItem() == Items.COOKIE;
    }

}
