package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RConstants;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class RegenEnglishLang extends LanguageProvider {

    public RegenEnglishLang(DataGenerator gen) {
        super(gen, RConstants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // === Traits ===
        addTrait(TraitRegistry.FIRE_RESISTANCE.get(), "Ignis Resistentia", "Grants immunity to damage from fire");
        addTrait(TraitRegistry.SPEED.get(), "Celeritas", "Move swifter throughout the world");
        addTrait(TraitRegistry.HUMAN.get(), "Mane homo", "Nothing - just plain human");
        addTrait(TraitRegistry.WATER_BREATHING.get(), "Humanum Piscium", "Breathe forever underwater");
        addTrait(TraitRegistry.STRENGTH.get(), "Fortis Human", "Grants super human strength");
        addTrait(TraitRegistry.ARROW_DODGE.get(), "Fracti sagitta", "Arrow Damage will be negated");
        addTrait(TraitRegistry.PHOTOSYNTHETIC.get(), "Sol comedentis", "Being out in the world feeds you");
        addTrait(TraitRegistry.SLOW_FALL.get(), "Non volans", "Gracely fall to the ground");
        addTrait(TraitRegistry.KNOCKBACK.get(), "Simia nulla musca", "Negate knockback effects");

        // === Advancements ===
        addAdvancement("fob_watch", "This watch, is me!", "Obtain a Fob Watch");
        addAdvancement("critical_grace", "Your song is ending...", "Refuse to Regenerate up till the point of near death...");
        addAdvancement("first_regeneration", "Change my dear...", "Regenerate for the First time!");
        addAdvancement("zero_status", "Smells like roses", "Use Zero Room Roundels to negate negative Regeneration Effects");
        addAdvancement("severed_hand", "For Science!", "Cut off your arm");
        addAdvancement("timelord_trade", "Galactic Fortune", "Trade with a Timelord");
        addAdvancement("change_refusal", "I. WILL. NOT. CHANGE!", "Punch a block to delay your Regeneration!");
        addAdvancement("council_clothing", "Member of the High Council", "Wear a full set of Gallifreyan Robes");
        addAdvancement("ready_for_war", "There's no Stun Setting...", "Hold both Gallifreyan weapons");
        addAdvancement("gallifreyan_soldier", "Chancellery Guard", "Wear all Timelord Guard Armor");

        // === Block ===
        add(RBlocks.BIO_CONTAINER.get(), "Bio-Container");
        add(RBlocks.ZERO_ROUNDEL.get(), "Zero Roundel (Half)");
        add(RBlocks.ZERO_ROOM_FULL.get(), "Zero Roundel (Full)");
        add(RBlocks.AZBANTIUM.get(), "Azbantium");
        add(RBlocks.ZINC_ORE.get(), "Zinc Ore");
        add(RBlocks.ZINC_ORE_DEEPSLATE.get(), "Deepslate Zinc Ore");

        // === Items ===
        add(RItems.FOB.get(), "Fob Watch");
        add(RItems.PISTOL.get(), "Staser");
        add(RItems.RIFLE.get(), "Staser Rifle");
        add(RItems.HAND.get(), "Hand");
        add(RItems.PLASMA_CARTRIDGE.get(), "Plasma Cartridge");
        add(RItems.M_ROBES_HEAD.get(), "Timelord Collar Piece (Male)");
        add(RItems.M_ROBES_CHEST.get(), "Timelord Suit (Male)");
        add(RItems.M_ROBES_LEGS.get(), "Timelord Lower Robes (Male)");
        add(RItems.ROBES_FEET.get(), "Boots");
        add(RItems.F_ROBES_HEAD.get(), "Timelord Collar Piece (Female)");
        add(RItems.F_ROBES_CHEST.get(), "Timelord Suit (Female)");
        add(RItems.F_ROBES_LEGS.get(), "Timelord Lower Robes (Female)");
        add(RItems.GUARD_HELMET.get(), "Timelord Soldier Helmet");
        add(RItems.GUARD_CHEST.get(), "Timelord Chest Armor");
        add(RItems.GUARD_LEGS.get(), "Timelord Lower Armor");
        add(RItems.GUARD_FEET.get(), "Timelord Boots");
        add(RItems.ZINC.get(), "Zinc Ingot");

        // === Damages Sources ===
        add("regen.source.regen_energy", "%s was blasted by Regeneration Energy!");
        add("regen.source.regen_heal", "%s died by donating too much Regeneration Energy...");
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



        //Item Group
        add("itemGroup.regen", "Regeneration");
        add("itemGroup.regen.regen", "Regeneration");


        //Tooltip
        add("button.tooltip.reset_mojang", "Resets your Skin to Mojang Skin");
        add("button.tooltip.previous_skin", "Previous Skin");
        add("button.tooltip.next_skin", "Next Skin");
        add("button.tooltip.save_skin", "Save Changes");
        add("button.tooltip.open_folder", "Open Local Skin Folder");
        add("button.tooltip.upload2mc", "Upload currently selected skin to Minecraft");

        //Sounds
        for (int i = 0; i < 7; i++) {
            add("regen.sound.regeneration_" + i, "Regenerating");
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
        add("regen.sound.m_timelord.die", "Male Timelord Dies");
        add("regen.sound.m_timelord.trade_fail", "Male Timelord Trade Fail");
        add("regen.sound.m_timelord.trade_success", "Male Timelord Trade Success");
        add("regen.sound.m_timelord.scream", "Male Timelord Screams");

        add("regen.sound.f_timelord.hurt", "Female Timelord Hurts");
        add("regen.sound.f_timelord.die", "Female Timelord Dies");
        add("regen.sound.f_timelord.trade_fail", "Female Timelord Trade Fail");
        add("regen.sound.f_timelord.trade_success", "Female Timelord Trade Success");
        add("regen.sound.f_timelord.scream", "Female Timelord Screams");

        add("regen.sound.rifle", "Rifle fires");
        add("regen.sound.staser", "Staser Fires");

        add("item.regen.hand_with_arg", "%s Hand");

        //Messages
        add("regen.messages.regen_death_msg", "%s is regenerating...");
        add("regen.messages.now_timelord", "You are now a Time lord!");
        add("regen.messages.gained_regens", "Recieved Regenerations!");
        add("regen.messages.new_skin", "Your skin will change next Regeneration!");
        add("regen.messages.transfer.success", "You've transferred one of your Regenerations into the watch");
        add("regen.messages.transfer.full_watch", "You can't store anymore Regenerations in this watch!");
        add("regen.messages.transfer.empty_watch", "This watch is empty!");
        add("regen.messages.transfer.max_regens", "You already have the maximum number of Regenerations");
        add("regen.messages.transfer.no_regens", "You don't have any Regenerations left to transfer");
        add("regen.messages.regen_chat_message", "%s is regenerating...");
        add("regen.messages.warning.grace", "You are in a state of grace! || Press %s to regenerate!");
        add("regen.messages.warning.grace_critical", "You are near death! || Press %s to regenerate!");
        add("regen.messages.regen_delayed", "Regeneration delayed!");
        add("regen.messages.regen_warning", "Punch a block to delay your Regeneration further");
        add("regen.messages.jar", "You have gained a Regeneration from harvested lindos!");
        add("regen.messages.jar_not_enough", "There is not enough Lindos in this jar! [100 Lindos = 1 free Regeneration]");
        add("regen.messages.jar_no_break", "You cannot break this Jar while it has %s Lindos Energy!");
        add("regen.messages.not_alive", "You cannot transfer Regenerations in this state!");
        add("regen.messages.item_taken_regens", "You have gained %s regenerations from %s");
        add("regen.messages.cannot_use", "You cannot use this right now!");
        add("regen.messages.healed", "You have given %s some of your Regeneration Energy!");
        add("regen.messages.reduced_dmg", "Reduced Damage due to post-regen State!");
        add("regen.messages.fall_dmg", "Fall damage reduced due to post-regen state! Although you're gonna feel sick..");
        add("regen.messages.post_ended", "Your Regeneration process is now entirely completed!");
        add("regen.messages.fast_forward_cmd_fail", "Nothing to fast forward to.");
        add("regen.messages.new_trait", "New Trait: %s");
        add("regen.messages.cant_glow", "You cannot activate a glowing hand without being in a grace period");

        // === Command Output ===
        add("command.regen.set_trait.success", "Set trait for player %s to %s");
        add("command.regen.set_trait.error", "Could not set trait for player %s to %s");
        add("command.regen.set_regen.success", "Set Regenerations for entity %s to %s");
        add("command.regen.set_regen.invalid_entity", "Could not set %s Regenerations for %s, it is not a Living Entity");
        add("command.regen.set_regen.config_off", "Could not set Regenerations for %s, the Config Option %s is set to %s");

        // === Config ===
        //Skin
        add("config.regen.downloadTrendingSkins", "Download Trending Skins?");
        add("config.regen.downloadPreviousSkins", "Download Previous Skins?");
        add("config.regen.downloadInternalSkins", "Download Doctor Who Skins?");

        //Client
        add("config.regen.changemyskin", "Change Skin?");
        add("config.regen.perspective", "Change Perspective view?");
        add("config.regen.timelordRenderSecondLayers", "Render Head layers on Timelords?");
        add("config.regen.heartIcons", "Regeneration Hearts?");

        //Common
        add("config.regen.max_regens", "Maximum Regenerations?");
        add("config.regen.lose_regens_on_death", "Lose Regenerations on Death?");
        add("config.regen.fiery_regen", "Spawn Fire?");
        add("config.regen.genFobLoot", "Chameleon Arch in chests?");
        add("config.regen.mobsHaveRegens", "Mobs allowed Regenerations?");
        add("config.regen.regen_messages", "Send Regeneration death chat messages?");
        add("config.regen.skindir", "Base Skin Directory");
        add("config.regen.post_regen_duration", "Post Regeneration Duration");
        add("config.regen.post_regenerationEffect_level", "Post Regeneration Level");
        add("config.regen.reset_hunger", "Reset Hunger?");
        add("config.regen.reset_oxygen", "Reset Air?");
        add("config.regen.regeneration_fire_immunity", "Fire immunity?");
        add("config.regen.regeneration_knocksback_players", "Knocks back players?");
        add("config.regen.absorption_level", "Absorption Level?");
        add("config.regen.post_effects", "Post Regeneration Effects");
        add("config.regen.gracePeriodLength", "Grace Period Length");
        add("config.regen.criticalPhaseLength", "Critical Period Length");
        add("config.regen.criticalDamageChance", "Critical Damage Chance");
        add("config.regen.handGlowInterval", "Hand Glow Interval");
        add("config.regen.handGlowTriggerDelay", "Hand Trigger Delay");
        add("config.regen.regenerative_kill_range", "Regeneration hurt range");
        add("config.regen.regenerative_knockback", "Knockback Radius");
        add("config.regen.regenerative_knockback_range", "Knockback Range");
        add("config.regen.traitsenabled", "Traits Enabled?");
        add("config.regen.disabledTraits", "Disabled Traits");
        add("config.regen.upwards_motion", "Upwards motion?");
        add("config.regen.update_checker", "Enable Update Checker");


        // === Toasts ===
        add("regen.toast.regenerated", "You have regenerated!");
        add("regen.toast.regenerations_left", "%s Regenerations left");
        add("regen.toast.enter_critical", "You are going critical");
        add("regen.toast.enter_critical.sub", "%s minutes left...");
        add("regen.toast.timelord", "You are now a Time lord!");
        add("regen.toast.to_use", "You have %s lives now!");

        // === GUI Elements ===
        add("regen.gui.primary", "Primary");
        add("regen.gui.secondary", "Secondary");
        add("regen.gui.undo", "Undo");
        add("regen.gui.close", "Close");
        add("regen.gui.input_color", "Hex");
        add("regen.gui.previous", "<");
        add("regen.gui.next", ">");
        add("regen.gui.default", "Default");
        add("regen.gui.open_folder", "Skin Dir");
        add("regen.gui.skin_choice", "Skin");
        add("regen.gui.save", "Save");
        add("regen.gui.next_incarnation", "Select next incarnation");
        add("regen.gui.reset_skin", "Reset");
        add("regen.gui.infinite_regenerations", "Infinite Regeneration Mode");
        add("regen.gui.remaining_regens.status", "Regenerations: %s");
        add("regen.gui.color_gui", "Colors");
        add("regen.gui.current_skin", "Select Next Incarnation");
        add("regen.gui.back", "Back");
        add("regen.gui.preferences", "Preferences");

        for (PlayerUtil.SkinType value : PlayerUtil.SkinType.values()) {
            String valueName = value.name().toLowerCase();
            add("regeneration.skin_type." + valueName, capitalize(valueName));
        }

        add("regen.gui.sound_scheme.hum", "Graceful Hum");
        add("regen.gui.sound_scheme.drum", "Resistant Drums");
        add("regen.gui.sound_scheme.saxon_england", "Saxons England");

        // === Regen ===
        add(TransitionTypes.ENDER_DRAGON.getTranslationKey(), "Ender-Dragon");
        add(TransitionTypes.SPARKLE.getTranslationKey(), "Sparkle");
        add(TransitionTypes.WATCHER.getTranslationKey(), "Watcher");
        add(TransitionTypes.FIERY.getTranslationKey(), "Fiery");
        add(TransitionTypes.TROUGHTON.getTranslationKey(), "Troughton");
        add(TransitionTypes.BLAZE.getTranslationKey(), "Blaze");
        add(TransitionTypes.TRISTIS_IGNIS.getTranslationKey(), "Tristis Ignis");

        //Entity
        add(REntities.TIMELORD.get(), "Timelord");
        add(REntities.WATCHER.get(), "Watcher");
    }

    public String capitalize(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        return firstLetter + text.substring(1);
    }

    public void addAdvancement(String advancement, String title, String description){
        add(RegenAdvancementsProvider.RegenAdvancements.getTitleTranslation(advancement), title);
        add(RegenAdvancementsProvider.RegenAdvancements.getDescriptionTranslation(advancement), description);
    }

    public void addTrait(TraitBase traitBase, String title, String description) {
        add("trait." + TraitRegistry.TRAITS_REGISTRY.getKey(traitBase).getPath() + ".title", title);
        add("trait." + TraitRegistry.TRAITS_REGISTRY.getKey(traitBase).getPath() + ".description", description);
    }
}
