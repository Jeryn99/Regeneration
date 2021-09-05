package me.suff.mc.regen.mixin;

import me.suff.mc.regen.common.objects.REntities;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/* Created by Craig on 17/03/2021 */
@Mixin(Villager.class)
public class VillagerTradeMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/npc/Villager;getVillagerData()Lnet/minecraft/world/entity/npc/VillagerData;"), method = "customServerAiStep()V", cancellable = true)
    protected void customServerAiStep(CallbackInfo ci) {
        Villager villagerEntity = (Villager) (Object) this;
        if (villagerEntity.getType() == REntities.TIMELORD.get()) {
            ci.cancel();
        }
    }
}
