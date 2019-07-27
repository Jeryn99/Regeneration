package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.types.ElixirRenderer;
import me.swirtzly.regeneration.common.capability.IRegeneration;
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
    public void onStartRegeneration(PlayerEntity player, IRegeneration capability) {

    }

    @Override
    public void onUpdateMidRegen(PlayerEntity player, IRegeneration capability) {

    }

    @Override
    public void onFinishRegeneration(PlayerEntity player, IRegeneration capability) {

    }

    @Override
    public double getAnimationProgress(IRegeneration cap) {
        return 280;
    }

    @Override
    public TypeManager.Type getTypeID() {
        return TypeManager.Type.CONFUSED;
    }

}
