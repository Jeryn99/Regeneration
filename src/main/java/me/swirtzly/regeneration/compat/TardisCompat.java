package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tardis.mod.dimensions.TardisDimension;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;

/**
 * Created by Swirtzly
 * on 19/04/2020 @ 16:20
 */
public class TardisCompat {

    @SubscribeEvent
    public void onLive(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().world;
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        if (world.dimension.getDimension() instanceof TardisDimension) {
            ConsoleTile console = TardisHelper.getConsole(world.dimension.getType());
            if (console == null || world.isRemote) return;
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();

            RegenCap.get(playerEntity).ifPresent((data) -> {
                //Regenerating
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {

                    if (data.getType() == TypeManager.Type.FIERY && playerEntity.ticksExisted % 60 == 0) {
                        for (Subsystem subSystem : console.getSubSystems()) {
                            subSystem.damage((ServerPlayerEntity) playerEntity, world.rand.nextInt(5));
                        }
                    }

                    if (console.isInFlight()) {
                        console.getInteriorManager().setAlarmOn(true);
                        console.getInteriorManager().setLight(0);

                        if (console.isInFlight() && data.getType() == TypeManager.Type.FIERY) {
                            if (world.rand.nextInt(50) < 10) {
                                console.crash();
                            }
                        }
                    }
                }

                //Grace
                if (data.getState().isGraceful()) {
                    for (TileEntity tileEntity : playerEntity.world.loadedTileEntityList) {
                        if (playerEntity.getDistanceSq(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < 40 && tileEntity instanceof ConsoleTile && data.getPlayer().ticksExisted % 25 == 0) {
                            tileEntity.getWorld().playSound(null, tileEntity.getPos(), RegenObjects.Sounds.ALARM, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }

            });

        }
    }

}
