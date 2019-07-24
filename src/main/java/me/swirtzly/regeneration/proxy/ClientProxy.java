package me.swirtzly.regeneration.proxy;

import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.rendering.layers.LayerHands;
import me.swirtzly.regeneration.client.rendering.layers.LayerRegeneration;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.util.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Map;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public void postInit() {
		if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;
		super.postInit();
		RegenKeyBinds.init();
		
		// Render layers ===========================================
		Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		for (PlayerRenderer renderPlayer : skinMap.values()) {
			renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
			renderPlayer.addLayer(new LayerHands(renderPlayer));
		}
		FileUtil.doSetupOnThread();
		MinecraftForge.EVENT_BUS.register(new SkinChangingHandler());

	}


}
