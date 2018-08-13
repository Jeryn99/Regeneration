package me.sub.regeneration.common.trait;

import java.util.Random;

import me.sub.regeneration.common.capability.CapabilityRegeneration;
import me.sub.regeneration.common.capability.IRegenerationCapability;
import me.sub.regeneration.common.trait.traits.TraitClumsy;
import me.sub.regeneration.common.trait.traits.TraitHunger;
import me.sub.regeneration.common.trait.traits.TraitNightVision;
import me.sub.regeneration.common.trait.traits.TraitNone;
import me.sub.regeneration.common.trait.traits.TraitSaturation;
import me.sub.regeneration.common.trait.traits.TraitSpeed;
import me.sub.regeneration.common.trait.traits.TraitStrength;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class TraitHandler {

    @SubscribeEvent
    public static void playerUpdate(TickEvent.PlayerTickEvent e) {
        EntityPlayer player = e.player;
        IRegenerationCapability capa = player.getCapability(CapabilityRegeneration.TIMELORD_CAP, null);
        capa.getTrait().update(player);
    }

    public static Trait getRandomTrait() {
        int pick = new Random().nextInt(Trait.values().length);
        return Trait.values()[pick];
    }

    public enum Trait {

        NONE(new TraitNone()),
        CLUMSY(new TraitClumsy()),
        SPEED(new TraitSpeed()),
        HUNGER(new TraitHunger()),
        STRENGTH(new TraitStrength()),
        SATURATION(new TraitSaturation()),
        NIGHT_VISION(new TraitNightVision());

        private ITrait trait;

        Trait(ITrait trait) {
            this.trait = trait;
        }

        public ITrait getTrait() {
            return trait;
        }
    }


}
