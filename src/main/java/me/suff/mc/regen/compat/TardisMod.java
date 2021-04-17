package me.suff.mc.regen.compat;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.regen.RegenCap;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tardis.mod.entity.hostile.dalek.DalekEntity;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.helper.WorldHelper;
import net.tardis.mod.sounds.TSounds;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.world.dimensions.TDimensions;

import java.util.List;

/* Created by Craig on 20/03/2021 */
public class TardisMod {

    public static void tardis() {
        MinecraftForge.EVENT_BUS.register(new TardisMod());
        Regeneration.LOG.info("Tardis Mod Detected! Enabling Compatibility Features!");
    }

    @SubscribeEvent
    public void onLive(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        World world = living.level;
        RegenCap.get(living).ifPresent(iRegen -> {
            if (iRegen.regenState() == RegenStates.REGENERATING && isTardis(living.level) && !PlayerUtil.isPlayerAboveZeroGrid(living)) {
                ConsoleTile console = (ConsoleTile) world.getBlockEntity(TardisHelper.TARDIS_POS);
                if (console.getLevel().getGameTime() % 5 == 0) {

                    if (console.isInFlight() && world.random.nextInt(100) < 5) {
                        console.crash();
                        return;
                    }

                    List< Subsystem > subsystems = console.getSubSystems();
                    if (!subsystems.isEmpty()) {
                        Subsystem randomSubsystem = subsystems.get(world.random.nextInt(subsystems.size()));
                        if (world.random.nextBoolean()) {
                            for (int i = 0; i < 18; ++i) {
                                double angle = Math.toRadians(i * 20);
                                double x = Math.sin(angle);
                                double z = Math.cos(angle);
                                BlockPos pos = new BlockPos(0, 128, 0);
                                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), TSounds.ELECTRIC_SPARK.get(), SoundCategory.BLOCKS, 0.05F, 1.0F, false);
                                world.addParticle(ParticleTypes.LAVA, (double) pos.getX() + 0.5D + x, (double) pos.getY() + world.random.nextDouble(), (double) pos.getZ() + 0.5D + z, 0.0D, 0.0D, 0.0D);
                            }
                            if (randomSubsystem.getHealth() > 0) {
                                randomSubsystem.damage(null, world.random.nextInt(5));
                            }
                        }
                    }
                }
            }
        });
    }

    public boolean isTardis(World world) {
        return WorldHelper.areDimensionTypesSame(world, TDimensions.DimensionTypes.TARDIS_TYPE);
    }

    @SubscribeEvent
    public void onLive(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof TimelordEntity) {
            TimelordEntity timelordEntity = (TimelordEntity) event.getEntity();
            if (timelordEntity.getTimelordType() == TimelordEntity.TimelordType.COUNCIL) {
                timelordEntity.goalSelector.addGoal(1, new AvoidEntityGoal<>(timelordEntity, DalekEntity.class, 15.0F, 0.5D, 0.5D));
            }

            if (timelordEntity.getTimelordType() == TimelordEntity.TimelordType.GUARD) {
                timelordEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(timelordEntity, DalekEntity.class, false));
            }
        }

        if (event.getEntity() instanceof DalekEntity) {
            DalekEntity dalekEntity = (DalekEntity) event.getEntity();
            dalekEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(dalekEntity, TimelordEntity.class, false));
        }
    }

}
