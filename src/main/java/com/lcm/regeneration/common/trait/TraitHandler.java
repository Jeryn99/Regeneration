package com.lcm.regeneration.common.trait;

import com.lcm.regeneration.common.capabilities.timelord.capability.CapabilityTimelord;
import com.lcm.regeneration.common.capabilities.timelord.capability.ITimelordCapability;
import com.lcm.regeneration.common.trait.traits.TraitClumsy;
import com.lcm.regeneration.common.trait.traits.TraitNone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

@Mod.EventBusSubscriber
public class TraitHandler {

    @SubscribeEvent
    public static void playerUpdate(TickEvent.PlayerTickEvent e) {
        EntityPlayer player = e.player;
        ITimelordCapability capa = player.getCapability(CapabilityTimelord.TIMELORD_CAP, null);
        capa.getTrait().update(player);
    }

    public static Trait getRandomTrait() {
        int pick = new Random().nextInt(Trait.values().length);
        return Trait.values()[pick];
    }

    public enum Trait {

        NONE(new TraitNone()),
        CLUMSY(new TraitClumsy());

        ITrait trait;

        Trait(ITrait trait) {
            this.trait = trait;
        }

        public ITrait getTrait() {
            return trait;
        }
    }


}
