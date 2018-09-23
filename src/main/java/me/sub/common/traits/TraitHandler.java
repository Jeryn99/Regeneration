package me.sub.common.traits;

import me.sub.common.traits.negative.TraitNone;
import me.sub.common.traits.positive.TraitHealthBoost;
import me.sub.common.traits.positive.TraitSpeed;
import me.sub.common.traits.positive.TraitStrong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Sub
 * on 23/09/2018.
 */
public class TraitHandler {

    public static Trait SPEED, HEALTH, NONE, STRENGTH;
    private static HashMap<String, Trait> TRAITS = new HashMap<>();
    private static ArrayList<Trait> traitList = new ArrayList<>();
    private static Random RAND = new Random();

    public static void init() {
        SPEED = registerTrait(new TraitSpeed());
        HEALTH = registerTrait(new TraitHealthBoost());
        NONE = registerTrait(new TraitNone());
        STRENGTH = registerTrait(new TraitStrong());
    }

    private static Trait registerTrait(Trait trait) {
        TRAITS.put(trait.getName(), trait);
        traitList.add(trait);
        return trait;
    }

    public static Trait getRandomTrait() {
        Trait trait = traitList.get(RAND.nextInt(traitList.size()));
        return trait;
    }

    public static Trait getTraitByName(String name) {
        if (TRAITS.containsKey(name)) {
            Trait trait = TRAITS.get(name);
            return trait;
        }
        return NONE;
    }

}
