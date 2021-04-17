package me.suff.mc.regen.util;

import com.mojang.authlib.GameProfile;
import me.suff.mc.regen.common.traits.AbstractTrait;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.server.ServerWorld;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class RTextHelper {

    public static TextComponent createTextComponentWithTip(String text, String tooltipText) {
        //Always surround tool tip items with brackets
        TextComponent textComponent = new StringTextComponent("[" + text + "]");
        textComponent.withStyle(style -> {
            return style.applyFormat(TextFormatting.GREEN)//color tool tip items green
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent(tooltipText)))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tooltipText));
        });
        return textComponent;
    }

    public static TextComponent getTraitTextObject(AbstractTrait trait) {
        if (trait != null)
            return createTextComponentWithTip(formatTraitName(trait), trait.getRegistryName().toString());
        return createTextComponentWithTip("Null Trait", "Null");
    }

    /**
     * For getting player tool tip. If player has been online since server start, then will show their display name.
     */
    public static TextComponent getPlayerTextObject(ServerWorld world, UUID id) {
        GameProfile profileByUUID = world.getServer().getProfileCache().get(id);
        String playerName = profileByUUID != null ? profileByUUID.getName() : "OFFLINE Player";
        return createTextComponentWithTip(playerName, id.toString());
    }

    public static TextComponent getEntityTextObject(ServerWorld world, UUID id) {
        Entity entity = world.getEntity(id);
        String entityName = entity != null ? entity.getName().getString() : "Null Entity";
        return createTextComponentWithTip(entityName, id.toString());
    }

    public static String formatTraitName(AbstractTrait trait) {
        String original = trait.getRegistryName().getPath().trim().replace("	", "").replace("_", " ");
        String output = Arrays.stream(original.split("\\s+"))
                .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
                .collect(Collectors.joining(" "));
        return output;
    }

}
