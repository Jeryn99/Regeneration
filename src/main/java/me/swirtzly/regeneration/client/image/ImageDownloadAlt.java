package me.swirtzly.regeneration.client.image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;


@SideOnly(Side.CLIENT)
public class ImageDownloadAlt extends SimpleTexture {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicInteger threadDownloadCounter = new AtomicInteger(0);
    private final File cacheFile;
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private boolean textureUploaded;

    public ImageDownloadAlt(File file, String url, ResourceLocation resource, IImageBuffer buffer) {
        super(resource);
        this.cacheFile = file;
        this.imageUrl = url;
        this.imageBuffer = buffer;
    }

    //It's okay, I hate it too
    public static boolean isAlexSkin(BufferedImage image) {
        return hasAlpha(55, 20, image) &&
                hasAlpha(55, 21, image) &&
                hasAlpha(55, 22, image) &&
                hasAlpha(55, 23, image) &&
                hasAlpha(55, 24, image) &&
                hasAlpha(55, 25, image) &&
                hasAlpha(55, 26, image) &&
                hasAlpha(55, 27, image) &&
                hasAlpha(55, 28, image) &&
                hasAlpha(55, 29, image) &&
                hasAlpha(55, 30, image) &&
                hasAlpha(55, 31, image) &&
                hasAlpha(54, 20, image) &&
                hasAlpha(54, 21, image) &&
                hasAlpha(54, 22, image) &&
                hasAlpha(54, 23, image) &&
                hasAlpha(54, 24, image) &&
                hasAlpha(54, 25, image) &&
                hasAlpha(54, 26, image) &&
                hasAlpha(54, 27, image) &&
                hasAlpha(54, 28, image) &&
                hasAlpha(54, 29, image) &&
                hasAlpha(54, 30, image) &&
                hasAlpha(54, 31, image) || hasAlpha(46, 52, image) && hasAlpha(46, 53, image) && hasAlpha(46, 54, image) && hasAlpha(46, 54, image) && hasAlpha(46, 55, image) && hasAlpha(46, 56, image) && hasAlpha(46, 57, image) && hasAlpha(46, 58, image) && hasAlpha(46, 59, image) && hasAlpha(46, 60, image) && hasAlpha(46, 61, image) && hasAlpha(46, 63, image) && hasAlpha(46, 53, image);
    }

    public static boolean hasAlpha(int x, int y, BufferedImage image) {
        int pixel = image.getRGB(x, y);
        return pixel >> 24 == 0x00 || ((pixel & 0x00FFFFFF) == 0);
    }

    private void checkTextureUploaded() {
        if (!this.textureUploaded) {
            if (this.bufferedImage != null) {
                this.textureUploaded = true;
                if (this.textureLocation != null) {
                    this.deleteGlTexture();
                }
                TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
            }
        }
    }

    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }

    public void setBufferedImage(BufferedImage p_147641_1_) {
        this.bufferedImage = p_147641_1_;

        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }

        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                logger.debug("Loading http texture from local cache ({})", new Object[]{this.cacheFile});

                try {
                    this.bufferedImage = ImageIO.read(this.cacheFile);

                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                } catch (IOException ioexception) {
                    logger.error("Couldn\'t load skin " + this.cacheFile, ioexception);
                    this.loadTextureFromServer();
                }
            } else {
                this.loadTextureFromServer();
            }
        }
    }

    protected void loadTextureFromServer() {
        this.imageThread = new Thread("Texture Downloader #" + threadDownloadCounter.incrementAndGet()) {
            public void run() {
                HttpURLConnection connection = null;
                ImageDownloadAlt.logger.debug("Downloading http texture from {} to {}", new Object[]{ImageDownloadAlt.this.imageUrl, ImageDownloadAlt.this.cacheFile});

                try {
                    connection = (HttpURLConnection) (new URL(ImageDownloadAlt.this.imageUrl)).openConnection(Minecraft.getMinecraft().getProxy());
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
                    connection.connect();

                    if (connection.getResponseCode() / 100 != 2) {
                        return;
                    }

                    BufferedImage bufferedimage;

                    if (ImageDownloadAlt.this.cacheFile != null) {
                        FileUtils.copyInputStreamToFile(connection.getInputStream(), ImageDownloadAlt.this.cacheFile);
                        bufferedimage = ImageIO.read(ImageDownloadAlt.this.cacheFile);
                    } else {
                        bufferedimage = TextureUtil.readBufferedImage(connection.getInputStream());
                    }

                    if (ImageDownloadAlt.this.imageBuffer != null) {
                        bufferedimage = ImageDownloadAlt.this.imageBuffer.parseUserSkin(bufferedimage);
                    }

                    ImageDownloadAlt.this.setBufferedImage(bufferedimage);
                } catch (Exception exception) {
                    ImageDownloadAlt.logger.error("Couldn\'t download http texture", exception);
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        };
        this.imageThread.setDaemon(true);
        this.imageThread.start();
    }

}
