package me.suff.regeneration.common.traits.negative;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.traits.DnaHandler;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

public class DnaHunger implements DnaHandler.IDna {

    private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "hunger");

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        if(player.ticksExisted % 2400 == 0 && player.world.rand.nextBoolean()){
            PlayerUtil.applyPotionIfAbsent(player, MobEffects.HUNGER, 200, 1, true, false);
        }
    }

    @Override
    public void onAdded(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        PlayerUtil.applyPotionIfAbsent(player, MobEffects.HUNGER, 200, 1, true, false);
    }

    @Override
    public void onRemoved(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        player.removePotionEffect(MobEffects.HUNGER);
    }

    @Override
    public String getLangKey() {
        return "traits." + ID.getPath() + ".name";
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }
}
