package me.swirtzly.regeneration.client.rendering.model;

import me.swirtzly.regeneration.common.capability.RegenCap;
import me.swirtzly.regeneration.common.entity.TimelordEntity;
import me.swirtzly.regeneration.common.item.GunItem;
import me.swirtzly.regeneration.util.common.PlayerUtil;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;

/**
 * Created by Swirtzly
 * on 08/05/2020 @ 11:28
 */
public class TimelordGuardModel extends BipedModel<TimelordEntity> {
    private final RendererModel Head;
    private final RendererModel Body;
    private final RendererModel RightArm;
    private final RendererModel LeftArm;
    private final RendererModel RightLeg;
    private final RendererModel LeftLeg;

    public TimelordGuardModel() {
        texWidth = 80;
        texHeight = 80;

        Head = new RendererModel(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        Head.cubes.add(new ModelBox(Head, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F, false));
        Head.cubes.add(new ModelBox(Head, 48, 64, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F, false));
        Head.cubes.add(new ModelBox(Head, 0, 64, -4.0F, -8.0F, -4.0F, 8, 8, 8, 0.6F, false));

        Body = new RendererModel(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        Body.cubes.add(new ModelBox(Body, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F, false));
        Body.cubes.add(new ModelBox(Body, 16, 32, -4.0F, 0.0F, -2.0F, 8, 12, 4, 0.25F, false));
        Body.cubes.add(new ModelBox(Body, 32, 69, -4.0F, 10.0F, -2.0F, 8, 6, 4, 0.25F, false));

        RightArm = new RendererModel(this);
        RightArm.setPos(-5.0F, 2.0F, 0.0F);
        RightArm.cubes.add(new ModelBox(RightArm, 40, 16, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F, false));
        RightArm.cubes.add(new ModelBox(RightArm, 40, 32, -2.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        LeftArm = new RendererModel(this);
        LeftArm.setPos(5.0F, 2.0F, 0.0F);
        LeftArm.cubes.add(new ModelBox(LeftArm, 32, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F, false));
        LeftArm.cubes.add(new ModelBox(LeftArm, 48, 48, -1.0F, -2.0F, -2.0F, 3, 12, 4, 0.25F, false));

        RightLeg = new RendererModel(this);
        RightLeg.setPos(-1.9F, 12.0F, 0.0F);
        RightLeg.cubes.add(new ModelBox(RightLeg, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        RightLeg.cubes.add(new ModelBox(RightLeg, 0, 32, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        LeftLeg = new RendererModel(this);
        LeftLeg.setPos(1.9F, 12.0F, 0.0F);
        LeftLeg.cubes.add(new ModelBox(LeftLeg, 16, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F, false));
        LeftLeg.cubes.add(new ModelBox(LeftLeg, 0, 48, -2.0F, 0.0F, -2.0F, 4, 12, 4, 0.25F, false));

        head = Head;
        body = Body;
        leftArm = LeftArm;
        rightArm = RightArm;
        leftLeg = LeftLeg;
        rightLeg = RightLeg;
    }

    @Override
    public void render(TimelordEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        // head.render(f5);
        Head.render(f5);
        Body.render(f5);
        RightArm.render(f5);
        LeftArm.render(f5);
        RightLeg.render(f5);
        LeftLeg.render(f5);

        Head.neverRender = false;
    }

    @Override
    public void setupAnim(TimelordEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        RegenCap.get(entityIn).ifPresent((data) -> {
            data.getRegenType().create().getRenderer().animateEntity(this, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

            if (entityIn.getMainHandItem().getItem() instanceof GunItem && data.getState() != PlayerUtil.RegenState.REGENERATING) {
                rightArmPose = ArmPose.BOW_AND_ARROW;
            }

            if (data.getState() == PlayerUtil.RegenState.REGENERATING) {
                rightArmPose = ArmPose.EMPTY;
            }
        });
    }
}
