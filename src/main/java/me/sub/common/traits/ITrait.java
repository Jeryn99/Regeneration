package me.sub.common.traits;

import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

/**
 * Created by Sub
 * on 22/09/2018.
 */
public interface ITrait {

    UUID getUuid();

    TextComponentTranslation getName();

}
