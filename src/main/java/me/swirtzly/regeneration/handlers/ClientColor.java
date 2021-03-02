package me.swirtzly.regeneration.handlers;

import net.minecraft.item.IDyeableArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

import static me.swirtzly.regeneration.Regeneration.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientColor {

    @SubscribeEvent
    public static void registerItemColor(ColorHandlerEvent.Item event) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> event.getItemColors().register((p_getColor_1_, p_getColor_2_) -> p_getColor_2_ > 0 ? -1 : ((IDyeableArmorItem) p_getColor_1_.getItem()).getColor(p_getColor_1_), RegenObjects.Items.ROBES_CHEST.get(), RegenObjects.Items.ROBES_LEGS.get()));
    }

}
