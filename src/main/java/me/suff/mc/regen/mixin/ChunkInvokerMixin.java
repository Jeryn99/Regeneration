package me.suff.mc.regen.mixin;


import com.mojang.serialization.Codec;
import net.minecraft.world.gen.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkInvokerMixin {

    @Invoker("codec")
    public Codec<? extends ChunkGenerator> giveMeTheCodecFam();

}
