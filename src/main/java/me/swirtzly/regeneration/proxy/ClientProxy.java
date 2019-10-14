package me.swirtzly.regeneration.proxy;

import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.gui.parts.InventoryTabRegeneration;
import me.swirtzly.regeneration.client.rendering.entity.RenderItemOverride;
import me.swirtzly.regeneration.client.rendering.entity.RenderLindos;
import me.swirtzly.regeneration.client.rendering.entity.RenderWatcher;
import me.swirtzly.regeneration.client.rendering.layers.LayerHands;
import me.swirtzly.regeneration.client.rendering.layers.LayerRegeneration;
import me.swirtzly.regeneration.client.rendering.tile.RenderTileEntityHand;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.entity.EntityItemOverride;
import me.swirtzly.regeneration.common.entity.EntityLindos;
import me.swirtzly.regeneration.common.entity.EntityWatcher;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.compat.lucraft.LucraftCoreHandler;
import me.swirtzly.regeneration.util.EnumCompatModids;
import me.swirtzly.regeneration.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Map;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
        MinecraftForge.EVENT_BUS.register(new SkinChangingHandler());
        RenderingRegistry.registerEntityRenderingHandler(EntityItemOverride.class, RenderItemOverride::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLindos.class, RenderLindos::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityWatcher.class, RenderWatcher::new);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHandInJar.class, new RenderTileEntityHand());
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
        RegenKeyBinds.init();

        // Render layers ===========================================
        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        for (RenderPlayer renderPlayer : skinMap.values()) {
            renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
            renderPlayer.addLayer(new LayerHands(renderPlayer));
        }
        FileUtil.doSetupOnThread();

        // Galacticraft API for TABS ======================
        if (TabRegistry.getTabList().isEmpty()) {
            MinecraftForge.EVENT_BUS.register(new TabRegistry());
            TabRegistry.registerTab(new InventoryTabVanilla());
        }
        TabRegistry.registerTab(new InventoryTabRegeneration());

        // LC Core
        if (EnumCompatModids.LCCORE.isLoaded()) {
            LucraftCoreHandler.registerEntry();
        }
    }

}
