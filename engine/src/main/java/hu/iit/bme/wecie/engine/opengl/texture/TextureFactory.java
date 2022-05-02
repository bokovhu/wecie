package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.drawing.three.ModelLoadingContext;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AITexture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class TextureFactory {

    private static final Logger log = LoggerFactory.getLogger (TextureFactory.class);

    private TextureFactory () {
        throw new UnsupportedOperationException ();
    }

    private static Texture create (
            ImageData imageData,
            TextureTarget textureTarget,
            TextureMinFilter minFilter,
            TextureMagFilter magFilter,
            TextureWrap wrapS, TextureWrap wrapT,
            boolean generateMipmaps
    ) {

        TextureImpl texture = new TextureImpl ();

        texture.generate ()
                .configure (
                        textureTarget,
                        minFilter,
                        magFilter,
                        wrapS,
                        wrapT
                )
                .upload2D (
                        imageData,
                        generateMipmaps
                );

        return texture;

    }

    private static Texture textureWithDefaultSettings (ImageData imageData) {

        return create (
                imageData,
                TextureTarget.texture2D,
                TextureMinFilter.linearMipMapLinear,
                TextureMagFilter.linear,
                TextureWrap.repeat,
                TextureWrap.repeat,
                true
        );

    }

    private static Texture cubemapWithDefaultSettings (
            ImageData px, ImageData nx,
            ImageData py, ImageData ny,
            ImageData pz, ImageData nz
    ) {

        return cubemapFromImageData (
                px, nx,
                py, ny,
                pz, nz
        );

    }

    private static BufferedImage loadBufferedImage (File file) {

        try {

            BufferedImage loadedImage = ImageIO.read (file);
            return loadedImage;

        } catch (Exception exc) {
            log.warn ("Could not load image from file {}", file.getAbsolutePath ());
            log.warn ("Exception: ", exc);
            return null;
        }

    }

    private static BufferedImage loadBufferedImage (URL url) {

        try {

            BufferedImage loadedImage = ImageIO.read (url);
            return loadedImage;

        } catch (Exception exc) {
            log.warn ("Could not load image from URL {}", url.toExternalForm ());
            log.warn ("Exception: ", exc);
            return null;
        }

    }

    private static BufferedImage loadBufferedImage (String resourceName, ClassLoader classLoader) {

        URL resourceURL = classLoader.getResource (resourceName);
        if (resourceURL == null) {
            log.warn ("Resource does not exist: {}", resourceName);
            return null;
        }
        return loadBufferedImage (resourceURL);

    }

    public static Texture checkerboard (
            int width, int height,
            int rows, int cols,
            Vector4f colorA, Vector4f colorB
    ) {

        FloatBuffer fb = BufferUtils.createFloatBuffer (width * height * 4);
        final int rowHeight = height / rows;
        final int colWidth = width / cols;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if ((x / colWidth) % 2 == 0) {
                    if ((y / rowHeight) % 2 == 0) {
                        fb.put (colorA.x).put (colorA.y).put (colorA.z).put (colorA.w);
                    } else {
                        fb.put (colorB.x).put (colorB.y).put (colorB.z).put (colorB.w);
                    }
                } else {
                    if ((y / rowHeight) % 2 == 0) {
                        fb.put (colorB.x).put (colorB.y).put (colorB.z).put (colorB.w);
                    } else {
                        fb.put (colorA.x).put (colorA.y).put (colorA.z).put (colorA.w);
                    }
                }
            }
        }
        fb.flip ();

        return textureWithDefaultSettings (ImageData.createFloat (fb, width, height));

    }

    public static Texture empty (
            int width, int height,
            Vector4f background
    ) {

        FloatBuffer fb = BufferUtils.createFloatBuffer (width * height * 4);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fb.put (background.x).put (background.y).put (background.z).put (background.w);
            }
        }
        fb.flip ();

        return textureWithDefaultSettings (ImageData.createFloat (fb, width, height));

    }

    public static Texture empty (ImageData imageData) {

        return textureWithDefaultSettings (imageData);

    }

    public static Texture load (File file) {

        BufferedImage img = loadBufferedImage (file);
        if (img != null) {
            return textureWithDefaultSettings (
                    ImageDataFactory.fromBufferedImage (img)
            );
        } else {
            log.warn ("Could not load texture from file {}", file.getAbsolutePath ());
            return empty (1, 1, new Vector4f (0f));
        }

    }

    public static Texture load (String resourceName, ClassLoader classLoader) {

        URL resourceURL = classLoader.getResource (resourceName);
        if (resourceURL == null) {
            log.warn ("Resource does not exist: {}", resourceName);
            return empty (1, 1, new Vector4f (0f));
        }
        BufferedImage img = loadBufferedImage (resourceURL);
        if (img != null) {
            return textureWithDefaultSettings (
                    ImageDataFactory.fromBufferedImage (img)
            );
        } else {
            log.warn ("Could not load texture from URL {}", resourceURL.toExternalForm ());
            return empty (1, 1, new Vector4f (0f));
        }

    }

    public static Texture fromBufferedImage (BufferedImage image) {
        return textureWithDefaultSettings (
                ImageDataFactory.fromBufferedImage (image)
        );
    }

    public static Texture cubemapFromImageData (
            ImageData px,
            ImageData nx,
            ImageData py,
            ImageData ny,
            ImageData pz,
            ImageData nz
    ) {
        TextureImpl texture = new TextureImpl ();

        texture.generate ();
        texture.configure (
                TextureTarget.cubemap,
                TextureMinFilter.linear,
                TextureMagFilter.linear,
                TextureWrap.clampToEdge,
                TextureWrap.clampToEdge,
                TextureWrap.clampToEdge
        );
        texture.uploadCubemap (
                px, nx,
                py, ny,
                pz, nz,
                false
        );

        return texture;
    }

    public static Texture loadCubemap (
            File pxFile,
            File nxFile,
            File pyFile,
            File nyFile,
            File pzFile,
            File nzFile
    ) {

        BufferedImage pxImg = loadBufferedImage (pxFile);
        if (pxImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Positive X image from file " + pxFile.getAbsolutePath ());
        }
        int pxW = pxImg.getWidth (), pxH = pxImg.getHeight ();

        BufferedImage nxImg = loadBufferedImage (nxFile);
        if (nxImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Negative X image from file " + nxFile.getAbsolutePath ());
        }
        int nxW = nxImg.getWidth (), nxH = nxImg.getHeight ();

        BufferedImage pyImg = loadBufferedImage (pyFile);
        if (pyImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Positive Y image from file " + pyFile.getAbsolutePath ());
        }
        int pyW = pyImg.getWidth (), pyH = pyImg.getHeight ();

        BufferedImage nyImg = loadBufferedImage (nyFile);
        if (nyImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Negative Y image from file " + nyFile.getAbsolutePath ());
        }
        int nyW = nyImg.getWidth (), nyH = nyImg.getHeight ();

        BufferedImage pzImg = loadBufferedImage (pzFile);
        if (pzImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Positive Z image from file " + pzFile.getAbsolutePath ());
        }
        int pzW = pzImg.getWidth (), pzH = pzImg.getHeight ();

        BufferedImage nzImg = loadBufferedImage (nzFile);
        if (nzImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Negative Z image from file " + nzFile.getAbsolutePath ());
        }
        int nzW = nzImg.getWidth (), nzH = nzImg.getHeight ();

        return cubemapWithDefaultSettings (
                ImageDataFactory.fromBufferedImage (pxImg),
                ImageDataFactory.fromBufferedImage (nxImg),
                ImageDataFactory.fromBufferedImage (pyImg),
                ImageDataFactory.fromBufferedImage (nyImg),
                ImageDataFactory.fromBufferedImage (pzImg),
                ImageDataFactory.fromBufferedImage (nzImg)
        );

    }

    public static Texture loadCubemap (
            String pxResource,
            String nxResource,
            String pyResource,
            String nyResource,
            String pzResource,
            String nzResource,
            ClassLoader classLoader
    ) {

        BufferedImage pxImg = loadBufferedImage (pxResource, classLoader);
        if (pxImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Positive X image from resource " + pxResource);
        }
        int pxW = pxImg.getWidth (), pxH = pxImg.getHeight ();

        BufferedImage nxImg = loadBufferedImage (nxResource, classLoader);
        if (nxImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Negative X image from resource " + nxResource);
        }
        int nxW = nxImg.getWidth (), nxH = nxImg.getHeight ();

        BufferedImage pyImg = loadBufferedImage (pyResource, classLoader);
        if (pyImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Positive Y image from resource " + pyResource);
        }
        int pyW = pyImg.getWidth (), pyH = pyImg.getHeight ();

        BufferedImage nyImg = loadBufferedImage (nyResource, classLoader);
        if (nyImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Negative Y image from resource " + nyResource);
        }
        int nyW = nyImg.getWidth (), nyH = nyImg.getHeight ();

        BufferedImage pzImg = loadBufferedImage (pzResource, classLoader);
        if (pzImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Positive Z image from resource " + pzResource);
        }
        int pzW = pzImg.getWidth (), pzH = pzImg.getHeight ();

        BufferedImage nzImg = loadBufferedImage (nzResource, classLoader);
        if (nzImg == null) {
            throw new IllegalArgumentException ("Could not load cubemap Negative Z image from resource " + nzResource);
        }
        int nzW = nzImg.getWidth (), nzH = nzImg.getHeight ();

        return cubemapWithDefaultSettings (
                ImageDataFactory.fromBufferedImage (pxImg),
                ImageDataFactory.fromBufferedImage (nxImg),
                ImageDataFactory.fromBufferedImage (pyImg),
                ImageDataFactory.fromBufferedImage (nyImg),
                ImageDataFactory.fromBufferedImage (pzImg),
                ImageDataFactory.fromBufferedImage (nzImg)
        );

    }

    public static Texture emptyDepthCubemap (int width, int height) {

        FloatBuffer fb = BufferUtils.createFloatBuffer (width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fb.put (0.0f);
            }
        }
        fb.flip ();

        ImageData imageData = ImageData.createFloat (
                fb,
                InternalPixelFormat.depthComponent,
                PixelFormat.depthComponent,
                PixelDataType.glFloat,
                width, height
        );

        return cubemapFromImageData (
                imageData, imageData,
                imageData, imageData,
                imageData, imageData
        );

    }

    public static Texture emptyDepth (int width, int height) {

        FloatBuffer fb = BufferUtils.createFloatBuffer (width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fb.put (0.0f);
            }
        }
        fb.flip ();

        return create (
                ImageData.createFloat (
                        fb,
                        InternalPixelFormat.depthComponent,
                        PixelFormat.depthComponent,
                        PixelDataType.glFloat,
                        width,
                        height
                ),
                TextureTarget.texture2D,
                TextureMinFilter.nearest, TextureMagFilter.nearest,
                TextureWrap.clampToEdge, TextureWrap.clampToEdge,
                false
        );

    }

    public static Texture emptyCubemap (
            int width,
            int height,
            InternalPixelFormat internalPixelFormat,
            PixelFormat pixelFormat,
            PixelDataType pixelDataType
    ) {

        FloatBuffer fb = BufferUtils.createFloatBuffer (width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fb.put (0.0f);
            }
        }
        fb.flip ();

        ImageData imageData = ImageData.createFloat (
                fb,
                internalPixelFormat,
                pixelFormat,
                pixelDataType,
                width, height
        );

        return cubemapFromImageData (
                imageData, imageData,
                imageData, imageData,
                imageData, imageData
        );

    }

}
