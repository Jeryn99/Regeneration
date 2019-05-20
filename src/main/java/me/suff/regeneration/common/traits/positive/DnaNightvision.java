package me.suff.regeneration.common.traits.positive;

import me.suff.regeneration.RegenerationMod;
import me.suff.regeneration.common.capability.IRegeneration;
import me.suff.regeneration.common.traits.DnaHandler;
import me.suff.regeneration.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

public class DnaNightvision implements DnaHandler.IDna {

    private ResourceLocation ID = new ResourceLocation(RegenerationMod.MODID, "nightvision");

    @Override
    public void onUpdate(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        PlayerUtil.applyPotionIfAbsent(player, MobEffects.NIGHT_VISION, 1200, 1, true, false);
    }

    @Override
    public void onAdded(IRegeneration cap) {

    }

    @Override
    public void onRemoved(IRegeneration cap) {
        EntityPlayer player = cap.getPlayer();
        player.removePotionEffect(MobEffects.NIGHT_VISION);
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
