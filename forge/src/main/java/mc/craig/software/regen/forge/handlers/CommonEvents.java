package mc.craig.software.regen.forge.handlers;

import mc.craig.software.regen.common.regeneration.RegenerationData;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommonEvents {

    @SubscribeEvent
    public static void onPunchBlock(PlayerInteractEvent.LeftClickBlock clickBlock){
        if (clickBlock.getEntity().level.isClientSide) return;
        RegenerationData.get(clickBlock.getEntity()).ifPresent(regenerationData -> {
            BlockState block = clickBlock.getLevel().getBlockState(clickBlock.getPos());
            regenerationData.stateManager().onPunchBlock(block, clickBlock.getPos());
        });
    }

}
