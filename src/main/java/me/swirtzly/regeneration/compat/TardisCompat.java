package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.types.TypeManager;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.tardis.mod.dimensions.TardisDimension;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.items.TItems;
import net.tardis.mod.recipe.Recipes;
import net.tardis.mod.recipe.WeldRecipe;
import net.tardis.mod.registries.TardisRegistries;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.subsystem.SubsystemEntry;
import net.tardis.mod.tileentities.ConsoleTile;

import static net.tardis.mod.registries.TardisRegistries.PROTOCOL_REGISTRY;

/**
 * Created by Swirtzly
 * on 19/04/2020 @ 16:20
 */
public class TardisCompat {

    public static SubsystemEntry<ArchSubSystem> ARCH_SUBSYSTEM;

    public static void on() {
        PROTOCOL_REGISTRY.register("arch_protocol", new ArchProtocol());
        ARCH_SUBSYSTEM = register("arch", new SubsystemEntry<>(ArchSubSystem::new, RegenObjects.Items.ARCH_PART.get()));
        Recipes.WELD_RECIPE.add(new WeldRecipe(RegenObjects.Items.ARCH_PART.get(), false, RegenObjects.Items.HAND.get(), TItems.CIRCUITS));
    }

    public static <T extends Subsystem> SubsystemEntry<T> register(ResourceLocation key, SubsystemEntry<T> system) {
        TardisRegistries.SUBSYSTEM_REGISTRY.register(key, system);
        return system;
    }

    public static <T extends Subsystem> SubsystemEntry<T> register(String key, SubsystemEntry<T> system) {
        return register(new ResourceLocation(RegenerationMod.MODID, key), system);
    }

    public static void damageSubsystem(World world) {
        getTardis(world).getSubsystem(ArchSubSystem.class).ifPresent((archSubSystem -> {
            archSubSystem.damage(null, 1);
        }));
    }

    private static ConsoleTile getTardis(World world) {
        return (ConsoleTile) world.getTileEntity(TardisHelper.TARDIS_POS);
    }


    @SubscribeEvent
    public void onLive(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().world;
        if (world.isRemote) return;
        if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
        if (world.dimension.getDimension() instanceof TardisDimension) {
            MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
            ConsoleTile console = TardisHelper.getConsole(minecraftServer, world.dimension.getType());
            if (console == null || world.isRemote) return;
            PlayerEntity playerEntity = (PlayerEntity) event.getEntityLiving();
            RegenCap.get(playerEntity).ifPresent((data) -> {
                //Regenerating
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    if (data.getType() == TypeManager.Type.FIERY && playerEntity.ticksExisted % 10 == 0) {
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
                        if (playerEntity.getDistanceSq(tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()) < 40 && tileEntity instanceof ConsoleTile && data.getLivingEntity().ticksExisted % 25 == 0) {
                            tileEntity.getWorld().playSound(null, tileEntity.getPos(), RegenObjects.Sounds.ALARM.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }

            });

        }
    }


}
