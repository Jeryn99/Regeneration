package me.swirtzly.regen.common.traits;

import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.util.RConstants;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RConstants.MODID)
public class TraitHandler {

    @SubscribeEvent
    public static void onExperienceGain(PlayerXpEvent.PickupXp event){
        RegenCap.get(event.getPlayer()).ifPresent(iRegen -> {
            if(iRegen.getTrait() == Traits.SMART.get()){
                event.getOrb().xpValue *= 1.5;
            }
        });
    }

}
