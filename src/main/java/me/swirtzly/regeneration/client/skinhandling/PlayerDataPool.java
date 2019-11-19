package me.swirtzly.regeneration.client.skinhandling;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Swirtzly
 * on 19/11/2019 @ 22:51
 */
@Mod.EventBusSubscriber
public class PlayerDataPool {

    private static HashMap<UUID, SkinInfo> PLAYER_POOL = new HashMap<>();

    public static void addPlayer(EntityPlayer player, SkinInfo info){
        PLAYER_POOL.put(player.getUniqueID(), info);
    }

    public static void removePlayer(EntityPlayer player){
        PLAYER_POOL.remove(player.getUniqueID());
    }
    public static void removePlayer(UUID player){
        PLAYER_POOL.remove(player);
    }

    public static SkinInfo getOrCreate(AbstractClientPlayer player){
        if(PLAYER_POOL.containsKey(player.getUniqueID())){
            return PLAYER_POOL.get(player.getUniqueID());
        }
        SkinInfo skinInfo = new SkinInfo();
        skinInfo.setTextureLocation(player.getLocationSkin());
        skinInfo.setSkintype(player.playerInfo.skinType.equalsIgnoreCase("default") ? SkinInfo.SkinType.STEVE : SkinInfo.SkinType.ALEX);
        addPlayer(player, skinInfo);
        return skinInfo;
    }


}
