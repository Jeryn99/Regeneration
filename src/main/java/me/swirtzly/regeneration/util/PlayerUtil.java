package me.swirtzly.regeneration.util;

import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.network.MessageSetPerspective;
import me.swirtzly.regeneration.network.MessageUpdateModel;
import me.swirtzly.regeneration.network.NetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {

    public static ArrayList<Potion> POTIONS = new ArrayList<>();

    public static void createPostList() {
        POTIONS.add(MobEffects.WEAKNESS);
        POTIONS.add(MobEffects.MINING_FATIGUE);
        POTIONS.add(MobEffects.RESISTANCE);
        POTIONS.add(MobEffects.HEALTH_BOOST);
        POTIONS.add(MobEffects.HUNGER);
        POTIONS.add(MobEffects.WATER_BREATHING);
        POTIONS.add(MobEffects.HASTE);
    }

    public static void sendMessage(EntityPlayer player, String message, boolean hotBar) {
        if (!player.world.isRemote) {
            player.sendStatusMessage(new TextComponentTranslation(message), hotBar);
        }
    }

    public static void sendMessage(EntityPlayer player, TextComponentTranslation translation, boolean hotBar) {
        if (!player.world.isRemote) {
            player.sendStatusMessage(translation, hotBar);
        }
    }

    public static void sendMessageToAll(TextComponentTranslation translation) {
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        players.forEach(playerMP -> sendMessage(playerMP, translation, false));
    }

    public static void setPerspective(EntityPlayerMP player, boolean thirdperson, boolean resetPitch) {
        NetworkHandler.INSTANCE.sendTo(new MessageSetPerspective(thirdperson, resetPitch), player);
    }

    public static boolean canEntityAttack(Entity entity) { // NOTE unused
        if (entity instanceof EntityLiving) {
            EntityLiving ent = (EntityLiving) entity;
            for (EntityAITasks.EntityAITaskEntry task : ent.tasks.taskEntries) {
                if (task.action instanceof EntityAIAttackMelee || task.action instanceof EntityAIAttackRanged || task.action instanceof EntityAIAttackRangedBow
                        || task.action instanceof EntityAINearestAttackableTarget || task.action instanceof EntityAIZombieAttack || task.action instanceof EntityAIOwnerHurtByTarget)
                    return true;
            }
        }
        return false;
    }

    public static void updateModel(SkinChangingHandler.EnumChoices choice) {
        NetworkHandler.INSTANCE.sendToServer(new MessageUpdateModel(choice.name()));
    }

    public static void openDoors(EntityPlayer player) {
        if (player.world.isRemote) return;
        AxisAlignedBB box = player.getEntityBoundingBox().grow(20);
        for (BlockPos pos : BlockPos.getAllInBox(new BlockPos(box.minX, box.minY, box.minZ), new BlockPos(box.maxX, box.maxY, box.maxZ))) {
            IBlockState blockState = player.world.getBlockState(pos);
            Block block = blockState.getBlock();

            if (player.getPosition().getY() < pos.getY()) {
                if (block instanceof BlockTrapDoor) {
                    IBlockState newState = blockState.withProperty(BlockTrapDoor.OPEN, true);
                    markUpdate(player.world, pos, newState);
                    int j = blockState.getMaterial() == Material.IRON ? 1036 : 1013;
                    player.world.playEvent(player, j, pos, 0);
                }
            } else if (block instanceof BlockDoor) {
                IBlockState down = player.world.getBlockState(pos);
                IBlockState newState = down.withProperty(BlockDoor.OPEN, true);
                markUpdate(player.world, pos, newState);
                player.world.playEvent(player, blockState.getValue(BlockDoor.OPEN) ? blockState.getMaterial() == Material.IRON ? 1005 : 1006 : blockState.getMaterial() == Material.IRON ? 1011 : 1012, pos, 0);
            }
        }
    }

    private static void markUpdate(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state, 10);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }


    public static boolean applyPotionIfAbsent(EntityPlayer player, Potion potion, int length, int amplifier, boolean ambient, boolean showParticles) {
        if (potion == null) return false;
        if (player.getActivePotionEffect(potion) == null) {
            player.addPotionEffect(new PotionEffect(potion, length, amplifier, ambient, showParticles));
            return true;
        }
        return false;
    }

    public static void lookAt(double px, double py, double pz, EntityPlayer me) {
        double dirx = me.getPosition().getX() - px;
        double diry = me.getPosition().getY() - py;
        double dirz = me.getPosition().getZ() - pz;

        double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;

        yaw += 90f;
        me.rotationPitch = (float) pitch;
        me.rotationYaw = (float) yaw;
    }

    public enum RegenState {

        ALIVE,
        GRACE, GRACE_CRIT, POST,
        REGENERATING;

        public boolean isGraceful() {
            return this == GRACE || this == GRACE_CRIT;
        }

        public enum Transition {
            HAND_GLOW_START(Color.YELLOW.darker()), HAND_GLOW_TRIGGER(Color.ORANGE),
            ENTER_CRITICAL(Color.BLUE),
            CRITICAL_DEATH(Color.RED),
            FINISH_REGENERATION(Color.GREEN.darker()),
            END_POST(Color.PINK.darker());

            public final Color color;

            Transition(Color col) {
                this.color = col;
            }
        }

    }
}
