package me.sub.common.handlers;

import me.sub.Regeneration;
import me.sub.common.capability.CapabilityRegeneration;
import me.sub.common.capability.IRegeneration;
import me.sub.config.RegenConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Sub
 * on 16/09/2018.
 */
@Mod.EventBusSubscriber(modid = Regeneration.MODID)
public class GracePeriodHandler {

	
    @SubscribeEvent
    public static void breakBlock(PlayerInteractEvent.LeftClickBlock e) {
        EntityPlayer player = e.getEntityPlayer();
        IRegeneration regenInfo = CapabilityRegeneration.get(player);
        boolean inGracePeriod = regenInfo.isInGracePeriod() && regenInfo.isGlowing();

        if (inGracePeriod) {
            regenInfo.setGlowing(false);
            regenInfo.setTicksGlowing(0);
            regenInfo.sync();
        }
    }

    @SubscribeEvent
    public static void registerLoot(LootTableLoadEvent e) {
        if (!e.getName().toString().toLowerCase().matches(RegenConfig.Loot.lootRegex) || RegenConfig.Loot.disableArch)
            return;

        LootCondition[] condAlways = new LootCondition[]{new RandomChance(1F)};
        LootEntry entry = new LootEntryTable(new ResourceLocation(Regeneration.MODID, "inject/arch_loot"), 1, 1, condAlways, "regeneration:arch-entry");
        LootPool lootPool = new LootPool(new LootEntry[]{entry}, condAlways, new RandomValueRange(1), new RandomValueRange(1), "regeneration:arch-pool");
        e.getTable().addPool(lootPool);
    }


}
