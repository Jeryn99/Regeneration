package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.registries.RRRegenType;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.tardis.mod.dimensions.TardisDimension;
import net.tardis.mod.entity.DalekEntity;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.items.TItems;
import net.tardis.mod.recipe.Recipes;
import net.tardis.mod.recipe.WeldRecipe;
import net.tardis.mod.registries.TardisRegistries;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.upgrades.UpgradeEntry;

/**
 * Created by Swirtzly
 * on 19/04/2020 @ 16:20
 */
public class TardisCompat {

    public static void addTardisCompat() {
        TardisRegistries.registerRegisters(TardisCompat::registerAllProtocols);
        TardisRegistries.registerRegisters(TardisCompat::registerAllUpgrades);
        Recipes.WELD_RECIPE.add(new WeldRecipe(RegenObjects.Items.ARCH_PART.get(), false, RegenObjects.Items.HAND.get(), TItems.CIRCUITS));
        Recipes.WELD_RECIPE.add(new WeldRecipe(RegenObjects.Items.ARCH_PART.get(), true, RegenObjects.Items.ARCH_PART.get(), TItems.CIRCUITS, RegenObjects.Items.HAND.get()));
    }

    public static void registerAllUpgrades() {
        TardisRegistries.UPGRADES.register(new ResourceLocation(RegenerationMod.MODID, "arch"), new UpgradeEntry<>(ArchUpgrade::new, RegenObjects.Items.ARCH_PART.get(), null));
    }

    public static void registerAllProtocols() {
        TardisRegistries.PROTOCOL_REGISTRY.register(new ResourceLocation(RegenerationMod.MODID, "arch_protocol"), new ArchProtocol());
    }


    public static void damageSubsystem(World world) {
        getTardis(world).getUpgrade(ArchUpgrade.class).ifPresent((archUpgrade -> {
            //TODO FIX SOURCES SO THE BELOW CALL CAN BE MADE
            // archUpgrade.damage(1, null, null);
            archUpgrade.getStack().attemptDamageItem(1, world.rand, null);
        }));
    }

    private static ConsoleTile getTardis(World world) {
        return (ConsoleTile) world.getTileEntity(TardisHelper.TARDIS_POS);
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent entityJoinWorldEvent){
        if(entityJoinWorldEvent.getEntity() instanceof TimelordEntity){
            TimelordEntity timelordEntity = (TimelordEntity) entityJoinWorldEvent.getEntity();
            timelordEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(timelordEntity, DalekEntity.class, false));
        }
    }

    @SubscribeEvent
    public void onLive(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().world;
        if (world.isRemote) return;

        if (world.dimension.getDimension() instanceof TardisDimension) {
            MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
            ConsoleTile console = TardisHelper.getConsole(minecraftServer, world.dimension.getType());
            if (console == null) return;
            LivingEntity playerEntity = event.getEntityLiving();
            RegenCap.get(playerEntity).ifPresent((data) -> {
                //Regenerating
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    if (data.getType() == RRRegenType.FIERY && playerEntity.ticksExisted % 10 == 0) {
                        for (Subsystem subSystem : console.getSubSystems()) {
                            subSystem.damage(null, world.rand.nextInt(5));
                        }
                    }

                    if (console.isInFlight()) {
                        console.getInteriorManager().setAlarmOn(true);
                        console.getInteriorManager().setLight(0);

                        if (console.isInFlight() && data.getType() == RRRegenType.FIERY) {
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