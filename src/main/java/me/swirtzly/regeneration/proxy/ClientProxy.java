package me.swirtzly.regeneration.proxy;

import me.swirtzly.animateme.AnimationManager;
import me.swirtzly.regeneration.client.RegenKeyBinds;
import me.swirtzly.regeneration.client.animation.GeneralAnimations;
import me.swirtzly.regeneration.client.gui.BioContainerScreen;
import me.swirtzly.regeneration.client.rendering.layers.HandsLayer;
import me.swirtzly.regeneration.client.rendering.layers.RegenerationLayer;
import me.swirtzly.regeneration.client.rendering.model.GuardArmorNew;
import me.swirtzly.regeneration.client.rendering.model.GuardModel;
import me.swirtzly.regeneration.client.rendering.model.RobeModel;
import me.swirtzly.regeneration.client.rendering.model.RobesNew;
import me.swirtzly.regeneration.client.rendering.tiles.ArchRender;
import me.swirtzly.regeneration.client.rendering.tiles.HandTileRenderer;
import me.swirtzly.regeneration.client.rendering.types.FieryRenderer;
import me.swirtzly.regeneration.client.rendering.types.TypeLayFadeRenderer;
import me.swirtzly.regeneration.client.skinhandling.SkinManipulation;
import me.swirtzly.regeneration.common.item.DyeableClothingItem;
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
 * Created by Craig on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {

    private static final RobesNew ROBES = new RobesNew(EquipmentSlotType.CHEST);
    private static final RobeModel ROBES_OLD = new RobeModel();
    private static final RobesNew ROBES_HEAD = new RobesNew(EquipmentSlotType.HEAD);
    private static final RobesNew ROBES_LEGS = new RobesNew(EquipmentSlotType.LEGS);
    private static final GuardArmorNew GUARD_HEAD = new GuardArmorNew(EquipmentSlotType.HEAD);
    private static final GuardArmorNew GUARD_CHEST = new GuardArmorNew(EquipmentSlotType.CHEST);
    private static final GuardArmorNew GUARD_LEGGINGS = new GuardArmorNew(EquipmentSlotType.LEGS);
    private static final GuardArmorNew GUARD_FEET = new GuardArmorNew(EquipmentSlotType.FEET);

    private static final GuardModel GUARD_HEAD_OLD = new GuardModel(EquipmentSlotType.HEAD);
    private static final GuardModel GUARD_CHEST_OLD = new GuardModel(EquipmentSlotType.CHEST);
    private static final GuardModel GUARD_LEGGINGS_OLD = new GuardModel(EquipmentSlotType.LEGS);
    private static final GuardModel GUARD_FEET_OLD = new GuardModel(EquipmentSlotType.FEET);

    public static BipedModel getArmorModel(ItemStack item) {

        boolean swiftItem = item.getOrCreateTag().contains(DyeableClothingItem.SWIFT_KEY);

        if (item.getItem() == RegenObjects.Items.ROBES_HEAD.get()) {
            return ROBES_HEAD;
        }

        if (item.getItem() == RegenObjects.Items.ROBES_LEGS.get()) {
            return ROBES_LEGS;
        }

        if (item.getItem() == RegenObjects.Items.ROBES_CHEST.get()) {
            return swiftItem ? ROBES_OLD : ROBES;
        }

        if (item.getItem() == RegenObjects.Items.GUARD_HEAD.get()) {
            return swiftItem ? GUARD_HEAD_OLD : GUARD_HEAD;
        }

        if (item.getItem() == RegenObjects.Items.GUARD_CHEST.get()) {
            return swiftItem ? GUARD_CHEST_OLD : GUARD_CHEST;
        }

        if (item.getItem() == RegenObjects.Items.GUARD_LEGGINGS.get()) {
            return swiftItem ? GUARD_LEGGINGS_OLD : GUARD_LEGGINGS;
        }

        if (item.getItem() == RegenObjects.Items.GUARD_FEET.get()) {
            return swiftItem ? GUARD_FEET_OLD : GUARD_FEET;
        }

        return swiftItem ? GUARD_HEAD_OLD : GUARD_HEAD;
    }

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        ScreenManager.register(RegenObjects.Containers.BIO_CONTAINER.get(), BioContainerScreen::new);
    }

    @Override
    public void postInit() {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;
        super.postInit();
        RegenKeyBinds.init();

        // Render layers ===========================================
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap();
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
        Minecraft.getInstance().setScreen(null);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().level;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }

}
