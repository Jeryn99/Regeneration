package me.suff.mc.regen.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.suff.mc.regen.common.advancement.BaseTrigger;
import me.suff.mc.regen.common.advancement.TriggerManager;
import me.suff.mc.regen.common.objects.RBlocks;
import me.suff.mc.regen.common.objects.RItems;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AdvancementCreation implements IDataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final List< Advancement > advancements = new ArrayList<>();
    private final DataGenerator generator;

    private Advancement lastAdvancement;

    public AdvancementCreation(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }


    @Override
    public void run(DirectoryCache p_200398_1_) throws IOException {
        Path path = this.generator.getOutputFolder();
        TriggerManager.init();
        this.createAdvancement("first_regen", new ItemStack(RItems.FOB.get()), new BaseTrigger.Instance(TriggerManager.FIRST_REGENERATION.getId()));
        this.createAdvancement("change_refusal", new ItemStack(RBlocks.BIO_CONTAINER.get()), new BaseTrigger.Instance(TriggerManager.CHANGE_REFUSAL.getId()));
        this.createAdvancement("critical_period", new ItemStack(Items.CLOCK), new BaseTrigger.Instance(TriggerManager.CRITICAL.getId()));
        this.createAdvancement("timelord_trade", new ItemStack(RItems.SPAWN_ITEM.get()), new BaseTrigger.Instance(TriggerManager.TIMELORD_TRADE.getId()));
        this.createAdvancement("gallifreyan_weapon", new ItemStack(RItems.PISTOL.get()), InventoryChangeTrigger.Instance.hasItems(() -> RItems.PISTOL.get(), () -> RItems.RIFLE.get()));
        this.createAdvancement("hand_cut", new ItemStack(RItems.HAND.get()), new BaseTrigger.Instance(TriggerManager.HAND_CUT.getId()));
        this.createAdvancement("zero_room", new ItemStack(RBlocks.ZERO_ROUNDEL.get()), new BaseTrigger.Instance(TriggerManager.ZERO_ROOM.getId()));


        for (Advancement adv : advancements) {
            IDataProvider.save(GSON, p_200398_1_, adv.deconstruct().serializeToJson(), getPath(path, adv));
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
                        new TranslationTextComponent("advancements.regen.title." + title),
                        new TranslationTextComponent("advancements.regen.desc." + title),
                        new ResourceLocation("regen:textures/block/zero_roundel_half.png"),
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

        return adv.build(new ResourceLocation(RConstants.MODID, name));
    }

    public Advancement createAdvancement(String name, ItemStack display, ICriterionInstance inst) {
        Advancement advance = this.create(name, name, display, inst);
        advancements.add(advance);
        lastAdvancement = advance;
        return advance;
    }
}