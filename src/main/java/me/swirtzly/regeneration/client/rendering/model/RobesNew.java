package me.swirtzly.regeneration.client.rendering.model;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.14
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.util.ResourceLocation;

public class RobesNew extends BipedModel {
    private final RendererModel Head;
    private final RendererModel Body;
    private final RendererModel RightArm;
    private final RendererModel LeftArm;
    private final RendererModel RightLeg;
    private final RendererModel LeftLeg;
    private final RendererModel bb_main;

    EquipmentSlotType type = EquipmentSlotType.CHEST;

    public RobesNew(EquipmentSlotType type) {
        this.type = type;
        texWidth = 100;
        texHeight = 100;

        Head = new RendererModel(this);
        Head.setPos(0.0F, 0.0F, 0.0F);


        Body = new RendererModel(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.cubes.add(new ModelBox(Body, 54, 16, -5.0F, -0.25F, 3.0F, 10, 23, 0, 0.0F, false));
        Body.cubes.add(new ModelBox(Body, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));
        Body.cubes.add(new ModelBox(Body, 16, 32, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F, false));

        RightArm = new RendererModel(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);
        RightArm.cubes.add(new ModelBox(RightArm, 40, 16, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));
        RightArm.cubes.add(new ModelBox(RightArm, 40, 32, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F * 2, false));

        LeftArm = new RendererModel(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);
        LeftArm.cubes.add(new ModelBox(LeftArm, 32, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));
        LeftArm.cubes.add(new ModelBox(LeftArm, 48, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F * 2, false));

        RightLeg = new RendererModel(this);
        RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        RightLeg.cubes.add(new ModelBox(RightLeg, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        RightLeg.cubes.add(new ModelBox(RightLeg, 0, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        LeftLeg = new RendererModel(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.cubes.add(new ModelBox(LeftLeg, 16, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        LeftLeg.cubes.add(new ModelBox(LeftLeg, 0, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        bb_main = new RendererModel(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);
        bb_main.cubes.add(new ModelBox(bb_main, 0, 84, -7.5F, -24.275F, -2.5F, 15, 3, 5, 0.0F, false));
        bb_main.cubes.add(new ModelBox(bb_main, 0, 64, -7.5F, -36.275F, -2.5F, 15, 12, 8, 0.0F, false));

        head = Head;
        body = Body;
        leftArm = LeftArm;
        rightArm = RightArm;
        leftLeg = LeftLeg;
        rightLeg = RightLeg;

    }

    @Override
    public void render(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        // head.render(f5);

        setupAnim(entity, f, f1, f2, f3, f4, f5);
        RegenCap.get(entity).ifPresent((data) -> {
            data.getRegenType().create().getRenderer().animateEntity(this, entity, f, f1, f2, f3, f4, f5);
        });
        if (type == EquipmentSlotType.HEAD) {
            if (entity instanceof ArmorStandEntity) {
                GlStateManager.rotatef(180, 0F, 1, 0F);
            }
            bb_main.render(f5);
        }

        if (type == EquipmentSlotType.CHEST) {
            Body.render(f5);
            RightArm.render(f5);
            LeftArm.render(f5);
            GlStateManager.pushMatrix();
            Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("regeneration:textures/entity/armour/robes_color_sections.png"));
            IDyeableArmorItem iDyeableArmorItem = (IDyeableArmorItem) entity.getItemBySlot(EquipmentSlotType.CHEST).getItem();
            int color = iDyeableArmorItem.getColor(entity.getItemBySlot(EquipmentSlotType.CHEST));
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            GlStateManager.color4f(1.0F * red, 1.0F * green, 1.0F * blue, 1F);
            Body.render(f5);
            RightArm.render(f5);
            LeftArm.render(f5);
            Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("regeneration:textures/entity/armour/robes.png"));
            GlStateManager.popMatrix();
        }

        if (type == EquipmentSlotType.LEGS || type == EquipmentSlotType.FEET) {
            GlStateManager.pushMatrix();
            Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("regeneration:textures/entity/armour/robes_color_sections.png"));
            IDyeableArmorItem iDyeableArmorItem = (IDyeableArmorItem) entity.getItemBySlot(EquipmentSlotType.LEGS).getItem();
            int color = iDyeableArmorItem.getColor(entity.getItemBySlot(EquipmentSlotType.LEGS));
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            GlStateManager.color4f(1.0F * red, 1.0F * green, 1.0F * blue, 1F);
            RightLeg.render(f5);
            LeftLeg.render(f5);
            Minecraft.getInstance().getTextureManager().bind(new ResourceLocation("regeneration:textures/entity/armour/robes.png"));
            GlStateManager.popMatrix();
        }
    }
}