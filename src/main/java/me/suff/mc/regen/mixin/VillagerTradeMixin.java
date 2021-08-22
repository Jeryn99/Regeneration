package me.suff.mc.regen.mixin;

/* Created by Craig on 17/03/2021 */
//@Mixin(Villager.class)
public class VillagerTradeMixin {

  /*  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/merchant/villager/VillagerEntity;getVillagerData()Lnet/minecraft/entity/merchant/villager/VillagerData;"), method = "customServerAiStep()V", cancellable = true)
    protected void customServerAiStep(CallbackInfo ci) {
        Villager villagerEntity = (Villager) (Object) this;
        if (villagerEntity.getType() == REntities.TIMELORD.get()) {
            ci.cancel();
        }
    }*/
}
