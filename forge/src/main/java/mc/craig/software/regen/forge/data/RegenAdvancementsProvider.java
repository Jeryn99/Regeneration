package mc.craig.software.regen.forge.data;

import com.google.common.collect.ImmutableList;
import mc.craig.software.regen.common.advancement.BaseTrigger;
import mc.craig.software.regen.common.advancement.TriggerManager;
import mc.craig.software.regen.common.objects.RBlocks;
import mc.craig.software.regen.common.objects.RItems;
import mc.craig.software.regen.util.constants.RConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RegenAdvancementsProvider extends AdvancementProvider {

    public RegenAdvancementsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, ImmutableList.of(new RegenAdvancements()));
    }

    public static class RegenAdvancements implements AdvancementGenerator {
        public static String getTitleTranslation(String name) {
            return "advancements." + name + ".title";
        }

        public static String getDescriptionTranslation(String name) {
            return "advancements." + name + ".description";
        }


        @Override
        public void generate(HolderLookup.Provider arg, Consumer<AdvancementHolder> advancementConsumer, ExistingFileHelper existingFileHelper) {
            Advancement rootAdvancement = Advancement.Builder.advancement().display(RItems.FOB.get(), Component.translatable(getTitleTranslation("fob_watch")), Component.translatable(getDescriptionTranslation("fob_watch")), new ResourceLocation(RConstants.MODID, "textures/block/zero_roundel_half.png"), FrameType.GOAL, false, true, false).addCriterion("obtained_fob_watch", InventoryChangeTrigger.TriggerInstance.hasItems(RItems.FOB.get())).save(advancementConsumer, "regen/root");

            Advancement firstRegeneration = Advancement.Builder.advancement().parent(rootAdvancement).display(Blocks.PLAYER_HEAD, Component.translatable(getTitleTranslation("first_regeneration")), Component.translatable(getDescriptionTranslation("first_regeneration")), null, FrameType.TASK, false, true, false).addCriterion("regenerated", new BaseTrigger.Instance(TriggerManager.FIRST_REGENERATION.getId())).save(advancementConsumer, "regen/regenerated");
            Advancement changeRefusal = Advancement.Builder.advancement().parent(rootAdvancement).display(RBlocks.BIO_CONTAINER.get(), Component.translatable(getTitleTranslation("change_refusal")), Component.translatable(getDescriptionTranslation("change_refusal")), null, FrameType.TASK, false, true, false).addCriterion("change_refusal", new BaseTrigger.Instance(TriggerManager.CHANGE_REFUSAL.getId())).save(advancementConsumer, "regen/change_refusal");

            Advancement handCut = Advancement.Builder.advancement().parent(changeRefusal).display(RItems.HAND.get(), Component.translatable(getTitleTranslation("severed_hand")), Component.translatable(getDescriptionTranslation("severed_hand")), null, FrameType.TASK, false, true, false).addCriterion("severed_hand", new BaseTrigger.Instance(TriggerManager.HAND_CUT.getId())).save(advancementConsumer, "regen/severed_hand");
            Advancement zeroStatus = Advancement.Builder.advancement().parent(handCut).display(RBlocks.ZERO_ROOM_FULL.get(), Component.translatable(getTitleTranslation("zero_status")), Component.translatable(getDescriptionTranslation("zero_status")), null, FrameType.TASK, false, true, false).addCriterion("zero_status", new BaseTrigger.Instance(TriggerManager.ZERO_ROOM.getId())).save(advancementConsumer, "regen/zero_status");
            Advancement criticalGrace = Advancement.Builder.advancement().parent(changeRefusal).display(RBlocks.ZERO_ROOM_FULL.get(), Component.translatable(getTitleTranslation("critical_grace")), Component.translatable(getDescriptionTranslation("critical_grace")), null, FrameType.TASK, false, true, false).addCriterion("critical_grace", new BaseTrigger.Instance(TriggerManager.CRITICAL.getId())).save(advancementConsumer, "regen/critical_grace");

            Advancement timelordTrade = Advancement.Builder.advancement().parent(rootAdvancement).display(RItems.SPAWN_ITEM.get(), Component.translatable(getTitleTranslation("timelord_trade")), Component.translatable(getDescriptionTranslation("timelord_trade")), null, FrameType.TASK, false, true, false).addCriterion("timelord_trade", new BaseTrigger.Instance(TriggerManager.TIMELORD_TRADE.getId())).save(advancementConsumer, "regen/timelord_trade");
            Advancement councilClothing = Advancement.Builder.advancement().parent(timelordTrade).display(RItems.F_ROBES_CHEST.get(), Component.translatable(getTitleTranslation("council_clothing")), Component.translatable(getDescriptionTranslation("council_clothing")), null, FrameType.TASK, false, true, false).addCriterion("council_clothing", new BaseTrigger.Instance(TriggerManager.COUNCIL.getId())).save(advancementConsumer, "regen/council_clothing");
            Advancement readyForWar = Advancement.Builder.advancement().parent(timelordTrade).display(RItems.RIFLE.get(), Component.translatable(getTitleTranslation("ready_for_war")), Component.translatable(getDescriptionTranslation("ready_for_war")), null, FrameType.TASK, false, true, false).addCriterion("ready_for_war", InventoryChangeTrigger.TriggerInstance.hasItems(() -> RItems.PISTOL.get(), () -> RItems.RIFLE.get())).save(advancementConsumer, "regen/ready_for_war");
            Advancement gallifreyanSoldier = Advancement.Builder.advancement().parent(timelordTrade).display(RItems.GUARD_CHEST.get(), Component.translatable(getTitleTranslation("gallifreyan_soldier")), Component.translatable(getDescriptionTranslation("gallifreyan_soldier")), null, FrameType.TASK, false, true, false).addCriterion("gallifreyan_soldier", InventoryChangeTrigger.TriggerInstance.hasItems(() -> RItems.GUARD_HELMET.get(), () -> RItems.GUARD_CHEST.get(), () -> RItems.GUARD_FEET.get(), () -> RItems.GUARD_LEGS.get())).save(advancementConsumer, "regen/gallifreyan_soldier");

        }
    }


}