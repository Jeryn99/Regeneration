package mc.craig.software.regen.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mc.craig.software.regen.common.item.HandItem;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.state.RegenStates;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.*;
import java.net.Socket;

public class RegenUtil {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static TagKey<Block> BANNED_BLOCKS = makeBlock("weeping_angels", "angel_proof");
    public static TagKey<Block> ARS = makeBlock("tardis", "ars");
    public static TagKey<Item> TIMELORD_CURRENCY = makeItem(RConstants.MODID, "timelord_currency");
    public static TagKey<Block> ZINC = makeBlock("forge", "ores/zinc");
    public static TagKey<Item> ZINC_INGOT = makeItem("forge", "ingots/zinc");
    public static TagKey<Biome> TIMELORD_SETTLEMENT = makeBiome("has_structure/timelord_settlement");
    public static TagKey<MobEffect> POST_REGEN_POTIONS = makeMobEffects("post_regen");

    public static RandomSource RAND = RandomSource.create();
    public static String[] USERNAMES = new String[]{};

    public static TagKey<Block> makeBlock(String domain, String path) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(domain, path));
    }

    private static TagKey<Biome> makeBiome(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(RConstants.MODID, name));
    }

    private static TagKey<MobEffect> makeMobEffects(String name) {
        return TagKey.create(Registry.MOB_EFFECT_REGISTRY, new ResourceLocation(RConstants.MODID, name));
    }

    public static TagKey<Item> makeItem(String domain, String path) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(domain, path));
    }

    public static double round(float value, int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }

    public static boolean shouldGiveCouncilAdvancement(ServerPlayer serverPlayerEntity) {
        EquipmentSlot[] equipmentSlotTypes = new EquipmentSlot[]{EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET};
        for (EquipmentSlot equipmentSlotType : equipmentSlotTypes) {
            if (!Registry.ITEM.getKey(serverPlayerEntity.getItemBySlot(equipmentSlotType).getItem()).getPath().contains("robes")) {
                return false;
            }
        }
        return true;
    }

    public static void setupNames() {
        if (USERNAMES.length == 0) {
            ResourceLocation resourceLocation = new ResourceLocation(RConstants.MODID, "names.json");
            Platform.getServer().getResourceManager().getResource(resourceLocation).ifPresent(resource -> {
                try {
                    InputStream stream = resource.open();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    stream.close();

                    USERNAMES = RegenUtil.GSON.fromJson(sb.toString(), String[].class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


    public static float randFloat(float min, float max) {
        return RAND.nextFloat() * (max - min) + min;
    }

    public static boolean doesHaveInternet() {
        try {
            Socket socket = new Socket("www.google.com", 80);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static byte[] fileToBytes(File file) {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    public static String colorToHex(Color color) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(color.getRGB() & 0xffffff));
        while (hex.length() < 6) {
            hex.insert(0, "0");
        }
        return "#" + hex;
    }

    public static void spawnHandIfPossible(LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.getItem() instanceof DiggerItem || itemStack.getItem() instanceof SwordItem) {
            RegenerationData.get(livingEntity).ifPresent((data) -> {
                if (data.regenState() == RegenStates.POST && livingEntity.isShiftKeyDown() & data.handState() == IRegen.Hand.NO_GONE) {
                    HandItem.createHand(livingEntity);
                }
            });
        }
    }

    public interface IEnum<E extends Enum<E>> {
        int ordinal();

        default E next() {
            E[] ies = this.getAllValues();
            return this.ordinal() != ies.length - 1 ? ies[this.ordinal() + 1] : null;
        }

        default E previous() {
            return this.ordinal() != 0 ? this.getAllValues()[this.ordinal() - 1] : null;
        }

        @SuppressWarnings("unchecked")
        default E[] getAllValues() {
            IEnum[] ies = this.getClass().getEnumConstants();
            return (E[]) ies;
        }
    }


}
