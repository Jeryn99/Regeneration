package me.swirtzly.regeneration.compat;

import me.swirtzly.regeneration.RegenConfig;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.api.ZeroRoomEvent;
import me.swirtzly.regeneration.common.block.ZeroRoomBlock;
import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.types.RegenTypes;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.tardis.mod.ars.ARSPiece;
import net.tardis.mod.ars.ARSPieces;
import net.tardis.mod.blocks.RoundelBlock;
import net.tardis.mod.dimensions.TardisDimension;
import net.tardis.mod.entity.DalekEntity;
import net.tardis.mod.enums.EnumDoorState;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.items.TItems;
import net.tardis.mod.recipe.WeldRecipe;
import net.tardis.mod.registries.TardisRegistries;
import net.tardis.mod.subsystem.Subsystem;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.upgrades.Upgrade;
import net.tardis.mod.upgrades.UpgradeEntry;

import java.util.Optional;

import static me.swirtzly.regeneration.Regeneration.LOG;
import static me.swirtzly.regeneration.handlers.RegenObjects.setUpBlock;
import static net.tardis.mod.helper.TardisHelper.TARDIS_POS;

/**
 * Created by Swirtzly
 * on 19/04/2020 @ 16:20
 */
public class TardisCompat {

    public static void addTardisCompat() {
        LOG.info("Loading Tardis Compatibility");
        TardisRegistries.registerRegisters(TardisCompat::registerAllProtocols);
        TardisRegistries.registerRegisters(TardisCompat::registerAllUpgrades);
        WeldRecipe.WELD_RECIPE.add(new WeldRecipe(RegenObjects.Items.ARCH_PART.get(), false, RegenObjects.Items.HAND.get(), TItems.CIRCUITS));
        WeldRecipe.WELD_RECIPE.add(new WeldRecipe(RegenObjects.Items.ARCH_PART.get(), true, RegenObjects.Items.ARCH_PART.get(), TItems.CIRCUITS, RegenObjects.Items.HAND.get()));
        TardisRegistries.registerRegisters(TardisCompat::registerAllRooms);
        MinecraftForge.EVENT_BUS.register(new TardisCompat());
    }

    public static void registerAllUpgrades() {
        TardisRegistries.UPGRADES.register(new ResourceLocation(Regeneration.MODID, "arch"), new UpgradeEntry<>(ArchUpgrade::new, RegenObjects.Items.ARCH_PART.get(), null));
    }

    public static void registerAllProtocols() {
      //  Optional<? extends ModContainer> tardis = ModList.get().getModContainerById("tardis");
      //  if (tardis.get().getModInfo().getVersion().getMajorVersion() >= 5) {
            TardisRegistries.PROTOCOL_REGISTRY.register(new ResourceLocation(Regeneration.MODID, "arch_protocol"), new ArchProtocol());
     //   } else {
    //        LOG.error("Tardis Mod Version is too low to Register Arch Protocol! This is fine");
    //    }
    }

    public static void registerAllRooms() {
        ARSPieces.register("zero_room", new ARSPiece(new ResourceLocation(Regeneration.MODID, "regeneration/structures/ars/zero_room"), new BlockPos(9, 5, 19)));
    }

    public static void damageSubsystem(World world) {
        getTardis(world).getUpgrade(ArchUpgrade.class).ifPresent((archUpgrade -> {
            archUpgrade.damage(1, Upgrade.DamageType.ITEM, null);
        }));
    }

    private static ConsoleTile getTardis(World world) {
        return (ConsoleTile) world.getTileEntity(TARDIS_POS);
    }

    public static Block createBlock() {
        return setUpBlock(new RoundelBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F), SoundType.CORAL, 3,3));
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent entityJoinWorldEvent) {
        if (entityJoinWorldEvent.getEntity() instanceof TimelordEntity) {
            TimelordEntity timelordEntity = (TimelordEntity) entityJoinWorldEvent.getEntity();
            timelordEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(timelordEntity, DalekEntity.class, false));
        }
    }

    @SubscribeEvent
    public void onZeroRoom(ZeroRoomEvent event) {
        World world = event.getEntityLiving().world;
        TardisHelper.getConsoleInWorld(world).ifPresent((consoleTile -> {
            if (consoleTile.getArtron() < 5F) {
                event.setCanceled(true);
            }
        }));
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        ServerPlayerEntity player = event.getPlayer();
        RegenCap.get(player).ifPresent((data) -> {
            if (data.getState() == PlayerUtil.RegenState.POST) {
                World world = player.world;
                if (world.dimension instanceof TardisDimension) {
                    if (PlayerUtil.isZeroRoom(player)) {
                        TardisHelper.getConsoleInWorld(world).ifPresent((consoleTile -> consoleTile.setArtron(consoleTile.getArtron() - 5F)));
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public void onLive(LivingEvent.LivingUpdateEvent event) {
        World world = event.getEntityLiving().world;
        if (world.isRemote) return;

        if (world.dimension.getDimension() instanceof TardisDimension) {
            ConsoleTile console = findConsole(world.dimension.getType());
            if (console == null) return;
            LivingEntity playerEntity = event.getEntityLiving();
            RegenCap.get(playerEntity).ifPresent((data) -> {
                //Regenerating
                if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                    if (data.getRegenType() == RegenTypes.FIERY && playerEntity.ticksExisted % 10 == 0) {
                        for (Subsystem subSystem : console.getSubSystems()) {
                            subSystem.damage(null, world.rand.nextInt(5));
                        }
                    }

                    if (console.isInFlight()) {
                        console.getInteriorManager().setAlarmOn(true);
                        console.getInteriorManager().setLight(0);

                        if (console.isInFlight() && data.getRegenType() == RegenTypes.FIERY) {
                            if (world.rand.nextInt(50) < 10) {
                                console.crash();
                            }
                        }
                    } else {
                        console.getDoor().ifPresent(doorEntity -> {
                            if (doorEntity.getOpenState() != EnumDoorState.CLOSED) {
                                BlockPos loc = console.getLocation();
                                ServerWorld currentWorld = ServerLifecycleHooks.getCurrentServer().getWorld(console.getDimension());
                                for (int i = 0; i < 5; i++) {
                                    int xOffset = world.rand.nextInt(5);
                                    int zOffset = world.rand.nextInt(5);
/*
                                    int y = world.getChunk(loc.getX() + xOffset, loc.getZ() + zOffset).getTopBlockY(Heightmap.Type.MOTION_BLOCKING, spawn.getX(), spawn.getZ()
*/
                                    currentWorld.setBlockState(loc.add(xOffset, 0, zOffset), Blocks.FIRE.getDefaultState());
                                }
                            }
                        });
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

    @SubscribeEvent
    public void onBreed(BabyEntitySpawnEvent entitySpawnEvent) {
        if(RegenConfig.COMMON.mobsHaveRegens.get()) {
            AgeableEntity kid = entitySpawnEvent.getChild();
            if (kid.getEntityWorld().dimension instanceof TardisDimension) {
                ConsoleTile tardis = getTardis(kid.getEntityWorld());
                if (tardis.isInFlight()) {
                    RegenCap.get(kid).ifPresent(iRegen -> {
                        iRegen.receiveRegenerations(kid.world.rand.nextInt(12));
                        iRegen.setRegenType(RegenTypes.HARTNELL);
                    });
                }
            }
        }
    }

    /* I'm aware this is not how to do this, and I will fix it in the due course of time */
    public ConsoleTile findConsole(DimensionType type) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerWorld world = server.getWorld(type);
        if (world != null) {
            TileEntity te = world.getTileEntity(TARDIS_POS);
            if (te instanceof ConsoleTile) {
                return (ConsoleTile) te;
            }
        }
        return null;
    }

}
