package me.suff.mc.regen.common.advancement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class BaseTrigger implements ICriterionTrigger< BaseTrigger.Instance > {
    private final ResourceLocation RL;
    private final Map< PlayerAdvancements, BaseTrigger.Listeners > listeners = Maps.newHashMap();

    /**
     * Instantiates a new custom trigger.
     *
     * @param parString the par string
     */
    public BaseTrigger(String parString) {
        super();
        RL = new ResourceLocation(parString);
    }

    /**
     * Instantiates a new custom trigger.
     *
     * @param parRL the par RL
     */
    public BaseTrigger(ResourceLocation parRL) {
        super();
        RL = parRL;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#getId()
     */
    @Override
    public ResourceLocation getId() {
        return RL;
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#addListener(net.minecraft.advancements.PlayerAdvancements, net.minecraft.advancements.ICriterionTrigger.Listener)
     */
    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener< BaseTrigger.Instance > listener) {
        BaseTrigger.Listeners myCustomTrigger$listeners = listeners.get(playerAdvancementsIn);

        if (myCustomTrigger$listeners == null) {
            myCustomTrigger$listeners = new BaseTrigger.Listeners(playerAdvancementsIn);
            listeners.put(playerAdvancementsIn, myCustomTrigger$listeners);
        }

        myCustomTrigger$listeners.add(listener);
    }

    /*
     * (non-Javadoc)
     * @see net.minecraft.advancements.ICriterionTrigger#removeListener(net.minecraft.advancements.PlayerAdvancements, net.minecraft.advancements.ICriterionTrigger.Listener)
     */
    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener< BaseTrigger.Instance > listener) {
        BaseTrigger.Listeners tameanimaltrigger$listeners = listeners.get(playerAdvancementsIn);

        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.remove(listener);

            if (tameanimaltrigger$listeners.isEmpty()) {
                listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements playerAdvancementsIn) {
        listeners.remove(playerAdvancementsIn);
    }

    @Override
    public Instance createInstance(JsonObject p_230307_1_, ConditionArrayParser p_230307_2_) {
        return new BaseTrigger.Instance(getId());
    }

    /**
     * Trigger.
     *
     * @param parPlayer the player
     */
    public void trigger(ServerPlayerEntity parPlayer) {
        BaseTrigger.Listeners tameanimaltrigger$listeners = listeners.get(parPlayer.getAdvancements());

        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.trigger(parPlayer);
        }
    }

    public static class Instance implements ICriterionInstance {

        private final ResourceLocation id;

        /**
         * Instantiates a new instance.
         *
         * @param parRL the par RL
         */
        public Instance(ResourceLocation parRL) {
            this.id = parRL;
        }

        /**
         * Test.
         *
         * @return true, if successful
         */
        public boolean test() {
            return true;
        }

        @Override
        public ResourceLocation getCriterion() {
            return id;
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer p_230240_1_) {
            return new JsonObject();
        }
    }

    static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set< ICriterionTrigger.Listener< BaseTrigger.Instance > > listeners = Sets.newHashSet();

        /**
         * Instantiates a new listeners.
         *
         * @param playerAdvancementsIn the player advancements in
         */
        public Listeners(PlayerAdvancements playerAdvancementsIn) {
            playerAdvancements = playerAdvancementsIn;
        }

        /**
         * Checks if is empty.
         *
         * @return true, if is empty
         */
        public boolean isEmpty() {
            return listeners.isEmpty();
        }

        /**
         * Adds the listener.
         *
         * @param listener the listener
         */
        public void add(ICriterionTrigger.Listener< BaseTrigger.Instance > listener) {
            listeners.add(listener);
        }

        /**
         * Removes the listener.
         *
         * @param listener the listener
         */
        public void remove(ICriterionTrigger.Listener< BaseTrigger.Instance > listener) {
            listeners.remove(listener);
        }

        /**
         * Trigger.
         *
         * @param player the player
         */
        public void trigger(ServerPlayerEntity player) {
            ArrayList< ICriterionTrigger.Listener< BaseTrigger.Instance > > list = null;

            for (ICriterionTrigger.Listener< BaseTrigger.Instance > listener : listeners) {
                if (listener.getTriggerInstance().test()) {
                    if (list == null) {
                        list = Lists.newArrayList();
                    }

                    list.add(listener);
                }
            }

            if (list != null) {
                for (ICriterionTrigger.Listener< BaseTrigger.Instance > listener1 : list) {
                    listener1.run(playerAdvancements);
                }
            }
        }
    }
}