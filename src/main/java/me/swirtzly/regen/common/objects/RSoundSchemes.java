package me.swirtzly.regen.common.objects;

import me.swirtzly.regen.Regeneration;
import me.swirtzly.regen.util.RConstants;
import me.swirtzly.regen.util.RegenUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/* Created by Craig on 03/03/2021 */
public class RSoundSchemes {

    public static SoundScheme[] F_SCHEMES =null;
    public static SoundScheme[] M_SCHEMES = null;

    public static void init(){
        F_SCHEMES =  new SoundScheme[]{RSoundSchemes.F_ANGELA};
        M_SCHEMES =  new SoundScheme[]{RSoundSchemes.M_GAS, M_ALVIN};
    }

    public static SoundScheme getRandom(boolean isMale){
        SoundScheme[] array = isMale ? M_SCHEMES : F_SCHEMES;
        if(array.length == 1){
            return array[0];
        }
        SoundScheme result = array[RegenUtil.RAND.nextInt(array.length)];
        return result;
    }

    public static SoundScheme get(ResourceLocation resourceLocation){
        for (SoundScheme mScheme : M_SCHEMES) {
            if(mScheme.identify().equals(resourceLocation)){
                return mScheme;
            }
        }

        for (SoundScheme fScheme : F_SCHEMES) {
            if(fScheme.identify().equals(resourceLocation)){
                return fScheme;
            }
        }
        return F_SCHEMES[0];
    }

    public static SoundScheme F_ANGELA = new SoundScheme() {
        @Override
        public SoundEvent getHurtSound() {
            return RSounds.F_ANGELA_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getTradeDeclineSound() {
            return RSounds.F_ANGELA_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getTradeAcceptSound() {
            return RSounds.F_ANGELA_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getDeathSound() {
            return null;
        }

        @Override
        public SoundEvent getScreamSound() {
            return null;
        }

        @Override
        public ResourceLocation identify() {
            return new ResourceLocation(RConstants.MODID, "angela");
        }
    };

    public static SoundScheme M_GAS = new SoundScheme() {
        @Override
        public SoundEvent getHurtSound() {
            return RSounds.M_GAS_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getTradeDeclineSound() {
            return RSounds.M_GAS_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getTradeAcceptSound() {
            return RSounds.M_GAS_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getDeathSound() {
            return RSounds.M_GAS_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getScreamSound() {
            return null;
        }

        @Override
        public ResourceLocation identify() {
            return new ResourceLocation(RConstants.MODID, "gasmask");
        }
    };

    public static SoundScheme M_ALVIN = new SoundScheme() {
        @Override
        public SoundEvent getHurtSound() {
            return RSounds.M_ALVIN_TIMELORD_HURT.get();
        }

        @Override
        public SoundEvent getTradeDeclineSound() {
            return RSounds.M_ALVIN_TIMELORD_TRADE_FAIL.get();
        }

        @Override
        public SoundEvent getTradeAcceptSound() {
            return RSounds.M_ALVIN_TIMELORD_TRADE_SUCCESS.get();
        }

        @Override
        public SoundEvent getDeathSound() {
            return RSounds.M_ALVIN_TIMELORD_DIE.get();
        }

        @Override
        public SoundEvent getScreamSound() {
            return RSounds.M_ALVIN_TIMELORD_SCREAM.get();
        }

        @Override
        public ResourceLocation identify() {
            return new ResourceLocation(RConstants.MODID, "alvin");
        }
    };
}
