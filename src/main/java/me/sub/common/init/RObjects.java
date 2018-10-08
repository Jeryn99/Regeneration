package me.sub.common.init;

import me.sub.Regeneration;
import me.sub.common.advancements.AdvancementBase;
import me.sub.common.items.ItemFobWatch;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class RObjects {

    //Items
    public static List<Item> ITEMS = new ArrayList<>();

    @SubscribeEvent
    public static void addItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> reg = e.getRegistry();
        reg.registerAll(setUpItem(new ItemFobWatch(), "fob_watch"));
    }

    private static List<AdvancementBase> ADVANCEMENTS = new ArrayList<>();
    public static AdvancementBase BECOME_TIMELORD, IWILLNOTCHANGE, FIRST_REGEN;

    public static void advancements() {
        BECOME_TIMELORD = createAdvancement(new AdvancementBase("become_timelord"));
        IWILLNOTCHANGE = createAdvancement(new AdvancementBase("change_refusal"));
        FIRST_REGEN = createAdvancement(new AdvancementBase("first_regen"));
        for (AdvancementBase base : RObjects.ADVANCEMENTS) {
            CriteriaTriggers.register(base);
        }
        ADVANCEMENTS.clear();
    }

    private static AdvancementBase createAdvancement(AdvancementBase advancementBase) {
        ADVANCEMENTS.add(advancementBase);
        return advancementBase;
    }

    private static Item setUpItem(Item item, String name) {
        item.setRegistryName(Regeneration.MODID, name);
        item.setTranslationKey(name);
        ITEMS.add(item);
        return item;
    }

    //Sounds
    @SubscribeEvent
    public static void addSounds(RegistryEvent.Register<SoundEvent> e) {
        IForgeRegistry<SoundEvent> reg = e.getRegistry();
        reg.registerAll(setUpSound("regen_1"), setUpSound("fob_watch"), setUpSound("critical_stage"), setUpSound("heart_beat"), setUpSound("hand_glow"));
    }

    private static SoundEvent setUpSound(String soundName) {
        return new SoundEvent(new ResourceLocation(Regeneration.MODID, soundName)).setRegistryName(soundName);
    }

    @GameRegistry.ObjectHolder(Regeneration.MODID)
    public static class Items {
        public static final Item FOB_WATCH = null;
    }

    @GameRegistry.ObjectHolder(Regeneration.MODID)
    public static class Sounds {
        public static final SoundEvent FOB_WATCH = null;
        public static final SoundEvent REGEN_1 = null;
        public static final SoundEvent CRITICAL_STAGE = null;
        public static final SoundEvent HEART_BEAT = null;
        public static final SoundEvent HAND_GLOW = null;
    }
}
