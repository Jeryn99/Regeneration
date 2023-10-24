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

    /**
     * Creates a MutableComponent with the given text and a tooltip.
     * The text will be displayed in green, and hovering over it will show the tooltip text.
     * Clicking on the text will also copy the tooltip text to the clipboard.
     *
     * @param text        the text to be displayed
     * @param tooltipText the text to be displayed in the tooltip and copied to the clipboard
     * @return a MutableComponent with the given text and tooltip
     */
    public static MutableComponent createTextComponentWithTip(String text, String tooltipText) {
        // Surround the text with brackets
        MutableComponent textComponent = Component.literal("[" + text + "]");
        textComponent.withStyle(style -> style
                // Set the text color to green
                .applyFormat(ChatFormatting.GREEN)
                // Set the hover event to show the tooltip text
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(tooltipText)))
                // Set the click event to copy the tooltip text to the clipboard
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, tooltipText))
        );
        return textComponent;
    }

    /**
     * Returns a text component with the player's name and a tooltip showing their UUID.
     * If the player is offline or their profile cannot be found in the cache, the text component
     * will show "OFFLINE Player" as the name.
     *
     * @param world the world in which the player exists
     * @param id    the player's UUID
     * @return a text component with the player's name and UUID as a tooltip
     */
    public static MutableComponent getPlayerTextObject(ServerLevel world, UUID id) {
        GameProfile profileByUUID = world.getServer().getProfileCache().get(id).get();
        String playerName = "OFFLINE Player";
        if (profileByUUID != null) {
            playerName = profileByUUID.getName();
        }
        return createTextComponentWithTip(playerName, id.toString());
    }

    /**
     * Gets a text component representing an entity in the world.
     *
     * @param world the world containing the entity
     * @param id    the UUID of the entity
     * @return a text component with the entity's name and a tooltip with the entity's UUID
     */
    public static MutableComponent getEntityTextObject(ServerLevel world, UUID id) {
        if (world == null) {
            throw new IllegalArgumentException("world cannot be null");
        }

        Entity entity = world.getEntity(id);
        String entityName = entity != null ? entity.getName().getString() : "Null Entity";
        return createTextComponentWithTip(entityName, id.toString());
    }

    /**
     * Formats the given trait's name by capitalizing the first letter of each word and removing underscores.
     *
     * @param trait the trait to format
     * @return the formatted trait name, or "null" if the trait is null
     * @throws NullPointerException if the trait is null
     */
    public static String formatTraitName(TraitBase trait) {
        if (trait == null) {
            return "null";
        }
        String original = TraitRegistry.TRAITS_REGISTRY.getKey(trait).getPath().trim().replace(" ", "").replace("_", " ");
        return Arrays.stream(original.split("\s+"))
                .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * Returns a text component for the given trait. The text component will have the
     * trait's name formatted in title case and will have a tooltip showing the trait's
     * registry name.
     *
     * @param trait the trait to create a text component for
     * @return a text component for the given trait
     */
    public static MutableComponent getTraitTextObject(TraitBase trait) {
        if (trait != null) {
            // Format the trait name in title case
            String formattedName = formatTraitName(trait);

            // Create the text component with a tooltip showing the trait's registry name
            return createTextComponentWithTip(formattedName, TraitRegistry.TRAITS_REGISTRY.getKey(trait).toString());
        }
        // Return a text component for a "null" trait
        return createTextComponentWithTip("Null Trait", "Null");
    }
}
