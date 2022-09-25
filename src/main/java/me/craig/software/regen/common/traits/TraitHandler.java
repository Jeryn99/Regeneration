package me.craig.software.regen.common.traits;

import me.craig.software.regen.util.RConstants;
import me.craig.software.regen.common.regen.RegenCap;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
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
            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.SMART.get()) {
                event.getOrb().value *= 1.5;
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        RegenCap.get(event.player).ifPresent(iRegen -> {

            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.WATER_STRIDE.get()) {
                World world = event.player.level;
                int x = MathHelper.floor(event.player.position().x);
                int y = MathHelper.floor(event.player.getBoundingBox().minY);
                int z = MathHelper.floor(event.player.position().z);
                if (world.getBlockState(new BlockPos(x, y - 1, z)).getMaterial() == Material.WATER) {
                    Vector3d delta = event.player.getDeltaMovement();
                    event.player.setDeltaMovement(new Vector3d(delta.x, 0, delta.z));
                }
            }

            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.PHOTOSYNTHETIC.get()) {
                //check if day
                if (event.player.level.isDay() && !event.player.level.isRaining()) {
                    //if day, check if its time to do photosynthetic ability
                    if (event.player.tickCount % 200 == 0) {
                        //if the player can see the sky
                        if (event.player.level.canSeeSky(event.player.blockPosition())) {
                            event.player.getFoodData().eat(1, 0.25f);
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
            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.ENDER_HURT.get()) {
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
            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.FAST_MINE.get()) {
                event.setNewSpeed(event.getOriginalSpeed() * 5);
            }
        });
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        RegenCap.get(event.getEntityLiving()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.KNOCKBACK.get()) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        RegenCap.get(event.getEntityLiving()).ifPresent(iRegen -> {
            if (iRegen.traitActive() && iRegen.trait() == RegenTraitRegistry.LEAP.get()) {
                event.getEntityLiving().setDeltaMovement(event.getEntityLiving().getDeltaMovement().x, event.getEntityLiving().getDeltaMovement().y + 0.1F * 2, event.getEntityLiving().getEntity().getDeltaMovement().z);
            }
        });
    }
}
