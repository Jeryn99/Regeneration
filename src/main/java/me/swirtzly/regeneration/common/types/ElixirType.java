package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.types.ElixirRenderer;
import me.swirtzly.regeneration.common.capability.IRegen;
import net.minecraft.entity.player.PlayerEntity;

public class ElixirType implements RegenType<ElixirRenderer> {

    @Override
    public int getAnimationLength() {
        return 1111111;
    }

    @Override
    public ElixirRenderer getRenderer() {
        return ElixirRenderer.INSTANCE;
    }

    @Override
    public void onStartRegeneration(PlayerEntity player, IRegen capability) {

    }

    @Override
    public void onUpdateMidRegen(PlayerEntity player, IRegen capability) {

    }

    @Override
    public void onFinishRegeneration(PlayerEntity player, IRegen capability) {

    }

    @Override
    public double getAnimationProgress(IRegen cap) {
        return 280;
    }

    @Override
    public TypeManager.Type getTypeID() {
        return TypeManager.Type.CONFUSED;
    }

}
