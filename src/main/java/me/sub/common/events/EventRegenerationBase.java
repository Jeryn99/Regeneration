package me.sub.common.events;

import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Created by Sub
 * on 20/09/2018.
 */
public class EventRegenerationBase extends PlayerEvent {

    private IRegeneration regenInfo;

    public EventRegenerationBase(EntityPlayer player) {
        super(player);
        regenInfo = CapabilityRegeneration.get(player);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public IRegeneration getRegenInfo() {
        return regenInfo;
    }

    public static class EventEnterRegeneration extends EventRegenerationBase {
        public EventEnterRegeneration(EntityPlayer player) {
            super(player);
        }
    }

    public static class EventRegeneration extends EventRegenerationBase {
        public EventRegeneration(EntityPlayer player) {
            super(player);
        }
    }

    public static class EventEndRegeneration extends EventRegenerationBase {
        public EventEndRegeneration(EntityPlayer player) {
            super(player);
        }

        @Override
        public boolean isCancelable() {
            return false;
        }
    }


}
