package me.sub.common.traits;

import me.sub.Regeneration;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import me.sub.common.traits.negative.TraitNone;
import me.sub.common.traits.negative.TraitSentimental;
import me.sub.common.traits.positive.TraitHealthBoost;
import me.sub.common.traits.positive.TraitSpeed;
import me.sub.common.traits.positive.TraitStrong;
import me.sub.util.PlayerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Sub
 * on 23/09/2018.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class TraitHandler {

    public static Trait SPEED, HEALTH, NONE, STRENGTH, SENTIMENTAL;
    private static HashMap<String, Trait> TRAITS = new HashMap<>();
    private static ArrayList<Trait> traitList = new ArrayList<>();
    private static Random RAND = new Random();

    public static void init() {
        SPEED = registerTrait(new TraitSpeed());
        HEALTH = registerTrait(new TraitHealthBoost());
        NONE = registerTrait(new TraitNone());
        STRENGTH = registerTrait(new TraitStrong());
        SENTIMENTAL = registerTrait(new TraitSentimental());
    }

    private static Trait registerTrait(Trait trait) {
        TRAITS.put(trait.getName(), trait);
        traitList.add(trait);
        return trait;
    }

    public static Trait getRandomTrait() {
        return traitList.get(RAND.nextInt(traitList.size()));
    }

    public static Trait getTraitByName(String name) {
        if (TRAITS.containsKey(name)) {
            return TRAITS.get(name);
        }
        return NONE;
    }

    //===========================================================\\

    @SubscribeEvent
    public static void onDeath(LivingHurtEvent e) {
        if (!(e.getSource().getTrueSource() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) e.getSource().getTrueSource();
        IRegeneration regenInfo = CapabilityRegeneration.get(player);
        Entity deadEntity = e.getEntity();

        if (regenInfo.getTrait() != null && regenInfo.getTrait() == SENTIMENTAL) {
            if (!PlayerUtil.canEntityAttack(deadEntity)) {
                player.attackEntityFrom(DamageSource.GENERIC, 1F);
            }
        }

    }

}
