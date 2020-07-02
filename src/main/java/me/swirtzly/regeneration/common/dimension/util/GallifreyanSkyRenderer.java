package me.swirtzly.regeneration.common.dimension.util;

import com.mojang.blaze3d.platform.GlStateManager;
import me.swirtzly.regeneration.Regeneration;
import me.swirtzly.regeneration.common.dimension.biomes.GallifrayanWastelands;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;

import java.util.Random;

public class GallifreyanSkyRenderer implements IRenderHandler {

    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation(Regeneration.MODID, "textures/environment/sun1.png");
    private static final ResourceLocation SUN2_TEXTURES = new ResourceLocation(Regeneration.MODID, "textures/environment/sun2.png");
    private static final ResourceLocation OG = new ResourceLocation("textures/environment/sun.png");
    private static GallifreyanSkyRenderer INSTANCE;
    private final TextureManager renderEngine;
    private final VertexFormat vertexBufferFormat;
    private final boolean vboEnabled;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private int starGLCallList = -1;
    private int glSkyList = -1;
    private int glSkyList2 = -1;

    public GallifreyanSkyRenderer() {
        this.renderEngine = Minecraft.getInstance().getTextureManager();
        this.vboEnabled = Minecraft.getInstance().gameSettings.field_225307_E;
        this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, VertexFormatElement.Usage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }

    public static GallifreyanSkyRenderer getInstance() {
        if (INSTANCE == null)
            INSTANCE = new GallifreyanSkyRenderer();
        return INSTANCE;
    }

    private void generateStars() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.starVBO != null) {
            this.starVBO.deleteGlBuffers();
        }

        if (this.starGLCallList >= 0) {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }

        if (this.vboEnabled) {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(bufferbuilder);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.starVBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            GlStateManager.newList(this.starGLCallList, 4864);
            this.renderStars(bufferbuilder);
            tessellator.draw();
            GlStateManager.endList();
            GlStateManager.popMatrix();
        }
    }

    private void renderStars(BufferBuilder bufferBuilderIn) {
        Random random = new Random(10842L);
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 1500; ++i) {
            double d0 = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + random.nextFloat() * 0.1F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 = d0 * d4;
                d1 = d1 * d4;
                d2 = d2 * d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    bufferBuilderIn.pos(d5 + d25, d6 + d23, d7 + d26).endVertex();
                }
            }
        }
    }

    private void generateSky2() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.sky2VBO != null) {
            this.sky2VBO.deleteGlBuffers();
        }

        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }

        if (this.vboEnabled) {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(bufferbuilder, -16.0F, true);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.sky2VBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.glSkyList2 = GLAllocation.generateDisplayLists(1);
            GlStateManager.newList(this.glSkyList2, 4864);
            this.renderSky(bufferbuilder, -16.0F, true);
            tessellator.draw();
            GlStateManager.endList();
        }
    }

    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }

        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }

        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(bufferbuilder, 16.0F, false);
            bufferbuilder.finishDrawing();
            bufferbuilder.reset();
            this.skyVBO.bufferData(bufferbuilder.getByteBuffer());
        } else {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GlStateManager.newList(this.glSkyList, 4864);
            this.renderSky(bufferbuilder, 16.0F, false);
            tessellator.draw();
            GlStateManager.endList();
        }
    }

    private void renderSky(BufferBuilder bufferBuilderIn, float posY, boolean reverseX) {
        bufferBuilderIn.begin(7, DefaultVertexFormats.POSITION);

        for (int k = -384; k <= 384; k += 50) {
            for (int l = -384; l <= 384; l += 50) {
                float f = (float) k;
                float f1 = (float) (k + 50);

                if (reverseX) {
                    f1 = (float) k;
                    f = (float) (k + 50);
                }

                bufferBuilderIn.pos(f, posY, l).endVertex();
                bufferBuilderIn.pos(f1, posY, l).endVertex();
                bufferBuilderIn.pos(f1, posY, l + 50).endVertex();
                bufferBuilderIn.pos(f, posY, l + 50).endVertex();
            }
        }
    }

    @Override
    public void render(int ticks, float partialTicks, ClientWorld world, Minecraft mc) {
        GlStateManager.disableTexture();
        Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity().getPosition(), partialTicks);
        float f = (float) vec3d.x;
        float f1 = (float) vec3d.y;
        float f2 = (float) vec3d.z;

        GlStateManager.color3f(f, f1, f2);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        GlStateManager.color3f(f, f1, f2);

        if (vboEnabled) {
            this.skyVBO.bindBuffer();
            GlStateManager.enableClientState(32884);
            GlStateManager.vertexPointer(3, 5126, 12, 0);
            this.skyVBO.drawArrays(7);
            VertexBuffer.unbindBuffer();
            GlStateManager.disableClientState(32884);
        } else {
            GlStateManager.callList(this.glSkyList);
        }

        GlStateManager.disableFog();
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        float[] afloat = world.dimension.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);

        if (afloat != null) {
            GlStateManager.disableTexture();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
            float f6 = afloat[0];
            float f7 = afloat[1];
            float f8 = afloat[2];

            bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(0.0D, 100.0D, 0.0D).color(f6, f7, f8, afloat[3]).endVertex();

            for (int j2 = 0; j2 <= 16; ++j2) {
                float f21 = (float) j2 * ((float) Math.PI * 2F) / 16.0F;
                float f12 = MathHelper.sin(f21);
                float f13 = MathHelper.cos(f21);
                bufferbuilder.pos(f12 * 120.0F, f13 * 120.0F, -f13 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
            }

            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }

        GlStateManager.enableTexture();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        float f16 = 1.0F - world.getRainStrength(partialTicks);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, f16);
        GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        float f17 = 30.0F;

        GlStateManager.pushMatrix();
        this.renderEngine.bindTexture(OG);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-f17, 100.0D, -f17).tex(0.0D, 0.0D).endVertex();
        bufferbuilder.pos(f17, 100.0D, -f17).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(f17, 100.0D, f17).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(-f17, 100.0D, f17).tex(0.0D, 1.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translatef(34, 0, 0);
        GlStateManager.translatef(0, 0.0F, -18);
        this.renderEngine.bindTexture(OG);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-f17, 100.0D, -f17).tex(0.0D, 0.0D).endVertex();
        bufferbuilder.pos(f17, 100.0D, -f17).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(f17, 100.0D, f17).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(-f17, 100.0D, f17).tex(0.0D, 1.0D).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();

        f17 = 20.0F;
        this.renderEngine.bindTexture(MOON_PHASES_TEXTURES);
        int k1 = world.getMoonPhase();
        int i2 = k1 % 4;
        int k2 = k1 / 4 % 2;
        float f22 = (float) (i2 + 0) / 4.0F;
        float f23 = (float) (k2 + 0) / 2.0F;
        float f24 = (float) (i2 + 1) / 4.0F;
        float f14 = (float) (k2 + 1) / 2.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-f17, -100.0D, f17).tex(f24, f14).endVertex();
        bufferbuilder.pos(f17, -100.0D, f17).tex(f22, f14).endVertex();
        bufferbuilder.pos(f17, -100.0D, -f17).tex(f22, f23).endVertex();
        bufferbuilder.pos(-f17, -100.0D, -f17).tex(f24, f23).endVertex();
        tessellator.draw();

        GlStateManager.disableTexture();
        float f15 = world.getStarBrightness(partialTicks) * f16;

        if (f15 > 0.0F) {
            GlStateManager.color4f(f15, f15, f15, f15);

            if (this.vboEnabled) {
                this.starVBO.bindBuffer();
                GlStateManager.enableClientState(32884);
                GlStateManager.vertexPointer(3, 5126, 12, 0);
                this.starVBO.drawArrays(7);
                VertexBuffer.unbindBuffer();
                GlStateManager.disableClientState(32884);
            } else {
                GlStateManager.callList(this.starGLCallList);
            }
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.disableTexture();
        GlStateManager.color3f(0.0F, 0.0F, 0.0F);
        double d3 = mc.player.getEyePosition(partialTicks).y - world.getHorizon();

        if (d3 < 0.0D) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 12.0F, 0.0F);

            if (this.vboEnabled) {
                this.sky2VBO.bindBuffer();
                GlStateManager.enableClientState(32884);
                GlStateManager.vertexPointer(3, 5126, 12, 0);
                this.sky2VBO.drawArrays(7);
                VertexBuffer.unbindBuffer();
                GlStateManager.disableClientState(32884);
            } else {
                GlStateManager.callList(this.glSkyList2);
            }

            GlStateManager.popMatrix();
            float f19 = -((float) (d3 + 65.0D));
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(-1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, f19, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, f19, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
        }

        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player.world.getBiome(player.getPosition()) instanceof GallifrayanWastelands) {
            f = 0.14F;
            f1 = 0.15F;
            f2 = 0.22F;
        }

        if (!world.dimension.isSkyColored()) {
            GlStateManager.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        } else {
            GlStateManager.color3f(f, f1, f2);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(0.0F, -((float) (d3 - 16.0D)), 0.0F);
        GlStateManager.callList(this.glSkyList2);
        GlStateManager.popMatrix();
        GlStateManager.enableTexture();
        GlStateManager.depthMask(true);

    }

}
