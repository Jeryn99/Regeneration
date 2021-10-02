package me.suff.mc.regen.common.capability;

import me.suff.mc.regen.RegenConfig;
import me.suff.mc.regen.api.RegenerationEvent;
import me.suff.mc.regen.client.skinhandling.SkinInfo;
import me.suff.mc.regen.common.types.RegenTypes;
import me.suff.mc.regen.util.common.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Craig on 16/09/2018.
 */
public interface IRegen extends INBTSerializable<CompoundNBT> {

    LivingEntity getLivingEntity();

    int getRegenerationsLeft();

    void setRegenerationsLeft(int amount);

    void triggerRegeneration();

    void tick();

    void synchronise();

    CompoundNBT getStyle();

    void setStyle(CompoundNBT nbt);

    Vec3d getPrimaryColor();

    Vec3d getSecondaryColor();

    /**
     * Returns if the player is currently <i>able to</i> regenerate
     */
    default boolean canRegenerate() {
        return (RegenConfig.COMMON.infiniteRegeneration.get() || getRegenerationsLeft() > 0) && getLivingEntity().y > 0 && !MinecraftForge.EVENT_BUS.post(new RegenerationEvent(getLivingEntity()));
    }

    void receiveRegenerations(int amount);

    void extractRegeneration(int amount);

    PlayerUtil.RegenState getState();

    RegenTypes getRegenType();

    void setRegenType(RegenTypes type);

    IStateManager getStateManager();

    String getEncodedSkin();

    void setEncodedSkin(String string);

    SkinInfo.SkinType getSkinType();

    void setSkinType(String skinType);

    PlayerUtil.EnumChoices getPreferredModel();

    void setPreferredModel(String skinType);

    boolean areHandsGlowing();

    String getDeathSource();

    void setDeathSource(String source);

    ResourceLocation getTrait();

    void setTrait(ResourceLocation registryName);

    boolean isDnaActive();

    void setDnaActive(boolean alive);

    int getAnimationTicks();

    void setAnimationTicks(int ticks);

    void setSyncingFromJar(boolean syncing);

    boolean isSyncingToJar();

    SkinInfo.SkinType getNextSkinType();

    void setNextSkinType(SkinInfo.SkinType skinType);

    String getNextSkin();

    void setNextSkin(String encodedSkin);

    boolean hasDroppedHand();

    void setDroppedHand(boolean droppedHand);

    HandSide getCutoffHand();

    void setCutOffHand(HandSide side);

}