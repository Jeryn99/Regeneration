package me.suff.mc.regen.proxy;

import me.suff.mc.regen.client.RegenKeyBinds;
import me.suff.mc.regen.client.gui.parts.InventoryTabArch;
import me.suff.mc.regen.client.gui.parts.InventoryTabRegeneration;
import me.suff.mc.regen.client.rendering.entity.RenderItemOverride;
import me.suff.mc.regen.client.rendering.entity.RenderLindos;
import me.suff.mc.regen.client.rendering.entity.RenderWatcher;
import me.suff.mc.regen.client.rendering.layers.LayerHands;
import me.suff.mc.regen.client.rendering.layers.LayerRegeneration;
import me.suff.mc.regen.client.rendering.layers.RenderArchItem;
import me.suff.mc.regen.client.rendering.tile.RenderTileEntityHand;
import me.suff.mc.regen.client.skinhandling.SkinChangingHandler;
import me.suff.mc.regen.common.entity.EntityItemOverride;
import me.suff.mc.regen.common.entity.EntityLindos;
import me.suff.mc.regen.common.entity.EntityWatcher;
import me.suff.mc.regen.common.tiles.TileEntityHandInJar;
import me.suff.mc.regen.compat.lucraft.LucraftCoreHandler;
import me.suff.mc.regen.util.EnumCompatModids;
import me.suff.mc.regen.util.FileUtil;
import micdoodle8.mods.galacticraft.api.client.tabs.InventoryTabVanilla;
import micdoodle8.mods.galacticraft.api.client.tabs.TabRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.Map;

/**
 * Created by Sub on 17/09/2018.
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
            renderPlayer.addLayer(new RenderArchItem(renderPlayer));
        }
        FileUtil.doSetupOnThread();

        // Galacticraft API for TABS ======================
        if (TabRegistry.getTabList().isEmpty()) {
            MinecraftForge.EVENT_BUS.register(new TabRegistry());
            TabRegistry.registerTab(new InventoryTabVanilla());
        }
        TabRegistry.registerTab(new InventoryTabRegeneration());
        TabRegistry.registerTab(new InventoryTabArch());

        // LC Core
        if (EnumCompatModids.LCCORE.isLoaded()) {
            LucraftCoreHandler.registerEntry();
        }
    }

}
