package mc.craig.software.regen.fabric;

import com.google.common.primitives.Longs;
import mc.craig.software.regen.client.RKeybinds;
import mc.craig.software.regen.client.rendering.model.RModels;
import mc.craig.software.regen.client.ArmorModelManager;
import mc.craig.software.regen.client.sound.SoundReverbListener;
import mc.craig.software.regen.fabric.handlers.ClientEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class RegenerationFabricClient implements ClientModInitializer {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static void register(PackType type, PreparableReloadListener listener) {
        var bytes = new byte[8];
        RANDOM.nextBytes(bytes);
        var id = new ResourceLocation("regen:reload_" + StringUtils.leftPad(Math.abs(Longs.fromByteArray(bytes)) + "", 19, '0'));
        ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return id;
            }

            @Override
            public String getName() {
                return listener.getName();
            }

            @Override
            public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {
                return listener.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
            }
        });
    }

    @Override
    public void onInitializeClient() {
        ClientEvents.init();
        RModels.init();
        KeyBindingHelper.registerKeyBinding(RKeybinds.FORCE_REGEN);
        KeyBindingHelper.registerKeyBinding(RKeybinds.REGEN_GUI);
        register(PackType.CLIENT_RESOURCES, new SoundReverbListener());
        register(PackType.CLIENT_RESOURCES, new ArmorModelManager());
    }
}
