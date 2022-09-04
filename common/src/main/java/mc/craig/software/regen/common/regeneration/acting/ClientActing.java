package mc.craig.software.regen.common.regeneration.acting;

import mc.craig.software.regen.common.regeneration.RegenerationData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EntityType;

class ClientActing implements Acting {

    public static final Acting INSTANCE = new ClientActing();

    @Override
    public void onRegenTick(RegenerationData cap) {
        // never forwarded as per the documentation
    }

    @Override
    public void onEnterGrace(RegenerationData cap) {
        if (cap.getPlayer().getUUID().equals(Minecraft.getInstance().player.getUUID())) {

        }
    }

    @Override
    public void onHandsStartGlowing(RegenerationData cap) {
        if (cap.getPlayer().getType() == EntityType.PLAYER) {

        }
    }

    @Override
    public void onRegenFinish(RegenerationData cap) {

    }

    @Override
    public void onPerformingPost(RegenerationData cap) {

    }

    @Override
    public void onRegenTrigger(RegenerationData cap) {

    }

    @Override
    public void onGoCritical(RegenerationData cap) {
        if (Minecraft.getInstance().player.getUUID().equals(cap.getPlayer().getUUID())) {
            if (cap.getPlayer().getType() == EntityType.PLAYER) {

            }
        }
    }

}
