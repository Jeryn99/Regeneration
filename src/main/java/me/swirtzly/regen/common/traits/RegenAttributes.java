package me.swirtzly.regen.common.traits;

import me.swirtzly.regen.util.RConstants;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegenAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RConstants.MODID);

}
