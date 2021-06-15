package me.suff.mc.regen.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.advancements.BaseTrigger;
import me.suff.mc.regen.common.advancements.TriggerManager;
import me.suff.mc.regen.handlers.RegenObjects;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.PositionTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdvancementCreation implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final List<Advancement> advancements = new ArrayList<>();
    private final DataGenerator generator;

    private Advancement lastAdvancement;

    public AdvancementCreation(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    /**
     * Performs this provider's action.
     */
    public void run(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();

        this.createAdvancement("first_regen", new ItemStack(RegenObjects.Items.FOB_WATCH.get()), new BaseTrigger.Instance(TriggerManager.FIRST_REGENERATION.getId()));
        this.createAdvancement("change_refusal", new ItemStack(RegenObjects.Blocks.HAND_JAR.get()), new BaseTrigger.Instance(TriggerManager.CHANGE_REFUSAL.getId()));
        this.createAdvancement("critical_period", new ItemStack(RegenObjects.Items.FOB_WATCH.get()), new BaseTrigger.Instance(TriggerManager.CRITICAL.getId()));
        this.createAdvancement("timelord_trade", new ItemStack(RegenObjects.Items.ROBES_CHEST.get()), new BaseTrigger.Instance(TriggerManager.TIMELORD_TRADE.getId()));

        this.createAdvancement(
                "gallifrey",
                new ItemStack(RegenObjects.Items.SEAL.get()),
                PositionTrigger.Instance.located(LocationPredicate.inBiome(RegenObjects.GallifreyBiomes.REDLANDS.get())));

        this.createAdvancement(
                "robes",
                new ItemStack(RegenObjects.Items.ROBES_CHEST.get()),
                InventoryChangeTrigger.Instance.hasItem(() -> RegenObjects.Items.ROBES_HEAD.get(), () -> RegenObjects.Items.ROBES_CHEST.get()));

        this.createAdvancement(
                "gallifreyan_weapon",
                new ItemStack(RegenObjects.Items.PISTOL.get()),
                InventoryChangeTrigger.Instance.hasItem(() -> RegenObjects.Items.PISTOL.get(), () -> RegenObjects.Items.RIFLE.get()));


        for (Advancement adv : advancements) {
            IDataProvider.save(GSON, cache, adv.deconstruct().serializeToJson(), getPath(path, adv));
        }

    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName() {
        return "Advancements";
    }

    public Advancement create(String name, String title, ItemStack display, ICriterionInstance... inst) {

        Advancement.Builder adv = Advancement.Builder.advancement()
                .display(
                        display.getItem(),
                        new TranslationTextComponent("advancements.regeneration.title." + title),
                        new TranslationTextComponent("advancements.regeneration.desc." + title),
                        new ResourceLocation("regeneration:textures/gui/roundel.png"),
                        FrameType.TASK,
                        true,
                        true,
                        false);
        int i = 0;

        for (ICriterionInstance in : inst) {
            adv = adv.addCriterion(i + "", in);
            i++;
        }

        if (lastAdvancement != null) {
            adv.parent(lastAdvancement);
        }

        return adv.build(new ResourceLocation(Regeneration.MODID, name));
    }

    public Advancement createAdvancement(String name, ItemStack display, ICriterionInstance inst) {
        Advancement advance = this.create(name, name, display, inst);
        advancements.add(advance);
        lastAdvancement = advance;
        return advance;
    }
}