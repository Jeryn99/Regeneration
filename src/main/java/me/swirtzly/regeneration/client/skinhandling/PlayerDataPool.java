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
        info.setUpdateRequired(true);
        PLAYER_POOL.put(player.getUniqueID(), info);
    }

    public static void addPlayer(UUID player, SkinInfo info) {
        info.setUpdateRequired(true);
        PLAYER_POOL.put(player, info);
    }

    public static void removePlayer(EntityPlayer player) {
        if (PLAYER_POOL.containsKey(player.getUniqueID())) {
            PLAYER_POOL.remove(player.getUniqueID()).setUpdateRequired(true);
        }
    }

    public static void updatePlayer(EntityPlayer player, SkinInfo info) {
        if (PLAYER_POOL.containsKey(player.getUniqueID())) {
            PLAYER_POOL.replace(player.getUniqueID(), info);
        } else {
            addPlayer(player, info);
        }
    }

    public static void removePlayer(UUID player) {
        if (PLAYER_POOL.containsKey(player)) {
            PLAYER_POOL.get(player).setUpdateRequired(true);
        }
    }

    public static SkinInfo get(AbstractClientPlayer player) {
        if (PLAYER_POOL.containsKey(player.getUniqueID())) {
            return PLAYER_POOL.get(player.getUniqueID());
        }
        return new SkinInfo().setUpdateRequired(true);
    }


    public static void wipeAllData() {
        if (!PLAYER_POOL.isEmpty()) {
            PLAYER_POOL.clear();
            RegenerationMod.LOG.info("Cleared Player Pool.");
        }
    }
}
