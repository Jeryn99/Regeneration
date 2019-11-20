package me.swirtzly.regeneration.client.skinhandling;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Swirtzly
 * on 19/11/2019 @ 22:51
 */
@Mod.EventBusSubscriber
public class PlayerDataPool {

    private static HashMap<UUID, SkinInfo> PLAYER_POOL = new HashMap<>();

    public static void addPlayer(EntityPlayer player, SkinInfo info) {
        PLAYER_POOL.put(player.getUniqueID(), info);
    }

    public static void addPlayer(UUID player, SkinInfo info) {
        PLAYER_POOL.put(player, info);
    }

    public static void removePlayer(EntityPlayer player) {
        PLAYER_POOL.remove(player.getUniqueID());
    }

    public static void removePlayer(UUID player) {
        PLAYER_POOL.remove(player);
    }

    public static SkinInfo getOrCreate(AbstractClientPlayer player) {
        if (PLAYER_POOL.containsKey(player.getUniqueID())) {
            return PLAYER_POOL.get(player.getUniqueID());
        }
        SkinInfo skinInfo = new SkinInfo();
        skinInfo.setTextureLocation(player.getLocationSkin());
        skinInfo.setSkintype(SkinChangingHandler.getSkinType(player));
        addPlayer(player, skinInfo);
        return skinInfo;
    }


}
