package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenUtil;
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
    public static SoundScheme F_ANGELA = new SoundScheme(RSounds.F_ANGELA_TIMELORD_HURT.get(), RSounds.F_ANGELA_TIMELORD_TRADE_FAIL.get(), RSounds.F_ANGELA_TIMELORD_TRADE_SUCCESS.get(), RSounds.F_ANGELA_TIMELORD_DIE.get(), RSounds.F_ANGELA_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "angela"));
    public static SoundScheme F_ASHER = new SoundScheme(RSounds.F_ANGELA_TIMELORD_HURT.get(), RSounds.F_ASHER_TIMELORD_TRADE_FAIL.get(), RSounds.F_ASHER_TIMELORD_TRADE_SUCCESS.get(), RSounds.F_ANGELA_TIMELORD_DIE.get(), RSounds.F_ANGELA_TIMELORD_SCREAM.get(), new ResourceLocation(RConstants.MODID, "asher"));


    public static void init() {
        F_SCHEMES = new SoundScheme[]{RSoundSchemes.F_ANGELA, RSoundSchemes.F_AALICEH, RSoundSchemes.F_ASHER};
        M_SCHEMES = new SoundScheme[]{RSoundSchemes.M_GAS, M_RAMEN, M_TOMMY, M_CONNOR, M_WOD, M_DIDGERIDOOMEN};
    }


    public static SoundScheme getRandom(boolean isMale) {
        SoundScheme[] array = isMale ? M_SCHEMES : F_SCHEMES;
        if (array.length == 1) {
            return array[0];
        }
        SoundScheme result = array[RegenUtil.RAND.nextInt(array.length)];
        return result;
    }

    public static SoundScheme get(ResourceLocation resourceLocation) {
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
        return F_SCHEMES[0];
    }
}
