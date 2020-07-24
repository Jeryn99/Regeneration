package me.swirtzly.data;

import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.traits.TraitManager;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class LangProvider extends LanguageProvider {

    public LangProvider(DataGenerator gen) {
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

        add(TraitManager.DNA_NIGHTVISION.getLangKey(), "Might-vision Genes!");
        add(TraitManager.DNA_NIGHTVISION.getLocalDesc(), "You can see better in dark places!");

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

        // === Tardis Compat ===
        add("tardis.protocol.arch", "Toggle Chameleon Arch");
        add("ars.piece.tardis.zero_room", "Regeneration: Zero Room");
        add("message.regeneration.arch_removed", "Chameleon Arch Retracted!");
        add("message.regeneration.arch_placed", "Chameleon Arch Deployed!");
        add("message.regeneration.arch_system_dead", "Biocomponent Upgrade is not in a functional state or is not installed...");
        add("message.regeneration.arch_no_space", "There is not enough space to deploy!");
        add("regeneration.gui.previous", "<");
        add("regeneration.gui.next", ">");


    }
}
