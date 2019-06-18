package me.swirtzly.regeneration.client.rendering.entity;

import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderDupe extends Render {
    public RenderDupe(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float ticks) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        IRegeneration data = CapabilityRegeneration.getForPlayer(player);


        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && data.getType() == TypeHandler.RegenType.CONFUSED && data.getState() == RegenState.REGENERATING) {

            if (player.isElytraFlying()) {
                return;
            }
            Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(player);
            RenderPlayer playerRenderer = (RenderPlayer) render;
            ModelPlayer playerModel = playerRenderer.getMainModel();

            ItemStack tempStackMain = player.inventory.getCurrentItem();
            ItemStack tempStackSecond = player.inventory.offHandInventory.get(0);
            player.inventory.removeStackFromSlot(player.inventory.currentItem);
            player.inventory.offHandInventory.set(0, ItemStack.EMPTY);

            //Temporarily remove helmet prior to rendering.
            ItemStack helmetStack = player.inventory.armorInventory.get(3);
            player.inventory.armorInventory.set(3, ItemStack.EMPTY);
            if (player.isPlayerSleeping()) {
                playerRenderer.doRender(player, player.posX - entity.posX + x, player.posY - entity.posY + y, player.posZ - entity.posZ + z, player.renderYawOffset, ticks);
            } else {
                double renderOffset = player.prevRenderYawOffset - (player.prevRenderYawOffset - player.renderYawOffset) * ticks;
                playerRenderer.doRender(player, player.posX - entity.posX + x + 0.35 * Math.sin(Math.toRadians(renderOffset)), player.posY - entity.posY + y, player.posZ - entity.posZ + z - 0.35 * Math.cos(Math.toRadians(renderOffset)), (float) renderOffset, ticks);
            }
            player.inventory.armorInventory.set(3, helmetStack);

            playerModel.bipedHead.isHidden = true;
            playerModel.bipedHeadwear.isHidden = true;
            playerModel.bipedHead.isHidden = false;
            playerModel.bipedHeadwear.isHidden = false;
            player.inventory.setInventorySlotContents(player.inventory.currentItem, tempStackMain);
            player.inventory.offHandInventory.set(0, tempStackSecond);
            playerModel.bipedLeftArm.isHidden = false;
            playerModel.bipedRightArm.isHidden = false;
            playerModel.bipedLeftArmwear.isHidden = false;
            playerModel.bipedRightArmwear.isHidden = false;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
