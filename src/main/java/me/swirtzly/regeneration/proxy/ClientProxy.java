package me.swirtzly.regeneration.proxy;

import me.swirtzly.animateme.AnimationManager;
import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.animation.GeneralAnimations;
import me.swirtzly.regeneration.client.gui.BioContainerScreen;
import me.swirtzly.regeneration.client.rendering.layers.HandsLayer;
import me.swirtzly.regeneration.client.rendering.layers.RegenerationLayer;
import me.swirtzly.regeneration.client.rendering.model.GuardModel;
import me.swirtzly.regeneration.client.rendering.model.RobeModel;
import me.swirtzly.regeneration.client.rendering.tiles.ArchRender;
import me.swirtzly.regeneration.client.rendering.tiles.HandTileRenderer;
import me.swirtzly.regeneration.client.rendering.types.FieryRenderer;
import me.swirtzly.regeneration.client.rendering.types.TypeLayFadeRenderer;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.tiles.ArchTile;
import me.swirtzly.regeneration.common.tiles.HandInJarTile;
import me.swirtzly.regeneration.handlers.ClientHandler;
import me.swirtzly.regeneration.handlers.RegenObjects;
import me.swirtzly.regeneration.util.common.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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

        AnimationManager.registerAnimations(new GeneralAnimations(), new FieryRenderer(), new TypeLayFadeRenderer());

		ClientRegistry.bindTileEntitySpecialRenderer(HandInJarTile.class, new HandTileRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ArchTile.class, new ArchRender());

    }
	
	@Override
	public void closeGui() {
		Minecraft.getInstance().displayGuiScreen(null);
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	private static final RobeModel ROBES = new RobeModel();
	private static final GuardModel GUARD_HEAD = new GuardModel(EquipmentSlotType.HEAD);
	private static final GuardModel GUARD_CHEST = new GuardModel(EquipmentSlotType.CHEST);
	private static final GuardModel GUARD_LEGGINGS = new GuardModel(EquipmentSlotType.LEGS);
	private static final GuardModel GUARD_FEET = new GuardModel(EquipmentSlotType.FEET);

	public static BipedModel getArmorModel(ItemStack item) {
		if (item.getItem().getRegistryName().toString().contains("robes")) {
			return ROBES;
		}

		if (item.getItem() == RegenObjects.Items.GUARD_HEAD.get()) {
			return GUARD_HEAD;
		}

		if (item.getItem() == RegenObjects.Items.GUARD_CHEST.get()) {
			return GUARD_CHEST;
		}

		if (item.getItem() == RegenObjects.Items.GUARD_LEGGINGS.get()) {
			return GUARD_LEGGINGS;
		}

		if (item.getItem() == RegenObjects.Items.GUARD_FEET.get()) {
			return GUARD_FEET;
		}

		return GUARD_HEAD;
	}

}
