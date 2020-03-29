package me.swirtzly.regeneration.common.item.arch.capability;

import me.swirtzly.regeneration.client.skinhandling.SkinInfo;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by Swirtzly on 29/02/2020 @ 22:27
 */
public interface IArch extends INBTSerializable<NBTTagCompound> {

	int getRegenAmount();

	void setRegenAmount(int regenAmount);

	ResourceLocation getSavedTrait();

	void setSavedTrait(ResourceLocation savedTrait);

	ArchStatus getArchStatus();

	void setArchStatus(ArchStatus status);

    void setSkinType(SkinInfo.SkinType skinType);
    SkinInfo.SkinType getSkinType();

    void setSkin(String encoded);
    String getSkin();

    enum ArchStatus {
		ARCH_ITEM, NORMAL_ITEM
	}
}
