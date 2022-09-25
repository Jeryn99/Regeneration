package me.craig.software.regen.common.objects;

import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.util.RegenUtil;
import net.minecraft.util.ResourceLocation;

/* Created by Craig on 03/03/2021 */
public class RSoundSchemes {

    public static SoundScheme[] F_SCHEMES = null;
    public static SoundScheme[] M_SCHEMES = null;

    public static SoundScheme M_GAS = new SoundScheme(RSounds.M_GAS_TIMELORD_HURT.get(), RSounds.M_GAS_TIMELORD_TRADE_FAIL.get(), RSounds.M_GAS_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_GAS_TIMELORD_DIE.get(), RSounds.M_GAS_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "gasmaskva"));
    public static SoundScheme M_RAMEN = new SoundScheme(RSounds.M_RAMEN_TIMELORD_HURT.get(), RSounds.M_RAMEN_TIMELORD_TRADE_FAIL.get(), RSounds.M_RAMEN_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_RAMEN_TIMELORD_DIE.get(), RSounds.M_RAMEN_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "ramen"));
    public static SoundScheme M_DIDGERIDOOMEN = new SoundScheme(RSounds.M_DIDGERIDOOMEN_TIMELORD_HURT.get(), RSounds.M_DIDGERIDOOMEN_TIMELORD_TRADE_FAIL.get(), RSounds.M_DIDGERIDOOMEN_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_DIDGERIDOOMEN_TIMELORD_DIE.get(), RSounds.M_DIDGERIDOOMEN_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "didgeridoomen"));
    public static SoundScheme M_TOMMY = new SoundScheme(RSounds.M_TOMMY_TIMELORD_HURT.get(), RSounds.M_TOMMY_TIMELORD_TRADE_FAIL.get(), RSounds.M_TOMMY_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_TOMMY_TIMELORD_DIE.get(), RSounds.M_TOMMY_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "tommy"));
    public static SoundScheme M_CONNOR = new SoundScheme(RSounds.M_CONNOR_TIMELORD_HURT.get(), RSounds.M_CONNOR_TIMELORD_TRADE_FAIL.get(), RSounds.M_CONNOR_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_CONNOR_TIMELORD_DIE.get(), RSounds.M_CONNOR_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "connor"));
    public static SoundScheme M_WOD = new SoundScheme(RSounds.M_WODAMAN_TIMELORD_HURT.get(), RSounds.M_WODAMAN_TIMELORD_TRADE_FAIL.get(), RSounds.M_WODAMAN_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_WODAMAN_TIMELORD_HURT.get(), RSounds.M_WODAMAN_TIMELORD_HURT.get(), new ResourceLocation(RConstants.MODID, "wodaman"));
    public static SoundScheme F_AALICEH = new SoundScheme(RSounds.F_AALICEH_TIMELORD_HURT.get(), RSounds.F_AALICEH_TIMELORD_TRADE_FAIL.get(), RSounds.F_AALICEH_TIMELORD_TRADE_SUCCESS.get(), RSounds.F_AALICEH_TIMELORD_HURT.get(), RSounds.F_AALICEH_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "aaliceh"));
    public static SoundScheme M_DISASTER = new SoundScheme(RSounds.M_DISASTER_TIMELORD_HURT.get(), RSounds.M_DISASTER_TIMELORD_TRADE_FAIL.get(), RSounds.M_DISASTER_TIMELORD_TRADE_SUCCESS.get(), RSounds.M_DISASTER_TIMELORD_SCREAM.get(), RSounds.M_DISASTER_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "disaster"));
    public static SoundScheme F_ASHER = new SoundScheme(RSounds.F_ASHER_TIMELORD_HURT.get(), RSounds.F_ASHER_TIMELORD_TRADE_FAIL.get(), RSounds.F_ASHER_TIMELORD_TRADE_SUCCESS.get(), RSounds.F_ASHER_TIMELORD_DIE.get(), RSounds.F_ASHER_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "asher"));


    public static void init() {
        F_SCHEMES = new SoundScheme[]{RSoundSchemes.F_AALICEH, RSoundSchemes.F_ASHER};
        M_SCHEMES = new SoundScheme[]{RSoundSchemes.M_DISASTER, RSoundSchemes.M_GAS, M_RAMEN, M_TOMMY, M_CONNOR, M_WOD, M_DIDGERIDOOMEN};
    }


    public static SoundScheme getRandom(boolean isMale) {
        SoundScheme[] array = isMale ? M_SCHEMES : F_SCHEMES;
        if (array.length == 1) {
            return array[0];
        }
        return array[RegenUtil.RAND.nextInt(array.length)];
    }

    public static SoundScheme get(ResourceLocation resourceLocation, boolean male) {
        for (SoundScheme mScheme : M_SCHEMES) {
            if (mScheme.identify().equals(resourceLocation)) {
                return mScheme;
            }

        }

        for (SoundScheme fScheme : F_SCHEMES) {
            if (fScheme.identify().equals(resourceLocation)) {
                return fScheme;
            }
        }
        return male ? M_SCHEMES[0] : F_SCHEMES[0];
    }
}
