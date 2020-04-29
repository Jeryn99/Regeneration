package me.swirtzly.regeneration.proxy;

import me.swirtzly.animateme.AnimationManager;
import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.animation.GeneralAnimations;
import me.swirtzly.regeneration.client.gui.BioContainerScreen;
import me.swirtzly.regeneration.client.rendering.layers.HandsLayer;
import me.swirtzly.regeneration.client.rendering.layers.RegenerationLayer;
import me.swirtzly.regeneration.client.rendering.tiles.ArchRender;
import me.swirtzly.regeneration.client.rendering.tiles.RenderTileEntityHand;
import me.swirtzly.regeneration.client.rendering.types.FieryRenderer;
import me.swirtzly.regeneration.client.rendering.types.TypeLayFadeRenderer;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.tiles.ArchTile;
import me.swirtzly.regeneration.common.tiles.TileEntityHandInJar;
import me.swirtzly.regeneration.handlers.CameraHandler;
import me.swirtzly.regeneration.handlers.ClientHandler;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Map;

/**
 * Created by Sub on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void init() {
		super.init();
		ScreenManager.registerFactory(RegenObjects.Containers.BIO_CONTAINER.get(), BioContainerScreen::new);
	}
	
	@Override
	public void postInit() {
		if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;
		super.postInit();
		RegenKeyBinds.init();

        // Render layers ===========================================
		Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		for (PlayerRenderer renderPlayer : skinMap.values()) {
			renderPlayer.addLayer(new RegenerationLayer(renderPlayer)); // Add Regeneration Layer
			renderPlayer.addLayer(new HandsLayer(renderPlayer));
		}

        FileUtil.doSetupOnThread();
		MinecraftForge.EVENT_BUS.register(new SkinManipulation());
		MinecraftForge.EVENT_BUS.register(new ClientHandler());
		MinecraftForge.EVENT_BUS.register(new CameraHandler());

        AnimationManager.registerAnimations(new GeneralAnimations(), new FieryRenderer(), new TypeLayFadeRenderer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHandInJar.class, new RenderTileEntityHand());
        ClientRegistry.bindTileEntitySpecialRenderer(ArchTile.class, new ArchRender());

    }
	
	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

    @Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

}
