package me.swirtzly.regeneration.common.trades;

import me.swirtzly.regeneration.handlers.RegenObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;

import java.util.Random;

/**
 * Created by Swirtzly
 * on 03/05/2020 @ 21:18
 */
public class Trades {

    private static Random RANDOM = new Random();

    public static VillagerTrades.ITrade[] genTrades() {
        return new VillagerTrades.ITrade[]{
                new TimelordTrade(new ItemStack(RegenObjects.Items.GAL_INGOT.get(), 25), new ItemStack(RegenObjects.Items.ARCH_PART.get()), RANDOM.nextInt(100), RANDOM.nextInt(100)),
                new TimelordTrade(new ItemStack(RegenObjects.Items.GAL_INGOT.get(), 55), new ItemStack(RegenObjects.Items.GAL_INGOT.get(), 55), new ItemStack(RegenObjects.Items.FOB_WATCH.get()), RANDOM.nextInt(100), RANDOM.nextInt(100)),
                new TimelordTrade(new ItemStack(RegenObjects.Items.GAL_INGOT.get(), 45), new ItemStack(RegenObjects.Blocks.HAND_JAR.get().asItem()), RANDOM.nextInt(100), RANDOM.nextInt(100))};
    }


    public static class TimelordTrade implements VillagerTrades.ITrade {

        private ItemStack coin2;
        private ItemStack coin;
        private ItemStack wares;

        private int xp;
        private int stock;

        public TimelordTrade(ItemStack coin, ItemStack coin2, ItemStack wares, int stock, int xp) {
            this.xp = xp;
            this.stock = stock;
            this.wares = wares;
            this.coin = coin;
            this.coin2 = coin2;
        }

        public TimelordTrade(ItemStack coin, ItemStack wares, int stock, int xp) {
            this(coin, ItemStack.EMPTY, wares, stock, xp);
        }

        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(coin, coin2, wares, stock, xp, 0F);
        }
    }


}


