package me.suff.mc.regen.proxy;

import me.suff.mc.regen.animateme.AnimationManager;
import me.suff.mc.regen.client.RegenKeyBinds;
import me.suff.mc.regen.client.animation.GeneralAnimations;
import me.suff.mc.regen.client.gui.BioContainerScreen;
import me.suff.mc.regen.client.rendering.layers.HandsLayer;
import me.suff.mc.regen.client.rendering.layers.RegenerationLayer;
import me.suff.mc.regen.client.rendering.model.GuardArmorNew;
import me.suff.mc.regen.client.rendering.model.RobesNew;
import me.suff.mc.regen.client.rendering.tiles.ArchRender;
import me.suff.mc.regen.client.rendering.tiles.HandTileRenderer;
import me.suff.mc.regen.client.rendering.types.FieryRenderer;
import me.suff.mc.regen.client.rendering.types.TypeLayFadeRenderer;
import me.suff.mc.regen.client.skinhandling.SkinManipulation;
import me.suff.mc.regen.common.tiles.ArchTile;
import me.suff.mc.regen.common.tiles.HandInJarTile;
import me.suff.mc.regen.handlers.ClientHandler;
import me.suff.mc.regen.handlers.RegenObjects;
import me.suff.mc.regen.util.common.FileUtil;
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
    private static final RobesNew ROBES_HEAD = new RobesNew(EquipmentSlotType.HEAD);
    private static final RobesNew ROBES_LEGS = new RobesNew(EquipmentSlotType.LEGS);
    private static final GuardArmorNew GUARD_HEAD = new GuardArmorNew(EquipmentSlotType.HEAD);
    private static final GuardArmorNew GUARD_CHEST = new GuardArmorNew(EquipmentSlotType.CHEST);
    private static final GuardArmorNew GUARD_LEGGINGS = new GuardArmorNew(EquipmentSlotType.LEGS);
    private static final GuardArmorNew GUARD_FEET = new GuardArmorNew(EquipmentSlotType.FEET);


    //TODO Turn this into a hashmap again
    public static BipedModel getArmorModel(ItemStack item) {

        if (item.getItem() == RegenObjects.Items.ROBES_HEAD.get()) {
            return ROBES_HEAD;
        }

        if (item.getItem() == RegenObjects.Items.ROBES_LEGS.get()) {
            return ROBES_LEGS;
        }

        if (item.getItem() == RegenObjects.Items.ROBES_CHEST.get()) {
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