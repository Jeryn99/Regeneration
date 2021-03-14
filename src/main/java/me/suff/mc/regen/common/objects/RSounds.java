package me.suff.mc.regen.common.objects;

import me.suff.mc.regen.util.RConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RSounds {
    public static final DeferredRegister< SoundEvent > SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RConstants.MODID);

    public static final RegistryObject< SoundEvent > FOB_WATCH = SOUNDS.register("fob_watch", () -> setUpSound("fob_watch"));
    public static final RegistryObject< SoundEvent > CRITICAL_STAGE = SOUNDS.register("critical_stage", () -> setUpSound("critical_stage"));
    public static final RegistryObject< SoundEvent > HEART_BEAT = SOUNDS.register("heart_beat", () -> setUpSound("heart_beat"));
    public static final RegistryObject< SoundEvent > HAND_GLOW = SOUNDS.register("hand_glow", () -> setUpSound("hand_glow"));
    public static final RegistryObject< SoundEvent > GRACE_HUM = SOUNDS.register("grace_hum", () -> setUpSound("grace_hum"));
    public static final RegistryObject< SoundEvent > ALARM = SOUNDS.register("alarm", () -> setUpSound("alarm"));
    public static final RegistryObject< SoundEvent > JAR_BUBBLES = SOUNDS.register("jar_bubbles", () -> setUpSound("jar_bubbles"));

    public static final RegistryObject< SoundEvent > REGENERATION_0 = SOUNDS.register("regeneration_0", () -> setUpSound("regeneration_0"));
    public static final RegistryObject< SoundEvent > REGENERATION_1 = SOUNDS.register("regeneration_1", () -> setUpSound("regeneration_1"));
    public static final RegistryObject< SoundEvent > REGENERATION_2 = SOUNDS.register("regeneration_2", () -> setUpSound("regeneration_2"));
    public static final RegistryObject< SoundEvent > REGENERATION_3 = SOUNDS.register("regeneration_3", () -> setUpSound("regeneration_3"));
    public static final RegistryObject< SoundEvent > REGENERATION_4 = SOUNDS.register("regeneration_4", () -> setUpSound("regeneration_4"));
    public static final RegistryObject< SoundEvent > REGENERATION_5 = SOUNDS.register("regeneration_5", () -> setUpSound("regeneration_5"));
    public static final RegistryObject< SoundEvent > REGENERATION_6 = SOUNDS.register("regeneration_6", () -> setUpSound("regeneration_6"));
    public static final RegistryObject< SoundEvent > REGENERATION_7 = SOUNDS.register("regeneration_7", () -> setUpSound("regeneration_7"));
    public static final RegistryObject< SoundEvent > REGENERATION_TROUGHTON = SOUNDS.register("regeneration_troughton", () -> setUpSound("regeneration_troughton"));
    public static final RegistryObject< SoundEvent > REGENERATION_WATCHER = SOUNDS.register("regeneration_watcher", () -> setUpSound("regeneration_watcher"));
    public static final RegistryObject< SoundEvent > DRUM_BEAT = SOUNDS.register("drum_beat", () -> setUpSound("drum_beat"));

    //Asher Timelord
    public static final RegistryObject< SoundEvent > F_ASHER_TIMELORD_HURT = SOUNDS.register("f_asher_timelord_hurt", () -> setUpSound("f_asher_timelord_hurt"));
    public static final RegistryObject< SoundEvent > F_ASHER_TIMELORD_DIE = SOUNDS.register("f_asher_timelord_die", () -> setUpSound("f_asher_timelord_die"));
    public static final RegistryObject< SoundEvent > F_ASHER_TIMELORD_TRADE_FAIL = SOUNDS.register("f_asher_timelord_trade_fail", () -> setUpSound("f_asher_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > F_ASHER_TIMELORD_TRADE_SUCCESS = SOUNDS.register("f_asher_timelord_trade_success", () -> setUpSound("f_asher_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > F_ASHER_TIMELORD_SCREAM = SOUNDS.register("f_asher_timelord_scream", () -> setUpSound("f_asher_timelord_scream"));


    //Angela Timelord
    public static final RegistryObject< SoundEvent > F_ANGELA_TIMELORD_HURT = SOUNDS.register("f_angela_timelord_hurt", () -> setUpSound("f_angela_timelord_hurt"));
    public static final RegistryObject< SoundEvent > F_ANGELA_TIMELORD_DIE = SOUNDS.register("f_angela_timelord_die", () -> setUpSound("f_angela_timelord_die"));
    public static final RegistryObject< SoundEvent > F_ANGELA_TIMELORD_TRADE_FAIL = SOUNDS.register("f_angela_timelord_trade_fail", () -> setUpSound("f_angela_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > F_ANGELA_TIMELORD_TRADE_SUCCESS = SOUNDS.register("f_angela_timelord_trade_success", () -> setUpSound("f_angela_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > F_ANGELA_TIMELORD_SCREAM = SOUNDS.register("f_angela_timelord_scream", () -> setUpSound("f_angela_timelord_scream"));

    //aaliceh Timelord
    public static final RegistryObject< SoundEvent > F_AALICEH_TIMELORD_HURT = SOUNDS.register("f_aaliceh_timelord_hurt", () -> setUpSound("f_aaliceh_timelord_hurt"));
    public static final RegistryObject< SoundEvent > F_AALICEH_TIMELORD_TRADE_FAIL = SOUNDS.register("f_aaliceh_timelord_trade_fail", () -> setUpSound("f_aaliceh_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > F_AALICEH_TIMELORD_TRADE_SUCCESS = SOUNDS.register("f_aaliceh_timelord_trade_success", () -> setUpSound("f_aaliceh_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > F_AALICEH_TIMELORD_SCREAM = SOUNDS.register("f_aaliceh_timelord_scream", () -> setUpSound("f_aaliceh_timelord_scream"));

    //Gas Timelord
    public static final RegistryObject< SoundEvent > M_GAS_TIMELORD_HURT = SOUNDS.register("m_gas_timelord_hurt", () -> setUpSound("m_gas_timelord_hurt"));
    public static final RegistryObject< SoundEvent > M_GAS_TIMELORD_DIE = SOUNDS.register("m_gas_timelord_die", () -> setUpSound("m_gas_timelord_die"));
    public static final RegistryObject< SoundEvent > M_GAS_TIMELORD_TRADE_FAIL = SOUNDS.register("m_gas_timelord_trade_fail", () -> setUpSound("m_gas_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > M_GAS_TIMELORD_TRADE_SUCCESS = SOUNDS.register("m_gas_timelord_trade_success", () -> setUpSound("m_gas_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > M_GAS_TIMELORD_SCREAM = SOUNDS.register("m_gas_timelord_scream", () -> setUpSound("m_gas_timelord_scream"));

    //Ramen Timelord
    public static final RegistryObject< SoundEvent > M_RAMEN_TIMELORD_HURT = SOUNDS.register("m_ramen_timelord_hurt", () -> setUpSound("m_ramen_timelord_hurt"));
    public static final RegistryObject< SoundEvent > M_RAMEN_TIMELORD_DIE = SOUNDS.register("m_ramen_timelord_die", () -> setUpSound("m_ramen_timelord_die"));
    public static final RegistryObject< SoundEvent > M_RAMEN_TIMELORD_TRADE_FAIL = SOUNDS.register("m_ramen_timelord_trade_fail", () -> setUpSound("m_ramen_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > M_RAMEN_TIMELORD_TRADE_SUCCESS = SOUNDS.register("m_ramen_timelord_trade_success", () -> setUpSound("m_ramen_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > M_RAMEN_TIMELORD_SCREAM = SOUNDS.register("m_ramen_timelord_scream", () -> setUpSound("m_ramen_timelord_scream"));

    //Didgeridoomen Timelord
    public static final RegistryObject< SoundEvent > M_DIDGERIDOOMEN_TIMELORD_HURT = SOUNDS.register("m_didgeridoomen_timelord_hurt", () -> setUpSound("m_didgeridoomen_timelord_hurt"));
    public static final RegistryObject< SoundEvent > M_DIDGERIDOOMEN_TIMELORD_DIE = SOUNDS.register("m_didgeridoomen_timelord_die", () -> setUpSound("m_didgeridoomen_timelord_die"));
    public static final RegistryObject< SoundEvent > M_DIDGERIDOOMEN_TIMELORD_TRADE_FAIL = SOUNDS.register("m_didgeridoomen_timelord_trade_fail", () -> setUpSound("m_didgeridoomen_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > M_DIDGERIDOOMEN_TIMELORD_TRADE_SUCCESS = SOUNDS.register("m_didgeridoomen_timelord_trade_success", () -> setUpSound("m_didgeridoomen_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > M_DIDGERIDOOMEN_TIMELORD_SCREAM = SOUNDS.register("m_didgeridoomen_timelord_scream", () -> setUpSound("m_didgeridoomen_timelord_scream"));


    //Tommy Timelord
    public static final RegistryObject< SoundEvent > M_TOMMY_TIMELORD_HURT = SOUNDS.register("m_tommy_timelord_hurt", () -> setUpSound("m_tommy_timelord_hurt"));
    public static final RegistryObject< SoundEvent > M_TOMMY_TIMELORD_DIE = SOUNDS.register("m_tommy_timelord_die", () -> setUpSound("m_tommy_timelord_die"));
    public static final RegistryObject< SoundEvent > M_TOMMY_TIMELORD_TRADE_FAIL = SOUNDS.register("m_tommy_timelord_trade_fail", () -> setUpSound("m_tommy_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > M_TOMMY_TIMELORD_TRADE_SUCCESS = SOUNDS.register("m_tommy_timelord_trade_success", () -> setUpSound("m_tommy_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > M_TOMMY_TIMELORD_SCREAM = SOUNDS.register("m_tommy_timelord_scream", () -> setUpSound("m_tommy_timelord_scream"));

    //Connor Timelord
    public static final RegistryObject< SoundEvent > M_CONNOR_TIMELORD_HURT = SOUNDS.register("m_connor_timelord_hurt", () -> setUpSound("m_connor_timelord_hurt"));
    public static final RegistryObject< SoundEvent > M_CONNOR_TIMELORD_DIE = SOUNDS.register("m_connor_timelord_die", () -> setUpSound("m_connor_timelord_die"));
    public static final RegistryObject< SoundEvent > M_CONNOR_TIMELORD_TRADE_FAIL = SOUNDS.register("m_connor_timelord_trade_fail", () -> setUpSound("m_connor_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > M_CONNOR_TIMELORD_TRADE_SUCCESS = SOUNDS.register("m_connor_timelord_trade_success", () -> setUpSound("m_connor_timelord_trade_success"));
    public static final RegistryObject< SoundEvent > M_CONNOR_TIMELORD_SCREAM = SOUNDS.register("m_connor_timelord_scream", () -> setUpSound("m_connor_timelord_scream"));

    //Wodaman Timelord
    public static final RegistryObject< SoundEvent > M_WODAMAN_TIMELORD_HURT = SOUNDS.register("m_wodaman_timelord_hurt", () -> setUpSound("m_wodaman_timelord_hurt"));
    public static final RegistryObject< SoundEvent > M_WODAMAN_TIMELORD_TRADE_FAIL = SOUNDS.register("m_wodaman_timelord_trade_fail", () -> setUpSound("m_wodaman_timelord_trade_fail"));
    public static final RegistryObject< SoundEvent > M_WODAMAN_TIMELORD_TRADE_SUCCESS = SOUNDS.register("m_wodaman_timelord_trade_success", () -> setUpSound("m_wodaman_timelord_trade_success"));


    public static final RegistryObject< SoundEvent > STASER = SOUNDS.register("staser", () -> setUpSound("staser"));
    public static final RegistryObject< SoundEvent > RIFLE = SOUNDS.register("rifle", () -> setUpSound("rifle"));

    private static SoundEvent setUpSound(String soundName) {
        return new SoundEvent(new ResourceLocation(RConstants.MODID, soundName));
    }


}
