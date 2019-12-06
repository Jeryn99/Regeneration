package me.swirtzly.regeneration.client.skinhandling;

import me.swirtzly.regeneration.RegenerationMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Swirtzly
 * on 19/11/2019 @ 22:51
 */
@Mod.EventBusSubscriber(value = Side.CLIENT)
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
        addPlayer(player, skinInfo);
        SkinChangingHandler.update(player);
        return skinInfo;
    }


    public static void wipeAllData() {
        for (UUID uuid : PLAYER_POOL.keySet()) {
            RegenerationMod.LOG.warn("Deleting skin data for: " + uuid);
            PLAYER_POOL.remove(uuid);
        }
    }
}
