package me.swirtzly.regen.data;

import me.swirtzly.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLang extends LanguageProvider {

    public EnglishLang(DataGenerator gen) {
        super(gen, RConstants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        // === Damages Sources ===
        add("source.regen.regen_energy", "%s was blasted by Regeneration Energy!");
        add("source.regen.regen_heal", "%s died by donating too much Regeneration energy...");
        add("source.regen.regen_crit", "%s died from holding in their regeneration for too long");
        add("source.regen.theft", "%s had their body stolen!");
        add("source.regen.lindos", "%s consumed lindos hormones! They are reborn!");
        add("source.regen.regen_killed", "%s was killed mid-regeneration...");

        //Sounds
        for (int i = 0; i < 7; i++) {
            add("regen.sound.regeneration_" + i, "Regeneration");
        }
        add("regen.subtitles.regen_bubble", "Jar bubbles");
        add("regen.subtitles.hum", "Calming hums");
        add("regen.subtitles.fob_watch_dialogue", "Fob Watch Speaks");
        add("regen.subtitles.critical_stage", "Critical Condition");
        add("regen.subtitles.heart_beat", "Timelord Heartbeat");
        add("regen.subtitles.hand_glow", "Glowing Hands");
        add("regen.subtitles.fob_watch", "Ticking");
        add("regen.subtitles.regen_breath", "Regeneration exhale");
        add("regen.subtitles.alarm", "Regeneration Alarm");

        //Messages
        add("regeneration.messages.regen_death_msg", "%s is regenerating...");

    }
}
