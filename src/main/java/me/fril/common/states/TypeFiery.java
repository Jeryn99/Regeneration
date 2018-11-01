package me.fril.common.states;

import java.util.Random;

import me.fril.RegenConfig;
import me.fril.common.capability.CapabilityRegeneration;
import me.fril.common.capability.IRegeneration;
import me.fril.common.init.RObjects;
import me.fril.util.PlayerUtil;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class TypeFiery implements IRegenType {
    @Override
    public String getName() {
        return "FIERY";
    }

    @Override
    public void onUpdateInitial(EntityPlayer player) {
        player.extinguish();
        player.setArrowCountInEntity(0);
    }

    @Override
    public void onUpdateMidRegen(EntityPlayer player) {
        player.extinguish();

        Random rand = player.world.rand;
        player.rotationPitch += (rand.nextInt(10) - 5) * 0.2;
        player.rotationYaw += (rand.nextInt(10) - 5) * 0.2;

        if (player.world.isRemote)
            return;

        if (player.world.getBlockState(player.getPosition()).getBlock() instanceof BlockFire)
            player.world.setBlockToAir(player.getPosition());
        double x = player.posX + player.getRNG().nextGaussian() * 2;
        double y = player.posY + 0.5 + player.getRNG().nextGaussian() * 2;
        double z = player.posZ + player.getRNG().nextGaussian() * 2;

        IRegeneration capa = CapabilityRegeneration.get(player);

        if (capa.getTicksRegenerating() > 150 && capa.getTicksRegenerating() < 152) {
            if (!player.world.isRemote) {
                PlayerUtil.damagePlayerArmor((EntityPlayerMP) player);
            }
        }

        player.world.newExplosion(player, x, y, z, 1, RegenConfig.fieryRegen, false);
        for (BlockPos bs : BlockPos.getAllInBox(player.getPosition().north().west(), player.getPosition().south().east()))
            if (player.world.getBlockState(bs).getBlock() instanceof BlockFire) {
                player.world.setBlockToAir(bs);
            }
    }

    @Override
    public void onFinish(EntityPlayer player) {
        if (player.world.isRemote)
            return;

        IRegeneration handler = CapabilityRegeneration.get(player);
        //   handler.setTrait(TraitHandler.getRandomTrait());
        //    player.sendStatusMessage(new TextComponentTranslation(handler.getTrait().getMessage()), true);
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, RegenConfig.postRegenerationDuration * 2, RegenConfig.postRegenerationLevel - 1, false, false)); //XXX move to general handler?
        handler.sync();
    }

    @Override
    public SoundEvent getSound() {
        return RObjects.Sounds.REGEN_1;
    }

    @Override
    public boolean blockMovement() {
        return true;
    }

    @Override
    public boolean isLaying() {
        return false;
    }
}
