package me.suff.mc.regen.common.advancement;

import com.google.gson.JsonObject;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class BaseTrigger extends AbstractCriterionTrigger<BaseTrigger.Instance> {
    private final ResourceLocation triggerID;

    public BaseTrigger(String parString) {
        this(new ResourceLocation(RConstants.MODID, parString));
    }

    public BaseTrigger(ResourceLocation parRL) {
        super();
        triggerID = parRL;
    }

    public void trigger(ServerPlayerEntity parPlayer) {
        this.trigger(parPlayer, Instance::test);
    }

    @Override
    public ResourceLocation getId() {
        return triggerID;
    }

    @Override
    protected Instance createInstance(JsonObject p_230241_1_, EntityPredicate.AndPredicate p_230241_2_, ConditionArrayParser p_230241_3_) {
        return new BaseTrigger.Instance(this.getId());
    }

    public static class Instance extends CriterionInstance {
        public Instance(ResourceLocation parID) {
            super(parID, EntityPredicate.AndPredicate.ANY);
        }

        public boolean test() {
            return true;
        }
    }
}