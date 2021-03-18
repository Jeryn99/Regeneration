package me.suff.mc.regen.mixin;

import me.suff.mc.regen.common.objects.REntities;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 17/03/2021 */
@Mixin(VillagerEntity.class)
public class VillagerTradeMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/merchant/villager/VillagerEntity;getVillagerData()Lnet/minecraft/entity/merchant/villager/VillagerData;"), method = "customServerAiStep()V", cancellable = true)
    protected void customServerAiStep(CallbackInfo ci) {
        VillagerEntity villagerEntity = (VillagerEntity) (Object) this;
        if(villagerEntity.getType() == REntities.TIMELORD.get()){
            ci.cancel();
        }
    }
}
