package mc.craig.software.regen.client.visual;

import mc.craig.software.regen.Regeneration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VisualManipulator {

    private static final Map<UUID, ResourceLocation> SKIN_CACHE = new HashMap<>();

    public static void addSkin(UUID uuid, ResourceLocation resourceLocation) {
        clearPlayersSkin(uuid);
        SKIN_CACHE.put(uuid, resourceLocation);
    }

    public static Optional<ResourceLocation> getSkin(UUID uuid) {
        if (SKIN_CACHE.containsKey(uuid)) {
            return Optional.of(SKIN_CACHE.get(uuid));
        }
        return Optional.empty();
    }

    public static void clearPlayersSkin(UUID uuid) {
        if (!SKIN_CACHE.containsKey(uuid)) return;
        ResourceLocation location = SKIN_CACHE.get(uuid);
     //   Minecraft.getInstance().getTextureManager().release(location);
        Regeneration.LOGGER.info("Releasing {}", location);
        SKIN_CACHE.remove(uuid);
    }


    public static void tickCache() {
        if (Minecraft.getInstance().level != null || SKIN_CACHE.isEmpty()) return;
        Regeneration.LOGGER.info("Clearing Skin Cache!");

        for (ResourceLocation value : SKIN_CACHE.values()) {
       //     Minecraft.getInstance().getTextureManager().release(value);
            Regeneration.LOGGER.info("Releasing {}", value);
        }

        SKIN_CACHE.clear();
    }

}
