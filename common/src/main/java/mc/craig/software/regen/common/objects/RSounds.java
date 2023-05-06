package mc.craig.software.regen.common.objects;

import mc.craig.software.regen.registry.DeferredRegistry;
import mc.craig.software.regen.registry.RegistrySupplier;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class RSounds {
    public static final DeferredRegistry<SoundEvent> SOUNDS = DeferredRegistry.create(RConstants.MODID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> FOB_WATCH = SOUNDS.register("fob_watch", () -> setUpSound("fob_watch"));
    public static final RegistrySupplier<SoundEvent> CRITICAL_STAGE = SOUNDS.register("critical_stage", () -> setUpSound("critical_stage"));
    public static final RegistrySupplier<SoundEvent> HEART_BEAT = SOUNDS.register("heart_beat", () -> setUpSound("heart_beat"));
    public static final RegistrySupplier<SoundEvent> HAND_GLOW = SOUNDS.register("hand_glow", () -> setUpSound("hand_glow"));
    public static final RegistrySupplier<SoundEvent> GRACE_HUM = SOUNDS.register("grace_hum", () -> setUpSound("grace_hum"));
    public static final RegistrySupplier<SoundEvent> ALARM = SOUNDS.register("alarm", () -> setUpSound("alarm"));
    public static final RegistrySupplier<SoundEvent> JAR_BUBBLES = SOUNDS.register("jar_bubbles", () -> setUpSound("jar_bubbles"));
    public static final RegistrySupplier<SoundEvent> SAXONS_ENGLAND = SOUNDS.register("saxons_england", () -> setUpSound("saxons_england"));

    public static final RegistrySupplier<SoundEvent> REGENERATION_0 = SOUNDS.register("regeneration_0", () -> setUpSound("regeneration_0"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_1 = SOUNDS.register("regeneration_1", () -> setUpSound("regeneration_1"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_2 = SOUNDS.register("regeneration_2", () -> setUpSound("regeneration_2"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_3 = SOUNDS.register("regeneration_3", () -> setUpSound("regeneration_3"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_4 = SOUNDS.register("regeneration_4", () -> setUpSound("regeneration_4"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_5 = SOUNDS.register("regeneration_5", () -> setUpSound("regeneration_5"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_6 = SOUNDS.register("regeneration_6", () -> setUpSound("regeneration_6"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_7 = SOUNDS.register("regeneration_7", () -> setUpSound("regeneration_7"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_TROUGHTON = SOUNDS.register("regeneration_troughton", () -> setUpSound("regeneration_troughton"));
    public static final RegistrySupplier<SoundEvent> REGENERATION_WATCHER = SOUNDS.register("regeneration_watcher", () -> setUpSound("regeneration_watcher"));
    public static final RegistrySupplier<SoundEvent> DRUM_BEAT = SOUNDS.register("drum_beat", () -> setUpSound("drum_beat"));
    public static final RegistrySupplier<SoundEvent> GUN_EMPTY = SOUNDS.register("gun_empty", () -> setUpSound("gun_empty"));

    public static final RegistrySupplier<SoundEvent> STASER = SOUNDS.register("staser", () -> setUpSound("staser"));
    public static final RegistrySupplier<SoundEvent> RIFLE = SOUNDS.register("rifle", () -> setUpSound("rifle"));
    public static final RegistrySupplier<SoundEvent> REGEN_LONG = SOUNDS.register("regen_long", () -> setUpSound("regen_long"));

    private static SoundEvent setUpSound(String soundName) {
        return new SoundEvent(new ResourceLocation(RConstants.MODID, soundName));
    }


}
