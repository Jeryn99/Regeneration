package mc.craig.software.regen.util;

import com.mojang.authlib.GameProfile;
import mc.craig.software.regen.common.traits.TraitRegistry;
import mc.craig.software.regen.common.traits.trait.TraitBase;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class RTextHelper {

    public static MutableComponent createTextComponentWithTip(String text, String tooltipText) {
        //Always surround tool tip items with brackets
        MutableComponent textComponent = Component.literal("[" + text + "]");
        textComponent.withStyle(style -> style.applyFormat(ChatFormatting.GREEN)//color tool tip items green
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(tooltipText)))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tooltipText)));
        return textComponent;
    }

    /**
     * For getting player tool tip. If player has been online since server start, then will show their display name.
     */
    public static MutableComponent getPlayerTextObject(ServerLevel world, UUID id) {
        GameProfile profileByUUID = world.getServer().getProfileCache().get(id).get();
        String playerName = profileByUUID != null ? profileByUUID.getName() : "OFFLINE Player";
        return createTextComponentWithTip(playerName, id.toString());
    }

    public static MutableComponent getEntityTextObject(ServerLevel world, UUID id) {
        Entity entity = world.getEntity(id);
        String entityName = entity != null ? entity.getName().getString() : "Null Entity";
        return createTextComponentWithTip(entityName, id.toString());
    }

    public static String formatTraitName(TraitBase trait) {
        String original = TraitRegistry.TRAITS_REGISTRY.getKey(trait).getPath().trim().replace("	", "").replace("_", " ");
        String output = Arrays.stream(original.split("\\s+"))
                .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
                .collect(Collectors.joining(" "));
        return output;
    }

    public static MutableComponent getTraitTextObject(TraitBase trait) {
        if (trait != null)
            return createTextComponentWithTip(formatTraitName(trait), TraitRegistry.TRAITS_REGISTRY.getKey(trait).toString());
        return createTextComponentWithTip("Null Trait", "Null");
    }
}
