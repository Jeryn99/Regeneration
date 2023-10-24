package mc.craig.software.regen.forge.data;

import mc.craig.software.regen.common.item.SpawnItem;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.REntities;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import mc.craig.software.regen.config.RegenConfig;
import mc.craig.software.regen.util.PlayerUtil;
import mc.craig.software.regen.util.RegenDamageTypes;
import mc.craig.software.regen.util.constants.RConstants;
import mc.craig.software.regen.util.constants.RMessages;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.data.LanguageProvider;

public class RegenEnglishLang extends LanguageProvider {

    public RegenEnglishLang(PackOutput output) {
        super(output, RConstants.MODID, "en_us");
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
        addTrait(TraitRegistry.JUMP_BOOST.get(), "Lepus Humanus", "Get a extra spring in your step");

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
        add("item.regen.hand_with_name", "%s Hand");
        add("item.regen.chalice_of", "Chalice of");

        // === Damages Sources ===
        add(RegenDamageTypes.REGEN_DMG_CRITICAL, "%s died from holding in their regeneration for too long");
        add(RegenDamageTypes.REGEN_DMG_FORCED, "%s forced themselves to regenerate!");
        add(RegenDamageTypes.REGEN_DMG_HAND, "%s died from blood loss!");
        add(RegenDamageTypes.REGEN_DMG_KILLED, "%s was killed mid-regeneration...");
        add(RegenDamageTypes.REGEN_DMG_STASER, "%s was shot dead with a Staser");
        add(RegenDamageTypes.REGEN_DMG_RIFLE, "%s was shot dead with a Rifle");
        add(RegenDamageTypes.REGEN_DMG_ENERGY_EXPLOSION, "%s was blasted by Regeneration Energy!");

        // === Timelords ===
        addTimelordtype(SpawnItem.Timelord.GUARD, "Timelord Guard");
        addTimelordtype(SpawnItem.Timelord.FEMALE_COUNCIL, "Timelord Council (Female)");
        addTimelordtype(SpawnItem.Timelord.MALE_COUNCIL, "Timelord Council (Male)");

        // === Regeneration Transtions ===
        add(TransitionTypes.ENDER_DRAGON.getTranslationKey(), "Ender-Dragon");
        add(TransitionTypes.SPARKLE.getTranslationKey(), "Sparkle");
        add(TransitionTypes.WATCHER.getTranslationKey(), "Watcher");
        add(TransitionTypes.FIERY.getTranslationKey(), "Fiery");
        add(TransitionTypes.TROUGHTON.getTranslationKey(), "Troughton");
        add(TransitionTypes.BLAZE.getTranslationKey(), "Blaze");
        add(TransitionTypes.TRISTIS_IGNIS.getTranslationKey(), "Tristis Ignis");
        add(TransitionTypes.SNEEZE.getTranslationKey(), "'Sneeze'");
        add(TransitionTypes.DRINK.getTranslationKey(), "'Drink'");

        // === Item Group ===
        add("itemGroup.regen", "Regeneration");

        // === Sound Schemes ===
        addSoundScheme(IRegen.TimelordSound.HUM, "Graceful Hum");
        addSoundScheme(IRegen.TimelordSound.DRUM, "Resistant Drums");
        addSoundScheme(IRegen.TimelordSound.SAXON_ENGLAND, "Saxons England");

        // === Entity ===
        add(REntities.TIMELORD.get(), "Timelord");
        add(REntities.WATCHER.get(), "Watcher");

        // === Messages ===
        add(RMessages.REGENERATION_DEATH_MSG, "%s is regenerating...");
        add(RMessages.TIMELORD_STATUS, "You are now a Time lord!");
        add(RMessages.GAINED_REGENERATIONS, "Received Regenerations!");
        add(RMessages.END_OF_PROCESS, "Your Regeneration process is now entirely completed!");
        add(RMessages.PUNCH_WARNING, "Punch a block to delay your Regeneration further");
        add(RMessages.POST_REDUCED_DAMAGE, "Reduced Damage due to post-regen State!");
        add(RMessages.DELAYED_REGENERATION, "Regeneration delayed!");
        add(RMessages.TRANSFER_SUCCESSFUL, "You've transferred one of your Regenerations into the watch");
        add(RMessages.TRANSFER_FULL_WATCH, "You can't store anymore Regenerations in this watch!");
        add(RMessages.TRANSFER_EMPTY_WATCH, "This watch is empty!");
        add(RMessages.TRANSFER_MAX_REGENS, "You already have the maximum number of Regenerations");
        add(RMessages.TRANSFER_NO_REGENERATIONS, "You don't have any Regenerations left to transfer");
        add(RMessages.TRANSFER_INVALID_STATE, "You cannot transfer Regenerations in this state!");

        // === Command Output ===
        add(RMessages.FAST_FORWARD_CMD_FAIL, "Nothing to fast forward to!");
        add(RMessages.SET_TRAIT_SUCCESS, "Set trait for player %s to %s");
        add(RMessages.SET_TRAIT_ERROR, "Could not set trait for player %s to %s");
        add(RMessages.SET_REGEN_SUCCESS, "Set Regenerations for entity %s to %s");
        add(RMessages.SET_REGEN_INVALID_ENTITY, "Could not set %s Regenerations for %s, it is not a Living Entity");
        add(RMessages.SET_REGEN_CONFIG_DISABLED, "Could not set Regenerations for %s, the Config Option %s is set to %s");

        // === ArgumentTypes ===
        add("argument.regeneration.trait.invalid", "Invalid trait!");

        // === Config ===
        //Skin
        addConfig(RegenConfig.CLIENT.downloadTrendingSkins, "Download Trending Skins?");
        addConfig(RegenConfig.CLIENT.downloadInteralSkins, "Download Doctor Who Skins?");

        //Client
        addConfig(RegenConfig.CLIENT.changeMySkin, "Change Skin?");
        addConfig(RegenConfig.CLIENT.changePerspective, "Change Perspective view?");
        addConfig(RegenConfig.CLIENT.renderTimelordHeadwear, "Render Head layers on Timelords?");

        //Common
        addConfig(RegenConfig.COMMON.loseRegensOnDeath, "Lose Regenerations on Death?");
        addConfig(RegenConfig.COMMON.fieryRegen, "Spawn Fire?");
        addConfig(RegenConfig.COMMON.mobsHaveRegens, "Mobs allowed Regenerations?");
        addConfig(RegenConfig.COMMON.sendRegenDeathMessages, "Send Regeneration death chat messages?");
        addConfig(RegenConfig.COMMON.postRegenerationDuration, "Post Regeneration Duration", 2);
        addConfig(RegenConfig.COMMON.postRegenerationLevel, "Post Regeneration Level", 2);
        addConfig(RegenConfig.COMMON.resetHunger, "Reset Hunger?",2);
        addConfig(RegenConfig.COMMON.resetOxygen, "Reset Air?",2);
        addConfig(RegenConfig.COMMON.regenFireImmune, "Fire immunity?");
        addConfig(RegenConfig.COMMON.regenerationKnocksbackPlayers, "Knocks back players?",2);
        addConfig(RegenConfig.COMMON.absorbtionLevel, "Absorption Level?",2);
        addConfig(RegenConfig.COMMON.gracePhaseLength, "Grace Period Length", 2);
        addConfig(RegenConfig.COMMON.criticalPhaseLength, "Critical Period Length",2);
        addConfig(RegenConfig.COMMON.criticalDamageChance, "Critical Damage Chance",2);
        addConfig(RegenConfig.COMMON.handGlowInterval, "Hand Glow Interval",2);
        addConfig(RegenConfig.COMMON.handGlowTriggerDelay, "Hand Trigger Delay",2);
        addConfig(RegenConfig.COMMON.regenerativeKillRange, "Regeneration hurt range",2);
        addConfig(RegenConfig.COMMON.regenerativeKnockback, "Knockback Radius",2);
        addConfig(RegenConfig.COMMON.regenKnockbackRange, "Knockback Range",2);
        addConfig(RegenConfig.COMMON.allowUpwardsMotion, "Upwards motion?",2);


        // === Skin Types ===
        for (PlayerUtil.SkinType value : PlayerUtil.SkinType.values()) {
            String valueName = value.name().toLowerCase();
            add("regeneration.skin_type." + valueName, capitalize(valueName));
        }

        //Tooltip
        add("item.regen.tooltip.trait", "Trait: %s");
        add("item.regen.tooltip.energy", "Energy: %s");


        add("button_tooltip.regen.reset_mojang", "Resets your Skin to Mojang Skin");
        add("button_tooltip.regen.previous_skin", "Previous Skin");
        add("button_tooltip.regen.next_skin", "Next Skin");
        add("button_tooltip.regen.save_skin", "Save Changes");
        add("button_tooltip.regen.open_folder", "Open Local Skin Folder");

        //Sounds
        add("regen.sound.regeneration", "Regenerating");
        add("regen.sound.regen_bubble", "Jar bubbles");
        add("regen.sound.hum", "Calming hums");
        add("regen.sound.fob_watch_dialogue", "Fob Watch Speaks");
        add("regen.sound.critical_stage", "Critical Condition");
        add("regen.sound.heart_beat", "Timelord Heartbeat");
        add("regen.sound.hand_glow", "Glowing Hands");
        add("regen.sound.fob_watch", "Ticking");
        add("regen.sound.regen_breath", "Regeneration exhale");
        add("regen.sound.alarm", "Regeneration Alarm");

        add("regen.sound.male_timelord.hurt", "Male Timelord Hurts");
        add("regen.sound.male_timelord.die", "Male Timelord Dies");
        add("regen.sound.male_timelord.trade_fail", "Male Timelord Trade Fail");
        add("regen.sound.male_timelord.trade_success", "Male Timelord Trade Success");
        add("regen.sound.male_timelord.scream", "Male Timelord Screams");

        add("regen.sound.female_timelord.hurt", "Female Timelord Hurts");
        add("regen.sound.female_timelord.die", "Female Timelord Dies");
        add("regen.sound.female_timelord.trade_fail", "Female Timelord Trade Fail");
        add("regen.sound.female_timelord.trade_success", "Female Timelord Trade Success");
        add("regen.sound.female_timelord.scream", "Female Timelord Screams");

        add("regen.sound.rifle", "Rifle fires");
        add("regen.sound.staser", "Staser Fires");


        // === Toasts ===
        add("toast.regen.regenerated", "You have regenerated!");
        add("toast.regen.regenerations_left", "%s Regenerations left");
        add("toast.regen.enter_critical", "You are going critical");
        add("toast.regen.enter_critical.sub", "%s minutes left...");

        // === GUI Elements ===
        add("gui.regen.primary", "Primary");
        add("gui.regen.secondary", "Secondary");
        add("gui.regen.undo", "Undo");
        add("gui.regen.close", "Close");
        add("gui.regen.input_color", "Hex");
        add("gui.regen.previous", "<");
        add("gui.regen.next", ">");
        add("gui.regen.default", "Default");
        add("gui.regen.open_folder", "Skin Directory");
        add("gui.regen.skin_choice", "Skin");
        add("gui.regen.save", "Save");
        add("gui.regen.next_incarnation", "Select next incarnation");
        add("gui.regen.reset_skin", "Reset");
        add("gui.regen.infinite_regenerations", "Infinite Regeneration Mode");
        add("gui.regen.remaining_regens.status", "Regenerations: %s");
        add("gui.regen.color_gui", "Colors");
        add("gui.regen.current_skin", "Select Next Incarnation");
        add("gui.regen.back", "Back");
        add("gui.regen.preferences", "Preferences");

        add(REntities.CYBER.get(), "Cyberlord");
    }

    public String capitalize(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        return firstLetter + text.substring(1);
    }

    public void addAdvancement(String advancement, String title, String description) {
        add(RegenAdvancementsProvider.RegenAdvancements.getTitleTranslation(advancement), title);
        add(RegenAdvancementsProvider.RegenAdvancements.getDescriptionTranslation(advancement), description);
    }

    public void addSoundScheme(IRegen.TimelordSound soundScheme, String translation) {
        add("gui.regen.sound_scheme." + soundScheme.name().toLowerCase(), translation);
    }

    public void addConfig(ForgeConfigSpec.ConfigValue waConfiguration, String message) {
        addConfig(waConfiguration, message, 1);
    }


    public void addConfig(ForgeConfigSpec.ConfigValue waConfiguration, String message, int index) {
        add("config.regen." + waConfiguration.getPath().get(index), message);
    }

    public void addTimelordtype(SpawnItem.Timelord type, String translation) {
        add("regen.timelord_type." + type.name().toLowerCase(), translation);
    }

    public void addTrait(TraitBase traitBase, String title, String description) {
        add("trait." + TraitRegistry.TRAITS_REGISTRY.getKey(traitBase).getPath() + ".title", title);
        add("trait." + TraitRegistry.TRAITS_REGISTRY.getKey(traitBase).getPath() + ".description", description);
    }

    public void add(ResourceKey<DamageType> damagetype, String message) {
        add("death.attack." + damagetype.location().getPath(), message);
        add("death.attack." + damagetype.location().getPath() + ".player", message);
    }
}
