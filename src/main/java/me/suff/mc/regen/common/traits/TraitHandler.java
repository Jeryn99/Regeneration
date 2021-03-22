package me.suff.mc.regen.common.traits;

import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.RConstants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RConstants.MODID)
public class TraitHandler {

    @SubscribeEvent
    public static void onExperienceGain(PlayerXpEvent.PickupXp event) {
        RegenCap.get(event.getPlayer()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.SMART.get().getRegistryName().toString())) {
                event.getOrb().value *= 1.5;
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        RegenCap.get(event.player).ifPresent(iRegen -> {
            String playerTrait = iRegen.trait().getRegistryName().toString();
            if (iRegen.traitActive() && playerTrait.equals(RegenTraitRegistry.WATER_STRIDE.get().getRegistryName().toString())) {
                World world = event.player.level;
                int x = MathHelper.floor(event.player.position().x);
                int y = MathHelper.floor(event.player.getBoundingBox().minY);
                int z = MathHelper.floor(event.player.position().z);
                if (world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial().isLiquid()) {
                    Vector3d delta = event.player.getDeltaMovement();
                    event.player.setDeltaMovement(new Vector3d(delta.x, 0, delta.z));
                }
            }
            else if (playerTrait.equals(RegenTraitRegistry.PHOTOSYNTHETIC.get().getRegistryName().toString())) {
                //check if day
                if (event.player.level.isDay() && !event.player.level.isRaining()) {
                    //if day, check if its time to do photosynthetic ability
                    if (event.player.tickCount % 200 == 0) {
                        //if the player can see the sky
                        if (event.player.level.canSeeSky(event.player.blockPosition())) {
                            event.player.getFoodData().eat(1,0.25f);
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        LivingEntity living = event.getEntityLiving();
        RegenCap.get(event.getEntityLiving()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.ENDER_HURT.get().getRegistryName().toString())) {
                for (int i = 0; i < 16; ++i) {
                    double d3 = living.getX() + (living.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double d4 = MathHelper.clamp(living.getY() + (double) (living.getRandom().nextInt(16) - 8), 0.0D, (double) (living.level.getHeight() - 1));
                    double d5 = living.getZ() + (living.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (living.isPassenger()) {
                        living.stopRiding();
                    }

                    if (living.randomTeleport(d3, d4, d5, true)) {
                        event.setAmount(event.getAmount() / 2);
                        break;
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onMineBlock(PlayerEvent.BreakSpeed event) {
        RegenCap.get(event.getPlayer()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.FAST_MINE.get().getRegistryName().toString())) {
                event.setNewSpeed(event.getOriginalSpeed() * 5);
            }
        });
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        RegenCap.get(event.getEntityLiving()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.KNOCKBACK.get().getRegistryName().toString())) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        RegenCap.get(event.getEntityLiving()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait().getRegistryName().toString().equals(RegenTraitRegistry.LEAP.get().getRegistryName().toString())) {
                event.getEntityLiving().setDeltaMovement(event.getEntityLiving().getDeltaMovement().x, event.getEntityLiving().getDeltaMovement().y + 0.1F * 2, event.getEntityLiving().getEntity().getDeltaMovement().z);
            }
        });
    }
}
