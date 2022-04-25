package me.suff.mc.regen.compat;

import me.suff.mc.regen.Regeneration;
import me.suff.mc.regen.common.entities.TimelordEntity;
import me.suff.mc.regen.common.regen.IRegen;
import me.suff.mc.regen.common.regen.acting.Acting;
import me.suff.mc.regen.common.regen.acting.ActingForwarder;
import me.suff.mc.regen.common.regen.state.RegenStates;
import me.suff.mc.regen.util.PlayerUtil;
import me.suff.mc.regen.util.RConstants;
import me.suff.mc.regen.util.RegenUtil;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tardis.mod.ars.ConsoleRoom;
import net.tardis.mod.entity.hostile.dalek.DalekEntity;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.helper.WorldHelper;
import net.tardis.mod.misc.CrashType;
import net.tardis.mod.sounds.TSounds;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.world.dimensions.TDimensions;

import java.util.List;

/* Created by Craig on 20/03/2021 */
public class TardisMod implements Acting {

    public static void tardis() {
        MinecraftForge.EVENT_BUS.register(new TardisMod());
        Regeneration.LOG.info("Tardis Mod Detected! Enabling Compatibility Features!");
        ActingForwarder.register(TardisMod.class, Dist.DEDICATED_SERVER);
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

    /**
     * Called for every tick a Player is regenerating WARNING: Server only!
     *
     * @param cap
     */
    @Override
    public void onRegenTick(IRegen cap) {
        World world = cap.getLiving().level;
        if (cap.regenState() == RegenStates.REGENERATING && isTardis(cap.getLiving().level) && !PlayerUtil.isPlayerAboveZeroGrid(cap.getLiving())) {
            ConsoleTile console = (ConsoleTile) world.getBlockEntity(TardisHelper.TARDIS_POS);
            if (console.getLevel().getGameTime() % 5 == 0) {

                if (console.isInFlight() && world.random.nextInt(100) < 5) {
                    console.crash(new CrashType(70, 7, true));
                    return;
                }

                List<Subsystem> subsystems = console.getSubSystems();
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
    }

    /**
     * Called just after the player has been killed It is only called ONCE, once the player enters a grace period
     *
     * @param cap
     */
    @Override
    public void onEnterGrace(IRegen cap) {

    }

    /**
     * Called ONCE when the players hands begin to glow
     *
     * @param cap
     */
    @Override
    public void onHandsStartGlowing(IRegen cap) {

    }

    /**
     * Called when the player enters the critical stage of a grace period
     *
     * @param cap
     */
    @Override
    public void onGoCritical(IRegen cap) {
        World world = cap.getLiving().level;
        if (isTardis(cap.getLiving().level) && !PlayerUtil.isPlayerAboveZeroGrid(cap.getLiving())) {
            ConsoleTile console = (ConsoleTile) world.getBlockEntity(TardisHelper.TARDIS_POS);
            console.getInteriorManager().setAlarmOn(true);
        }
    }

    /**
     * Called on the first tick of a players Regeneration
     *
     * @param cap
     */
    @Override
    public void onRegenTrigger(IRegen cap) {
        World world = cap.getLiving().level;
        if (isTardis(cap.getLiving().level) && !PlayerUtil.isPlayerAboveZeroGrid(cap.getLiving())) {
            ConsoleTile console = (ConsoleTile) world.getBlockEntity(TardisHelper.TARDIS_POS);
            console.getInteriorManager().setAlarmOn(true);
        }
    }

    /**
     * Called on the last tick of a players Regeneration
     *
     * @param cap
     */
    @Override
    public void onRegenFinish(IRegen cap) {
        World world = cap.getLiving().level;
        if (isTardis(cap.getLiving().level)) {
            ConsoleTile console = (ConsoleTile) world.getBlockEntity(TardisHelper.TARDIS_POS);
            if (cap.getLiving() instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) cap.getLiving();
                console.getEmotionHandler().setLoyalty(playerEntity.getUUID(), console.getEmotionHandler().getLoyalty(playerEntity.getUUID()) - RegenUtil.RAND.nextInt(25));
                console.getInteriorManager().setAlarmOn(false);
                console.getUnlockManager().addConsoleRoom(ConsoleRoom.getRegistry().get(new ResourceLocation(RConstants.MODID, "mccoy")));
            }
        }
    }

    @Override
    public void onPerformingPost(IRegen cap) {

    }
}
