package mc.craig.software.regen.client.rendering;

import mc.craig.software.regen.common.regen.transitions.TransitionTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class JarParticle extends TextureSheetParticle {
    private JarParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        Vec3 color = random.nextBoolean() ? TransitionTypes.FIERY.get().getDefaultPrimaryColor() : TransitionTypes.FIERY.get().getDefaultSecondaryColor();
        this.rCol = (float) color.x;
        this.gCol = (float) color.y;
        this.bCol = (float) color.z;
        this.alpha = 0.2F;
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
        this.xd *= 0.02F;
        this.yd *= 0.02F;
        this.zd *= 0.02F;
        this.lifetime = (int) (20.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.99D;
            this.yd *= 0.99D;
            this.zd *= 0.99D;
        }
    }

        public record Factory(SpriteSet spriteSet) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(@NotNull SimpleParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                JarParticle jarParticle = new JarParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
                jarParticle.pickSprite(this.spriteSet);
                return jarParticle;
            }
        }
}
