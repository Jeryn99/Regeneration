package me.swirtzly.regeneration.common.types;

import me.swirtzly.regeneration.client.rendering.types.TypeElixirRenderer;
import me.swirtzly.regeneration.common.capability.IRegeneration;
import net.minecraft.entity.player.PlayerEntity;

public class TypeElixir implements IRegenType<TypeElixirRenderer> {

    @Override
    public int getAnimationLength() {
        return 1111111;
    }

    @Override
    public TypeElixirRenderer getRenderer() {
        return TypeElixirRenderer.INSTANCE;
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
    public TypeHandler.RegenType getTypeID() {
        return TypeHandler.RegenType.CONFUSED;
    }

}
