package me.sub.util;

import me.sub.client.sound.MovingSoundPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class PlayerUtil {

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


    @SideOnly(Side.CLIENT)
    public static void playMovingSound(EntityPlayer player, SoundEvent soundIn, SoundCategory categoryIn, boolean playerOnly) {
        if (playerOnly) {
            if (player.getUniqueID() == Minecraft.getMinecraft().player.getUniqueID()) {
                return;
            }
        }
        Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundPlayer(player, soundIn, categoryIn));
    }

    public static boolean canEntityAttack(Entity entity) {
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

    public static void sendMessage(EntityPlayer player, ITextComponent textComponent, boolean hotBar) {
        if (!player.world.isRemote) {
            player.sendStatusMessage(textComponent, hotBar);
        }
    }
}
