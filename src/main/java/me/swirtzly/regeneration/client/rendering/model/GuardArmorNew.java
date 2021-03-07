package me.swirtzly.regeneration.client.rendering.model;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.common.capability.RegenCap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Created by Swirtzly
 * on 08/05/2020 @ 11:28
 */
public class GuardArmorNew extends BipedModel {
    private final RendererModel Head;
    private final RendererModel Body;
    private final RendererModel RightArm;
    private final RendererModel LeftArm;
    private final RendererModel RightLeg;
    private final RendererModel LeftLeg;

    EquipmentSlotType type = EquipmentSlotType.CHEST;

    public GuardArmorNew(EquipmentSlotType type) {
        this.type = type;
        textureWidth = 80;
        textureHeight = 80;

        Head = new RendererModel(this);
        Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        Head.cubeList.add(new ModelBox(Head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        Head.cubeList.add(new ModelBox(Head, 48, 64, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F, false));
        Head.cubeList.add(new ModelBox(Head, 0, 64, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6F, false));

        Body = new RendererModel(this);
        Body.setRotationPoint(0.0F, 0.0F, 0.0F);
        Body.cubeList.add(new ModelBox(Body, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));
        Body.cubeList.add(new ModelBox(Body, 16, 32, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F, false));
        Body.cubeList.add(new ModelBox(Body, 32, 69, -4.0F, 10.0F, -2.0F, 8, 6, 4, 0.25F, false));

        RightArm = new RendererModel(this);
        RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        RightArm.cubeList.add(new ModelBox(RightArm, 40, 16, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F, false));
        RightArm.cubeList.add(new ModelBox(RightArm, 40, 32, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        LeftArm = new RendererModel(this);
        LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        LeftArm.cubeList.add(new ModelBox(LeftArm, 32, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F, false));
        LeftArm.cubeList.add(new ModelBox(LeftArm, 48, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        RightLeg = new RendererModel(this);
        RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
        RightLeg.cubeList.add(new ModelBox(RightLeg, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        RightLeg.cubeList.add(new ModelBox(RightLeg, 0, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        LeftLeg = new RendererModel(this);
        LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        LeftLeg.cubeList.add(new ModelBox(LeftLeg, 16, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        LeftLeg.cubeList.add(new ModelBox(LeftLeg, 0, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        bipedHead = Head;
        bipedBody = Body;
        bipedLeftArm = LeftArm;
        bipedRightArm = RightArm;
        bipedLeftLeg = LeftLeg;
        bipedRightLeg = RightLeg;
    }

    @Override
    public void render(LivingEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        // head.render(f5);

        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        RegenCap.get(entity).ifPresent((data) -> {
            data.getRegenType().create().getRenderer().animateEntity(this, entity, f, f1, f2, f3, f4, f5);
        });

            if (type == EquipmentSlotType.HEAD) {
            if (entity instanceof ArmorStandEntity) {
                GlStateManager.rotatef(90, 0F, 1, 0F);
            }
            Head.render(f5);
        }

        if (type == EquipmentSlotType.CHEST) {
            Body.render(f5);
            RightArm.render(f5);
            LeftArm.render(f5);
        }

        if (type == EquipmentSlotType.LEGS || type == EquipmentSlotType.FEET) {
            RightLeg.render(f5);
            LeftLeg.render(f5);
        }
    }
}
