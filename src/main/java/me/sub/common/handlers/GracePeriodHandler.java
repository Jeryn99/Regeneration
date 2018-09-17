package me.sub.common.handlers;

import me.sub.Regeneration;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Regeneration.MODID)
public class GracePeriodHandler {

    @SubscribeEvent
    public static void breakBlock(PlayerInteractEvent.LeftClickBlock e) {
        EntityPlayer player = e.getEntityPlayer();
        IRegeneration regenInfo = CapabilityRegeneration.get(player);
        boolean inGracePeriod = regenInfo.isInGracePeriod() && regenInfo.isGlowing();

        if (inGracePeriod) {
            regenInfo.setGlowing(false);
            regenInfo.setTicksGlowing(0);
        }
    }


}
