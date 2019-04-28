package me.suff.regeneration.proxy;

import me.suff.regeneration.client.RegenKeyBinds;
import me.suff.regeneration.client.rendering.LayerRegeneration;
import me.suff.regeneration.client.rendering.entity.RenderItemOverride;
import me.suff.regeneration.client.rendering.entity.RenderLindos;
import me.suff.regeneration.common.entity.EntityItemOverride;
import me.suff.regeneration.common.entity.EntityLindos;
import me.suff.regeneration.util.FileUtil;
import me.suff.regeneration.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
		Map<String, RenderPlayer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		for (RenderPlayer renderPlayer : skinMap.values()) {
			RenderUtil.setupArmorModelOverride(renderPlayer);
			renderPlayer.addLayer(new LayerRegeneration(renderPlayer)); // Add Regeneration Layer
		}
		FileUtil.doThreadStuff();
		
	}
	
}
