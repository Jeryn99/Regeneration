package me.suff.mc.regen.client.rendering;

import me.suff.mc.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class JarParticle extends SpriteTexturedParticle {
    private JarParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        Vector3d color = rand.nextBoolean() ? TransitionTypes.FIERY.get().getDefaultSecondaryColor() : TransitionTypes.FIERY.get().getDefaultSecondaryColor();
        this.particleRed = (float) color.x;
        this.particleGreen = (float) color.y;
        this.particleBlue = (float) color.z;
        this.particleAlpha = 0.2F;
        this.setSize(0.02F, 0.02F);
        this.particleScale *= this.rand.nextFloat() * 0.6F + 0.5F;
        this.motionX *= 0.02F;
        this.motionY *= 0.02F;
        this.motionZ *= 0.02F;
        this.maxAge = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.maxAge-- <= 0) {
            this.setExpired();
        } else {
            this.move(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.99D;
            this.motionY *= 0.99D;
            this.motionZ *= 0.99D;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory< BasicParticleType > {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            JarParticle jarParticle = new JarParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            jarParticle.selectSpriteRandomly(this.spriteSet);
            return jarParticle;
        }
    }
}
