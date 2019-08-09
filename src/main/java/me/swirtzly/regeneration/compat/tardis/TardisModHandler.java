package me.swirtzly.regeneration.compat.tardis;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.handlers.IActingHandler;
import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tardis.mod.common.dimensions.TDimensions;
import net.tardis.mod.common.dimensions.WorldProviderTardis;
import net.tardis.mod.common.entities.controls.ControlDoor;
import net.tardis.mod.common.sounds.TSounds;
import net.tardis.mod.common.systems.SystemDimension;
import net.tardis.mod.common.systems.TardisSystems;
import net.tardis.mod.common.tileentity.TileEntityTardis;

import java.util.Random;

public class TardisModHandler implements IActingHandler {

    public static void registerEventBus() {
        MinecraftForge.EVENT_BUS.register(new TardisModHandler());
    }

    @Override
    public void onRegenTick(IRegeneration cap) {
        playBells(cap, false);
    }

    @Override
    public void onEnterGrace(IRegeneration cap) {
        if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
            playBells(cap, true);
        }

    }

    @Override
    public void onHandsStartGlowing(IRegeneration cap) {
        if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
            playBells(cap, true);
        }
    }

    @Override
    public void onRegenFinish(IRegeneration cap) {
        if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
            playBells(cap, true);
        }
    }

    @Override
    public void onStartPost(IRegeneration cap) {

    }

    @Override
    public void onProcessDone(IRegeneration cap) {

    }

    @Override
    public void onRegenTrigger(IRegeneration cap) {
        if (cap.getType() == TypeHandler.RegenType.FIERY) {
            damageTardisInRange(cap.getPlayer());
        }
    }

    @Override
    public void onGoCritical(IRegeneration cap) {
        if (cap.getPlayer().world.provider instanceof WorldProviderTardis) {
            playBells(cap, true);
        }
    }

    private void playBells(IRegeneration cap, boolean force) {
        if (cap.getPlayer().ticksExisted % 1200 == 0 && cap.getPlayer().world.provider instanceof WorldProviderTardis || force) {
            cap.getPlayer().world.playSound(null, cap.getPlayer().getPosition(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1, 1);
        }
    }

    private void damageTardisInRange(EntityPlayer player) {
        if (!RegenConfig.modIntegrations.tardisMod.damageTardis)
            return;
        for (TileEntity te : player.world.loadedTileEntityList) {
            if (!(te instanceof TileEntityTardis) || player.getDistanceSq(te.getPos()) > 10)
                continue;

            TileEntityTardis tileEntityTardis = (TileEntityTardis) te;
            Random rand = tileEntityTardis.getWorld().rand;
            tileEntityTardis.getWorld().playSound(null, tileEntityTardis.getPos(), TSounds.cloister_bell, SoundCategory.BLOCKS, 1.0F, 1.0F);

            // Lets close the door
            ControlDoor controlDoor = tileEntityTardis.getDoor();
            if (controlDoor != null && controlDoor.isOpen()) {
                controlDoor.processInitialInteract(player, EnumHand.MAIN_HAND);
            }

            // Lets damage some systems
            TardisSystems.BaseSystem[] systems = tileEntityTardis.systems;
            for (TardisSystems.BaseSystem system : systems) {
                if (rand.nextInt(5) < 2 && !(system instanceof SystemDimension)) {
                    system.damage();
                    tileEntityTardis.getWorld().getEntitiesWithinAABB(EntityPlayer.class, player.getEntityBoundingBox().expand(45, 45, 45)).forEach(player1 -> {
                        if (player1 instanceof EntityPlayerMP) {
                            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player1;
                            BlockPos tilePos = tileEntityTardis.getPos();
                            Vec3d look = new Vec3d(0.1, 0, 0.1);
                            for (int particle = 0; particle < 300; ++particle) {
                                entityPlayerMP.connection.sendPacket(new SPacketParticles(EnumParticleTypes.EXPLOSION_NORMAL, true, tilePos.getX(), tilePos.getY(), tilePos.getZ(), (float) look.x, (float) look.y, (float) look.z, 2, 25));
                                look = look.rotateYaw(0.003F);
                            }
                        }
                    });
                }
            }

            if (tileEntityTardis.isInFlight()) {
                tileEntityTardis.crash(true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            IRegeneration data = CapabilityRegeneration.getForPlayer(player);
            if (player.dimension == TDimensions.TARDIS_ID) {
                if (data.getState().isGraceful()) {
                    for (TileEntity tileEntity : player.world.loadedTileEntityList) {
                        if (player.getDistanceSq(tileEntity.getPos()) < 40 && tileEntity instanceof TileEntityTardis && data.getPlayer().ticksExisted % 25 == 0) {
                            tileEntity.getWorld().playSound(null, tileEntity.getPos(), RegenObjects.Sounds.ALARM, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

}
