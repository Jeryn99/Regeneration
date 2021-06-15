package me.suff.mc.regen.data;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.traits.TraitManager;
import me.suff.mc.regen.common.types.RegenTypes;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LangCreation extends LanguageProvider {

    public LangCreation(DataGenerator gen) {
        super(gen, Regeneration.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

        // === Items ===

        //Loot
        this.add(RegenObjects.Items.SEAL.get(), "Seal of Rassilon");
        this.add(RegenObjects.Items.DIAL.get(), "Confession Dial");
        this.add(RegenObjects.Items.ARCH_PART.get(), "Biological Upgrade");
        this.add(RegenObjects.Items.FOB_WATCH.get(), "Fob watch");
        this.add(RegenObjects.Items.HAND.get(), "%s's Chopped off hand");
        this.add(RegenObjects.Items.ROBES_LEGS.get(), "Council Lower Robes");

        //Weapons
        this.add(RegenObjects.Items.PISTOL.get(), "Gallifreyan Staser");
        this.add(RegenObjects.Items.RIFLE.get(), "Gallifreyan Rifle");

        //Ores
        this.add(RegenObjects.Items.GAL_INGOT.get(), "Unknown ingot");

        //Wearables
        this.add(RegenObjects.Items.GUARD_HEAD.get(), "Gallifreyan Helmet");
        this.add(RegenObjects.Items.GUARD_CHEST.get(), "Gallifreyan Chestplate");
        this.add(RegenObjects.Items.GUARD_LEGGINGS.get(), "Gallifreyan leggings");
        this.add(RegenObjects.Items.GUARD_FEET.get(), "Gallifreyan Boots");
        this.add(RegenObjects.Items.ROBES_CHEST.get(), "Council Robes");
        this.add(RegenObjects.Items.ROBES_HEAD.get(), "Council Hat");

        // === Biomes ===
        this.add(RegenObjects.GallifreyBiomes.WASTELANDS.get(), "Galliffreyan Wastelands");
        this.add(RegenObjects.GallifreyBiomes.GALLIFREY_MOUNTAINS.get(), "Galliffreyan Redlands Mountains");
        this.add(RegenObjects.GallifreyBiomes.REDLANDS.get(), "Galliffreyan Redlands");
        this.add(RegenObjects.GallifreyBiomes.REDLANDS_FOREST.get(), "Galliffreyan Redlands Forest");
        this.add(RegenObjects.GallifreyBiomes.GALLIFREYAN_OCEAN.get(), "Galliffreyan Ocean");
        this.add(RegenObjects.GallifreyBiomes.GOLDEN_FIELDS.get(), "Galliffreyan Golden Fields");
        this.add(RegenObjects.GallifreyBiomes.GALLIFREYAN_RIVER.get(), "Galliffreyan River");
        this.add(RegenObjects.GallifreyBiomes.WASTELANDS_MOUNTAINS.get(), "Galliffreyan Wastelands Mountains");
        this.add(RegenObjects.GallifreyBiomes.SNOWY.get(), "Galliffreyan Snowy Redlands");

        // === Blocks ===
        this.add(RegenObjects.Blocks.GAL_ORE.get(), "Unknown Ore");
        this.add(RegenObjects.Blocks.ZERO_ROOM.get(), "Zero Room Roundel");
        this.add(RegenObjects.Blocks.ZERO_ROOM_TWO.get(), "Zero Room Roundel");
        this.add(RegenObjects.Blocks.HAND_JAR.get(), "Bio-Container");
        this.add(RegenObjects.Blocks.ARCH.get(), "Chameleon Arch");

        // === Entities ===
        this.add(RegenObjects.EntityEntries.TIMELORD.get(), "Timelord");
        this.add(RegenObjects.EntityEntries.LASER.get(), "Laser");
        this.add(RegenObjects.EntityEntries.ITEM_OVERRIDE_ENTITY_TYPE.get(), "I am something");


        // === Traits ===
        add(TraitManager.DNA_BORING.getLangKey(), "Boring Genes!");
        add(TraitManager.DNA_BORING.getLocalDesc(), "Nothing Special, Just standard Human");

        add(TraitManager.DNA_ATHLETE.getLangKey(), "Athletic Genes!");
        add(TraitManager.DNA_ATHLETE.getLocalDesc(), "You can run faster and jump higher!");

        add(TraitManager.DNA_DUMB.getLangKey(), "Idiotic Genes!");
        add(TraitManager.DNA_DUMB.getLocalDesc(), "You won't be picking up as much XP as usual...");

        add(TraitManager.DNA_REPEL_ARROW.getLangKey(), "Anti=Arrow Genes!");
        add(TraitManager.DNA_REPEL_ARROW.getLocalDesc(), "Arrows have no affect on you!");

        add(TraitManager.DNA_FIRE_RESISTANT.getLangKey(), "Fire-resistant Genes!");
        add(TraitManager.DNA_FIRE_RESISTANT.getLocalDesc(), "You're immune to all fire damage!");

        add(TraitManager.DNA_HUNGER.getLangKey(), "Hunger Genes!");
        add(TraitManager.DNA_HUNGER.getLocalDesc(), "Your stomach is going to feel hungry more...");

        add(TraitManager.DNA_WALLCLIMB.getLangKey(), "Anti-Grav Genes!");
        add(TraitManager.DNA_WALLCLIMB.getLocalDesc(), "You can walk up walls!");

        add(TraitManager.DNA_TOUGH.getLangKey(), "Strong Genes!");
        add(TraitManager.DNA_TOUGH.getLocalDesc(), "Your punches do more damage!");

        add(TraitManager.DNA_SCARED_OF_WATER.getLangKey(), "Aquaphobic Genes!");
        add(TraitManager.DNA_SCARED_OF_WATER.getLocalDesc(), "Any kind of water makes you unwell...");

        add(TraitManager.DNA_SWIMMER.getLangKey(), "Swimmer Genes!");
        add(TraitManager.DNA_SWIMMER.getLocalDesc(), "You don't need to hold your breath anymore!");


        // === GUI Elements ===
        add("regeneration.gui.red", "Red: ");
        add("regeneration.gui.green", "Green: ");
        add("regeneration.gui.blue", "Blue: ");
        add("regeneration.gui.primary", "Primary");
        add("regeneration.gui.secondary", "Secondary");
        add("regeneration.gui.undo", "Undo");
        add("regeneration.gui.regen_type", "Type: %s");
        add("regeneration.gui.close", "Close");
        add("regeneration.gui.input_color", "Hex");
        add("regeneration.gui.previous", "<");
        add("regeneration.gui.next", ">");
        add("regeneration.gui.default", "Default");
        add("regeneration.gui.open_folder", "Skin Dir");
        add("regeneration.gui.skin_choice", "Skin Settings");
        add("regeneration.gui.save", "Save");
        add("regeneration.gui.next_incarnation", "Select next incarnation");
        add("regeneration.gui.reset_skin", "Reset Skin");
        add("regeneration.gui.infinite_regenerations", "Infinite Regeneration Mode");
        add("regeneration.gui.remaining_regens.status", "Remaining Regenerations,");
        add("regeneration.gui.color_gui", "Color selection");
        add("regeneration.gui.current_skin", "Select next Incarnation");
        add("regeneration.gui.skintype", "Preference: %s");
        add("regeneration.gui.back", "Back");
        add("regeneration.gui.preferences", "Preferences");

        for (PlayerUtil.EnumChoices value : PlayerUtil.EnumChoices.values()) {
            add("regeneration.skin_type." + value.name().toLowerCase(), grammerNazi(value.name().toLowerCase()));
        }

        // === Tardis Compat ===
        add("tardis.protocol.arch", "Toggle Chameleon Arch");
        add("ars.piece.tardis.zero_room", "Regeneration: Zero Room");
        add("message.regeneration.arch_removed", "Chameleon Arch Retracted!");
        add("message.regeneration.arch_placed", "Chameleon Arch Deployed!");
        add("message.regeneration.arch_system_dead", "Biocomponent Upgrade is not in a functional state or is not installed...");
        add("message.regeneration.arch_no_space", "There is not enough space to deploy!");
        add("message.regeneration.transferred_regens", "You have given %s %s Regenerations!");

        // === NBT ===
        add("nbt.regeneration.item.lindos", "Lindos Energy: %s");
        add("nbt.regeneration.item.stored_regens", "Stored Regenerations: %s");
        add("nbt.regeneration.created", "Created on: %s");

        // === Regen ===
        add(RegenTypes.HARTNELL.create().getTranslation().getKey(), "Hartnell");
        add(RegenTypes.FIERY.create().getTranslation().getKey(), "Fiery");

        // === Item Groups ===
        add("itemGroup.regeneration", grammerNazi(Regeneration.MODID));
        add("itemGroup.regeneration_clothes", grammerNazi(Regeneration.MODID + " clothes"));

        // === Command ===
        add("argument.regeneration.invalid", "This trait is invalid!");

        // === Subtitles ===
        add("regeneration.subtitles.fob_watch", "Fob Watch opening");
        add("regeneration.subtitles.regeneration", "Regeneration energy");
        add("regeneration.subtitles.hand_glow", "Glowing hands");
        add("regeneration.subtitles.heart_beat", "Heartbeat");
        add("regeneration.subtitles.critical_stage", "Critical grace stage");
        add("regeneration.subtitles.fob_watch_dialogue", "Fob Watch Dialogue");
        add("regeneration.subtitles.regeneration_breath", "Regeneration Breath");
        add("regeneration.subtitles.hum", "Regeneration Energy Hums");
        add("regeneration.subtitles.rifle", "Energy Rifle Discharges");
        add("regeneration.subtitles.staser", "Energy Staser Discharges");

        // === Keybinds ===
        add("regeneration.keybinds.regenerate_forced", "Force Regen/Sync to hand!");
        add("regeneration.keybinds.regenerate", "Regenerate");

        // === Toasts ===
        add("regeneration.toast.regenerated", "You have regenerated!");
        add("regeneration.toast.regenerations_left", "%s regenerations left");
        add("regeneration.toast.enter_critical", "You are going critical");
        add("regeneration.toast.enter_critical.sub", "%s minutes left...");
        add("regeneration.toast.timelord", "You are now a Time lord!");
        add("regeneration.toast.to_use", "You have %s lives now!");


        // === Damages Sources ===
        add("regeneration.damagesrc.regen_energy", "%s was blasted by Regeneration Energy!");
        add("regeneration.damagesrc.regen_heal", "%s died by donating too much Regeneration energy...");
        add("regeneration.damagesrc.regen_crit", "%s died from holding in their regeneration for too long");
        add("regeneration.damagesrc.theft", "%s had their body stolen!");
        add("regeneration.damagesrc.lindos", "%s consumed lindos hormones! They are reborn!");
        add("regeneration.damagesrc.regen_killed", "%s was killed mid-regeneration....");

        // === "advancements ===
        add("advancements.regeneration.title.change_refusal", "I. WILL. NOT. CHANGE!");
        add("advancements.regeneration.desc.change_refusal", "Punch a block to delay your regeneration!");
        add("advancements.regeneration.title.critical_period", "Your song is ending...");
        add("advancements.regeneration.desc.critical_period", "Refuse to Regenerate up till the point of near death...");
        add("advancements.regeneration.title.first_regen", "Change my dear...");
        add("advancements.regeneration.desc.first_regen", "Regenerate for the First time!");
        add("advancements.regeneration.title.robes", "Member of the High Council");
        add("advancements.regeneration.desc.robes", "Wear Gallifreyan Robes");
        add("advancements.regeneration.title.gallifrey", "Constellation of kasterborous");
        add("advancements.regeneration.desc.gallifrey", "Step foot on Gallifrey");
        add("advancements.regeneration.title.gallifreyan_weapon", "There's no Stun Setting...");
        add("advancements.regeneration.desc.gallifreyan_weapon", "Hold a Gallifreyan Weapon");
        add("advancements.regeneration.title.timelord_trade", "Galactic Fortune");
        add("advancements.regeneration.desc.timelord_trade", "Trade with a Timelord");

        // === Messages ===
        add("regeneration.messages.now_timelord", "You are now a Time lord!");
        add("regeneration.messages.gained_regens", "Recieved Regenerations!");
        add("regeneration.messages.new_skin", "Your skin will change next Regeneration!");
        add("regeneration.messages.transfer.success", "You've transferred one of your regenerations into the watch");
        add("regeneration.messages.transfer.full_watch", "You can't store anymore regenerations in this watch!");
        add("regeneration.messages.transfer.empty_watch", "This watch is empty!");
        add("regeneration.messages.transfer.max_regens", "You already have the maximum number of regenerations");
        add("regeneration.messages.transfer.no_regens", "You don't have any regenerations left to transfer");
        add("regeneration.messages.jar_amount", "This container currently contains %s Lindos!");
        add("regeneration.messages.jar_inuse", "This is in use!");
        add("regeneration.messages.regen_chat_message", "%s is regenerating...");
        add("regeneration.messages.warning.grace", "You are in a state of grace, press %s to regenerate!");
        add("regeneration.messages.warning.grace_critical", "You are near death, press %s to regenerate!");
        add("regeneration.messages.regen_delayed", "Regeneration delayed!");
        add("regeneration.messages.regen_warning", "Punch a block to delay your regeneration further");
        add("regeneration.messages.jar", "You have gained a Regeneration from harvested lindos!");
        add("regeneration.messages.jar_not_enough", "There is not enough Lindos in this jar! [100 Lindos = 1 free Regeneration]");
        add("regeneration.messages.jar_no_break", "You cannot break this Jar while it has %s Lindos Energy!");
        add("regeneration.messages.not_alive", "You cannot transfer regens in this state!");
        add("regeneration.messages.item_taken_regens", "You have gained %s regenerations from %s");
        add("regeneration.messages.cannot_use", "You cannot use this right now!");
        add("regeneration.messages.healed", "You have given %s some of your Regeneration Energy!");
        add("regeneration.messages.reduced_dmg", "Reduced Damage due to Post Regen State!");
        add("regeneration.messages.fall_dmg", "Fall damage reduced due to post regen state! Although you're gonna feel sick..");
        add("regeneration.messages.post_ended", "Your Regeneration process is now entirely completed!");
        add("regeneration.messages.fast_forward_cmd_fail", "Nothing to fast forward to.");
        add("regeneration.messages.cant_glow", "You cannot activate a glowing hand without being in a grace period");

        // === MISC ===
        add("regeneration.messages.info_trait", "Trait:");
        add("regeneration.messages.regen_fail", "This item either contains no Regenerations and you have no regenerations to store!");
        add("regeneration.messages.item_invalid", "This item either contains no Regenerations and you have no regenerations to store!");
        add("regeneration.messages.stored_item", "%s Regenerations have been stored into %s");

    }

    public String grammerNazi(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        return firstLetter + text.substring(1);
    }
}
