package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.capability.IRegen;
import me.suff.mc.regen.common.capability.RegenCap;
import me.suff.mc.regen.common.traits.negative.HungerTrait;
import me.suff.mc.regen.common.traits.negative.HydrophobicTrait;
import me.suff.mc.regen.common.traits.positive.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Suffril on 24/01/2019.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TraitManager {

    private static final ArrayList<IDna> DNA_LIST = new ArrayList<>();
    public static IDna DNA_ATHLETE = new AthleteTrait();
    public static IDna DNA_BORING = new BaseTrait("boring");
    public static IDna DNA_DUMB = new BaseTrait("dumb");
    public static IDna DNA_TOUGH = new ToughTrait();
    public static IDna DNA_SWIMMER = new BreathingTrait();
    public static IDna DNA_SCARED_OF_WATER = new HydrophobicTrait();
    public static IDna DNA_FIRE_RESISTANT = new FireResistantTrait();
    public static IDna DNA_HUNGER = new HungerTrait();
    public static IDna DNA_WALLCLIMB = new WallClimbingTrait();
    public static IDna DNA_REPEL_ARROW = new BaseTrait("repel_arrow");
    public static HashMap<ResourceLocation, IDna> DNA_ENTRIES = new HashMap<>();

    public static void init() {
        register(DNA_ATHLETE);
        register(DNA_BORING);
        register(DNA_DUMB);
        register(DNA_TOUGH);
        register(DNA_SWIMMER);
        register(DNA_SCARED_OF_WATER);
        register(DNA_FIRE_RESISTANT);
        register(DNA_HUNGER);
        register(DNA_WALLCLIMB);
        register(DNA_REPEL_ARROW);
    }

    public static void register(IDna dna) {
        DNA_ENTRIES.put(dna.getRegistryName(), dna);
        DNA_LIST.add(dna);
    }

    public static IDna getDnaEntry(ResourceLocation resourceLocation) {
        if (DNA_ENTRIES.containsKey(resourceLocation)) {
            return DNA_ENTRIES.get(resourceLocation);
        }
        return DNA_BORING;
    }

    public static IDna getRandomDna(Random random) {
        return DNA_LIST.get(random.nextInt(DNA_LIST.size()));
    }

    @SubscribeEvent
    public static void onXpPickup(PlayerPickupXpEvent e) {
        RegenCap.get(e.getEntityPlayer()).ifPresent((data) -> {
            IDna dna = TraitManager.getDnaEntry(data.getTrait());
            if (dna.getRegistryName().equals(TraitManager.DNA_DUMB.getRegistryName()) && data.isDnaActive()) {
                e.getOrb().value *= 0.5;
            }
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            RegenCap.get(player).ifPresent((data) -> {
                if (player.level.isClientSide) return;
                if (data.isDnaActive() && data.getTrait().equals(DNA_ATHLETE.getRegistryName())) {
                    player.getDeltaMovement().add(0, 0.1, 0);
                    player.hurtMarked = true;
                }
            });
        }
    }

    @SubscribeEvent
    public static void onArrow(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        Entity attacked = event.getEntity();
        if (source != null && attacked != null && source.getDirectEntity() != null) {
            if (attacked instanceof PlayerEntity && source.getDirectEntity() instanceof AbstractArrowEntity) {
                if (!attacked.level.isClientSide) {
                    PlayerEntity player = (PlayerEntity) attacked;
                    boolean flag = RegenCap.get(player).orElse(null).getTrait().toString().equals(DNA_REPEL_ARROW.getRegistryName().toString());
                    event.setCanceled(flag);
                }
            }
        }
    }

    public static abstract class IDna {

        private final ResourceLocation resourceLocation;
        public String localName;
        public String localDesc;

        public IDna(String name) {
            resourceLocation = new ResourceLocation(Regeneration.MODID, name);
            localName = "trait." + resourceLocation.getNamespace() + "." + name;
            localDesc = "trait." + resourceLocation.getNamespace() + "." + name + ".desc";
        }

        public abstract void onUpdate(IRegen cap);

        public abstract void onAdded(IRegen cap);

        public abstract void onRemoved(IRegen cap);

        public String getLangKey() {
            return localName;
        }

        public ResourceLocation getRegistryName() {
            return resourceLocation;
        }

        public String getLocalDesc() {
            return localDesc;
        }

        public void registerAttributeIfAbsent(LivingEntity livingEntity, IAttribute attributes) {
            if (livingEntity.getAttributes().getInstance(attributes) == null) {
                livingEntity.getAttributes().registerAttribute(attributes);
            }
        }
    }

}