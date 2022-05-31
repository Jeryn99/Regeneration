package me.suff.mc.regen.util;

import me.suff.mc.regen.Regeneration;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;

import static me.suff.mc.regen.Regeneration.GSON;

public class RegenUtil {

    public static ITag.INamedTag<Block> BANNED_BLOCKS = makeBlock("weeping_angels", "angel_proof");
    public static ITag.INamedTag<Block> ARS = makeBlock("tardis", "ars");
    public static ITag.INamedTag<Item> TIMELORD_CURRENCY = makeItem(RConstants.MODID, "timelord_currency");
    public static ITag.INamedTag<Block> FORGE_ZINC = makeBlock("forge", "ore/zinc");
    public static ITag.INamedTag<Item> FORGE_ZINC_ITEM = makeItem("forge", "ore/zinc");

    public static Random RAND = new Random();
    public static String[] USERNAMES = new String[]{};

    public static Vector3d vecFromPos(Vector3i location) {
        return new Vector3d(location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5);
    }


    public static void versionCheck(PlayerEntity playerEntity) {
        VersionChecker.CheckResult version = VersionChecker.getResult(ModList.get().getModFileById(RConstants.MODID).getMods().get(0));
        if (version.status == VersionChecker.Status.OUTDATED) {
            TranslationTextComponent click = new TranslationTextComponent("Download");
            click.setStyle(Style.EMPTY.setUnderlined(true).withColor(TextFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/regeneration/files")));

            TranslationTextComponent translationTextComponent = new TranslationTextComponent(TextFormatting.BOLD + "[" + TextFormatting.RESET + TextFormatting.YELLOW + "Regeneration" + TextFormatting.RESET + TextFormatting.BOLD + "]");
            translationTextComponent.append(new TranslationTextComponent(" New Update Found: (" + version.target + ") ").append(click));

            PlayerUtil.sendMessage(playerEntity, translationTextComponent, false);
        }
    }

    public static ITag.INamedTag<Block> makeBlock(String domain, String path) {
        return BlockTags.createOptional(new ResourceLocation(domain, path));
    }

    public static ITag.INamedTag<Item> makeItem(String domain, String path) {
        return ItemTags.createOptional(new ResourceLocation(domain, path));
    }

    public static double round(float value, int scale) {
        return Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale);
    }

    public static void setupNames() {
        if (USERNAMES.length == 0) {
            try {

                ResourceLocation resourceLocation = new ResourceLocation(RConstants.MODID, "names.json");
                InputStream stream = ServerLifecycleHooks.getCurrentServer().getDataPackRegistries().getResourceManager().getResource(resourceLocation).getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                reader.close();
                stream.close();

                USERNAMES = GSON.fromJson(sb.toString(), String[].class);

            } catch (IOException e) {
                Regeneration.LOG.catching(e);
            }
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

    public static String encodeFileToBase64Binary(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return new String(encoded, StandardCharsets.US_ASCII);
    }

    public static String colorToHex(Color color) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(color.getRGB() & 0xffffff));
        while (hex.length() < 6) {
            hex.insert(0, "0");
        }
        return "#" + hex;
    }

    public static String randomCode() {
        String code = String.valueOf(UUID.randomUUID());
        return code.replaceAll("-", "").replaceAll("_", "");
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
