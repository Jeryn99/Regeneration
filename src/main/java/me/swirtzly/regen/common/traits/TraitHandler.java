package me.swirtzly.regen.common.traits;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.util.RConstants;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RConstants.MODID)
public class TraitHandler {

    @SubscribeEvent
    public static void onExperienceGain(PlayerXpEvent.PickupXp event) {
        RegenCap.get(event.getPlayer()).ifPresent(iRegen -> {
            if (iRegen.getTrait().getRegistryName().toString().equals(Traits.SMART.get().getRegistryName().toString())) {
                event.getOrb().xpValue *= 1.5;
            }
        });
    }

    @SubscribeEvent
    public static void onMineBlock(PlayerEvent.BreakSpeed event){
        RegenCap.get(event.getPlayer()).ifPresent(iRegen -> {
            if(iRegen.getTrait().getRegistryName().toString().equals(Traits.FAST_MINE.get().getRegistryName().toString())){
                event.setNewSpeed(event.getOriginalSpeed() * 5);
            }
        });
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event){
        RegenCap.get(event.getEntityLiving()).ifPresent(iRegen -> {
            if (iRegen.getTrait().getRegistryName().toString().equals(Traits.KNOCKBACK.get().getRegistryName().toString())) {
                event.setCanceled(true);
            }
        });
    }
}
