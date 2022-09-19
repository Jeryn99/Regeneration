package mc.craig.software.regen.common.traits.trait;

import mc.craig.software.regen.common.regen.IRegen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;

public class PhotosyntheticTrait extends TraitBase {
    @Override
    public int getPotionColor() {
        return Color.GREEN.darker().darker().getRGB();
    }

    @Override
    public void onAdded(LivingEntity livingEntity, IRegen data) {}

    @Override
    public void onRemoved(LivingEntity livingEntity, IRegen data) {

    }

    @Override
    public void tick(LivingEntity livingEntity, IRegen data) {
        if (livingEntity.level.isDay() && !livingEntity.level.isRaining()) {
            //if day, check if it's time to do photosynthetic ability
            if (livingEntity.tickCount % 200 == 0) {
                //if the player can see the sky
                if (livingEntity.level.canSeeSky(livingEntity.blockPosition())) {
                    if (livingEntity instanceof ServerPlayer serverPlayer) {
                        serverPlayer.getFoodData().eat(1, 0.25F);
                        return;
                    }
                    livingEntity.eat(livingEntity.level, new ItemStack(Items.COOKED_BEEF));
                }
            }
        }
    }
}
