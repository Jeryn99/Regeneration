package craig.software.mc.regen.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import craig.software.mc.regen.common.advancement.BaseTrigger;
import craig.software.mc.regen.common.advancement.TriggerManager;
import craig.software.mc.regen.common.objects.RBlocks;
import craig.software.mc.regen.common.objects.RItems;
import craig.software.mc.regen.util.RConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RegenAdvancements implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final List<Advancement> advancements = new ArrayList<>();
    private final DataGenerator generator;

    public RegenAdvancements(DataGenerator generatorIn) {
        this.generator = generatorIn;
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }


    @Override
    public void run(@NotNull CachedOutput hashCache) throws IOException {
        Path path = this.generator.getOutputFolder();
        TriggerManager.init();
        Advancement watchIsMe = this.createAdvancement("watch_is_me", new ItemStack(RItems.FOB.get()), InventoryChangeTrigger.TriggerInstance.hasItems(RItems.FOB.get()), null, FrameType.GOAL);
        Advancement firstRegeneration = this.createAdvancement("first_regen", new ItemStack(Blocks.PLAYER_HEAD), new BaseTrigger.Instance(TriggerManager.FIRST_REGENERATION.getId()), watchIsMe, FrameType.CHALLENGE);

        //Refusal Path
        Advancement changeRefuse = this.createAdvancement("change_refusal", new ItemStack(RBlocks.BIO_CONTAINER.get()), new BaseTrigger.Instance(TriggerManager.CHANGE_REFUSAL.getId()), firstRegeneration);
        this.createAdvancement("critical_period", new ItemStack(Items.CLOCK), new BaseTrigger.Instance(TriggerManager.CRITICAL.getId()), changeRefuse);

        //Restoration Path
        Advancement cutHand = this.createAdvancement("hand_cut", new ItemStack(RItems.HAND.get()), new BaseTrigger.Instance(TriggerManager.HAND_CUT.getId()), changeRefuse);
        this.createAdvancement("zero_room", new ItemStack(RBlocks.ZERO_ROUNDEL.get()), new BaseTrigger.Instance(TriggerManager.ZERO_ROOM.getId()), cutHand);

        //  Advancement gallifrey = this.createAdvancement("gallifrey", new ItemStack(RBlocks.AZBANTIUM.get()), ChangeDimensionTrigger.Instance.changedDimensionTo(RConstants.GALLIFREY), watchIsMe, FrameType.CHALLENGE);
        Advancement trade = this.createAdvancement("timelord_trade", new ItemStack(RItems.SPAWN_ITEM.get()), new BaseTrigger.Instance(TriggerManager.TIMELORD_TRADE.getId()), watchIsMe);
        Advancement guard = this.createAdvancement("guard", new ItemStack(RItems.GUARD_HELMET.get()), InventoryChangeTrigger.TriggerInstance.hasItems(() -> RItems.GUARD_HELMET.get(), () -> RItems.GUARD_CHEST.get(), () -> RItems.GUARD_FEET.get(), () -> RItems.GUARD_LEGS.get()), trade);
        this.createAdvancement("gallifreyan_weapon", new ItemStack(RItems.PISTOL.get()), InventoryChangeTrigger.TriggerInstance.hasItems(() -> RItems.PISTOL.get(), () -> RItems.RIFLE.get()), guard);
        Advancement council = this.createAdvancement("council", new ItemStack(RItems.M_ROBES_HEAD.get()), new BaseTrigger.Instance(TriggerManager.COUNCIL.getId()), trade);

        for (Advancement adv : advancements) {
            DataProvider.saveStable(hashCache, adv.deconstruct().serializeToJson(), getPath(path, adv));
        }
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public @NotNull String getName() {
        return "Advancements";
    }

    public Advancement create(String name, String title, ItemStack display, Advancement parent, FrameType frameType, CriterionTriggerInstance... inst) {

        Advancement.Builder adv = Advancement.Builder.advancement()
                .display(
                        display.getItem(),
                        Component.translatable("advancements.regen.title." + title),
                        Component.translatable("advancements.regen.desc." + title),
                        new ResourceLocation("regen:textures/block/zero_roundel_half.png"),
                        frameType,
                        true,
                        true,
                        false);
        int i = 0;

        for (CriterionTriggerInstance in : inst) {
            adv = adv.addCriterion(i + "", in);
            i++;
        }

        if (parent != null) {
            adv.parent(parent);
        }

        return adv.build(new ResourceLocation(RConstants.MODID, name));
    }

    public Advancement createAdvancement(String name, ItemStack display, CriterionTriggerInstance inst, Advancement parent) {
        return createAdvancement(name, display, inst, parent, FrameType.TASK);
    }

    public Advancement createAdvancement(String name, ItemStack display, CriterionTriggerInstance inst, Advancement parent, FrameType frameType) {
        Advancement advance = this.create(name, name, display, parent, frameType, inst);
        advancements.add(advance);
        return advance;
    }
}