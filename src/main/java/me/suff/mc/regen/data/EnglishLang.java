package me.suff.mc.regen.data;

import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.common.objects.REntities;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import me.suff.mc.regen.common.traits.Traits;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLang extends LanguageProvider {

    public EnglishLang(DataGenerator gen) {
        super(gen, RConstants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // === Advancements ===
        add("advancements.regen.title.change_refusal", "I. WILL. NOT. CHANGE!");
        add("advancements.regen.desc.change_refusal", "Punch a block to delay your Regeneration!");
        add("advancements.regen.title.critical_period", "Your song is ending...");
        add("advancements.regen.desc.critical_period", "Refuse to Regenerate up till the point of near death...");
        add("advancements.regen.title.first_regen", "Change my dear...");
        add("advancements.regen.desc.first_regen", "Regenerate for the First time!");
        add("advancements.regen.title.council", "Member of the High Council");
        add("advancements.regen.desc.council", "Wear Gallifreyan Robes");
        add("advancements.regen.title.gallifrey", "Constellation of Kasterborous");
        add("advancements.regen.desc.gallifrey", "Step foot on Gallifrey");
        add("advancements.regen.title.gallifreyan_weapon", "There's no Stun Setting...");
        add("advancements.regen.desc.gallifreyan_weapon", "Hold both Gallifreyan weapons");
        add("advancements.regen.title.timelord_trade", "Galactic Fortune");
        add("advancements.regen.desc.timelord_trade", "Trade with a Timelord");
        add("advancements.regen.title.hand_cut", "For Science!");
        add("advancements.regen.desc.hand_cut", "Cut off your hand");
        add("advancements.regen.title.zero_room", "Smells like roses");
        add("advancements.regen.desc.zero_room", "Use Zero Room Roundels to negate negative Regeneration Effects");
        add("advancements.regen.title.watch_is_me", "This watch, is me!");
        add("advancements.regen.desc.watch_is_me", "Obtain a Chameleon Arch");
        add("advancements.regen.title.guard", "Chancellery Guard");
        add("advancements.regen.desc.guard", "Wear all Timelord Guard Armor");


        // === Damages Sources ===
        add("regen.source.regen_energy", "%s was blasted by Regeneration Energy!");
        add("regen.source.regen_heal", "%s died by donating too much Regeneration energy...");
        add("regen.source.regen_crit", "%s died from holding in their regeneration for too long");
        add("regen.source.theft", "%s had their body stolen!");
        add("regen.source.lindos", "%s consumed lindos hormones! They are reborn!");
        add("regen.source.regen_killed", "%s was killed mid-regeneration...");
        add("regen.source.staser", "%s was shot dead with a Staser");
        add("regen.source.rifle", "%s was shot dead with a Rifle");
        add("regen.source.forced", "%s forced themselves to regenerate!");
        add("regen.source.hand_cut", "%s died from blood loss!");
        add("regen.timelord_type.female_council", "Timelord Council (Female)");
        add("regen.timelord_type.guard", "Timelord Guard");
        add("regen.timelord_type.male_council", "Timelord Council (Male)");

        //Block
        add(RBlocks.BIO_CONTAINER.get(), "Bio-Container");
        add(RBlocks.ZERO_ROUNDEL.get(), "Zero Roundel (Half)");
        add(RBlocks.ZERO_ROOM_FULL.get(), "Zero Roundel (Full)");

        //Item Group
        add("itemGroup.regen", "Regeneration");


        //Sounds
        for (int i = 0; i < 7; i++) {
            add("regen.sound.regeneration_" + i, "Regeneration");
        }
        add("regen.sound.regen_bubble", "Jar bubbles");
        add("regen.sound.hum", "Calming hums");
        add("regen.sound.fob_watch_dialogue", "Fob Watch Speaks");
        add("regen.sound.critical_stage", "Critical Condition");
        add("regen.sound.heart_beat", "Timelord Heartbeat");
        add("regen.sound.hand_glow", "Glowing Hands");
        add("regen.sound.fob_watch", "Ticking");
        add("regen.sound.regen_breath", "Regeneration exhale");
        add("regen.sound.alarm", "Regeneration Alarm");
        add("regen.sound.m_timelord.hurt", "Male Timelord Hurts");
        add("regen.sound.f_timelord.hurt", "Female Timelord Hurts");
        add("regen.sound.m_timelord.die", "Male Timelord Dies");
        add("regen.sound.m_timelord.trade_fail", "Male Timelord Trade Fail");
        add("regen.sound.m_timelord.trade_success", "Male Timelord Trade Success");
        add("regen.sound.m_timelord.scream", "Male Timelord Screams");

        //Items
        add(RItems.FOB.get(), "Chameleon Arch");
        add(RItems.PISTOL.get(), "Staser");
        add(RItems.RIFLE.get(), "Staser Rifle");
        add(RItems.HAND.get(), "Hand");
        add("item.regen.hand_with_arg", "%s Hand");

        add(RItems.M_ROBES_HEAD.get(), "Timelord Collar Piece (Male)");
        add(RItems.M_ROBES_CHEST.get(), "Timelord Suit (Male)");
        add(RItems.M_ROBES_LEGS.get(), "Timelord Lower Robes (Male)");
        add(RItems.ROBES_FEET.get(), "Timelord Shoes");

        add(RItems.F_ROBES_HEAD.get(), "Timelord Collar Piece (Female)");
        add(RItems.F_ROBES_CHEST.get(), "Timelord Suit (Female)");
        add(RItems.F_ROBES_LEGS.get(), "Timelord Lower Robes (Female)");

        add(RItems.GUARD_HELMET.get(), "Timelord Solider Helmet");
        add(RItems.GUARD_CHEST.get(), "Timelord Chest Armor");
        add(RItems.GUARD_LEGS.get(), "Timelord Lower Armor");
        add(RItems.GUARD_FEET.get(), "Timelord Boots");

        add(RBlocks.ZINC_ORE.get(), "Zinc Ore");
        add(RItems.ZINC.get(), "Zinc Ingot");

        //Messages
        add("regen.messages.regen_death_msg", "%s is regenerating...");
        add("regen.messages.now_timelord", "You are now a Time lord!");
        add("regen.messages.gained_regens", "Recieved Regenerations!");
        add("regen.messages.new_skin", "Your skin will change next Regeneration!");
        add("regen.messages.transfer.success", "You've transferred one of your regenerations into the watch");
        add("regen.messages.transfer.full_watch", "You can't store anymore regenerations in this watch!");
        add("regen.messages.transfer.empty_watch", "This watch is empty!");
        add("regen.messages.transfer.max_regens", "You already have the maximum number of regenerations");
        add("regen.messages.transfer.no_regens", "You don't have any regenerations left to transfer");
        add("regen.messages.regen_chat_message", "%s is regenerating...");
        add("regen.messages.warning.grace", "You are in a state of grace! || Press %s to regenerate!");
        add("regen.messages.warning.grace_critical", "You are near death! || Press %s to regenerate!");
        add("regen.messages.regen_delayed", "Regeneration delayed!");
        add("regen.messages.regen_warning", "Punch a block to delay your regeneration further");
        add("regen.messages.jar", "You have gained a Regeneration from harvested lindos!");
        add("regen.messages.jar_not_enough", "There is not enough Lindos in this jar! [100 Lindos = 1 free Regeneration]");
        add("regen.messages.jar_no_break", "You cannot break this Jar while it has %s Lindos Energy!");
        add("regen.messages.not_alive", "You cannot transfer Regenerations in this state!");
        add("regen.messages.item_taken_regens", "You have gained %s regenerations from %s");
        add("regen.messages.cannot_use", "You cannot use this right now!");
        add("regen.messages.healed", "You have given %s some of your Regeneration Energy!");
        add("regen.messages.reduced_dmg", "Reduced Damage due to Post Regen State!");
        add("regen.messages.fall_dmg", "Fall damage reduced due to post regen state! Although you're gonna feel sick..");
        add("regen.messages.post_ended", "Your Regeneration process is now entirely completed!");
        add("regen.messages.fast_forward_cmd_fail", "Nothing to fast forward to.");
        add("regen.messages.new_trait", "New Trait: %s");
        add("regen.messages.cant_glow", "You cannot activate a glowing hand without being in a grace period");

        // === Toasts ===
        add("regen.toast.regenerated", "You have regenerated!");
        add("regen.toast.regenerations_left", "%s regenerations left");
        add("regen.toast.enter_critical", "You are going critical");
        add("regen.toast.enter_critical.sub", "%s minutes left...");
        add("regen.toast.timelord", "You are now a Time lord!");
        add("regen.toast.to_use", "You have %s lives now!");

        // === GUI Elements ===
        add("regen.gui.red", "Red: ");
        add("regen.gui.green", "Green: ");
        add("regen.gui.blue", "Blue: ");
        add("regen.gui.primary", "Primary");
        add("regen.gui.secondary", "Secondary");
        add("regen.gui.undo", "Undo");
        add("regen.gui.close", "Close");
        add("regen.gui.input_color", "Hex");
        add("regen.gui.previous", "<");
        add("regen.gui.next", ">");
        add("regen.gui.default", "Default");
        add("regen.gui.open_folder", "Skin Dir");
        add("regen.gui.skin_choice", "Skin Settings");
        add("regen.gui.save", "Save");
        add("regen.gui.next_incarnation", "Select next incarnation");
        add("regen.gui.reset_skin", "Reset");
        add("regen.gui.infinite_regenerations", "Infinite Regeneration Mode");
        add("regen.gui.remaining_regens.status", "Remaining Regenerations: %s");
        add("regen.gui.color_gui", "Color selection");
        add("regen.gui.current_skin", "Select Next Incarnation");
        add("regen.gui.back", "Back");
        add("regen.gui.preferences", "Preferences");

        for (PlayerUtil.SkinType value : PlayerUtil.SkinType.values()) {
            add("regeneration.skin_type." + value.name().toLowerCase(), grammerNazi(value.name().toLowerCase()));
        }

        add("regen.gui.sound_scheme.hum", "Graceful Hum");
        add("regen.gui.sound_scheme.drum", "Resistant Drums");

        // === Regen ===
        add(TransitionTypes.ENDER_DRAGON.get().getTranslation().getKey(), "Ender-Dragon");
        add(TransitionTypes.SPARKLE.get().getTranslation().getKey(), "Sparkle");
        add(TransitionTypes.WATCHER.get().getTranslation().getKey(), "Watcher");
        add(TransitionTypes.FIERY.get().getTranslation().getKey(), "Fiery");
        add(TransitionTypes.TROUGHTON.get().getTranslation().getKey(), "Troughton");
        add(TransitionTypes.BLAZE.get().getTranslation().getKey(), "Blaze");

        //Entity
        add(REntities.TIMELORD.get(), "Timelord");

        //Traits
        add(Traits.BORING.get().getTranslation().getKey(), "Boring");
        add(Traits.BORING.get().getDescription().getKey(), "Plain Human!");

        add(Traits.QUICK.get().getTranslation().getKey(), "Quick");
        add(Traits.QUICK.get().getDescription().getKey(), "Sprint Faster!");

        add(Traits.SWIM_SPEED.get().getTranslation().getKey(), "Mermaid");
        add(Traits.SWIM_SPEED.get().getDescription().getKey(), "Swim Faster!");

        add(Traits.FAST_MINE.get().getTranslation().getKey(), "Fast Mine");
        add(Traits.FAST_MINE.get().getDescription().getKey(), "Mine Faster!");

        add(Traits.SMART.get().getTranslation().getKey(), "Smart");
        add(Traits.SMART.get().getDescription().getKey(), "More XP Pickup!");

        add(Traits.LONG_ARMS.get().getTranslation().getKey(), "Long Arms");
        add(Traits.LONG_ARMS.get().getDescription().getKey(), "Reach further!");

        add(Traits.STRONG.get().getTranslation().getKey(), "Strong");
        add(Traits.STRONG.get().getDescription().getKey(), "Punch Harder!");

        add(Traits.KNOCKBACK.get().getTranslation().getKey(), "KnockBack");
        add(Traits.KNOCKBACK.get().getDescription().getKey(), "Don't get flung back!");

        add(Traits.FISH.get().getTranslation().getKey(), "Fish");
        add(Traits.FISH.get().getDescription().getKey(), "Infinite Oxygen under water");

        add(Traits.LEAP.get().getTranslation().getKey(), "Leap");
        add(Traits.LEAP.get().getDescription().getKey(), "Jump a little higher");

        add(Traits.FIRE.get().getTranslation().getKey(), "Fire Immune");
        add(Traits.FIRE.get().getDescription().getKey(), "Immune to fire");

        add(Traits.ENDER_HURT.get().getTranslation().getKey(), "Ender Emergency");
        add(Traits.ENDER_HURT.get().getDescription().getKey(), "Teleport away when hurt");
    }

    public String grammerNazi(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        return firstLetter + text.substring(1);
    }

}
