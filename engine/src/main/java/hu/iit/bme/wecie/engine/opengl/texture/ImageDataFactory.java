package hu.iit.bme.wecie.engine.opengl.texture;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ImageDataFactory {

    private ImageDataFactory () {
        throw new UnsupportedOperationException ();
    }

    public static ImageData fromBufferedImage (BufferedImage image) {
        ByteBuffer bb = BufferUtils.createByteBuffer (4 * image.getWidth () * image.getHeight ());
        int [] rgb = image.getRGB (0, 0, image.getWidth (), image.getHeight (), null, 0, image.getWidth ());
        for (int i = 0; i < rgb.length; i++) {
            int p = rgb [i];
            bb.put ((byte) ( (p >> 16) & 0xFF ))
                    .put ((byte) ( (p >> 8) & 0xFF ))
                    .put ((byte) ( (p & 0xFF) ))
                    .put ((byte) ( (p >> 24) & 0xFF ));
        }
        return ImageData.createByte (bb.flip (), image.getWidth (), image.getHeight ());
    }

    public static ImageData create (
            int width, int height,
            FloatBuffer pixelData,
            InternalPixelFormat internalPixelFormat,
            PixelFormat pixelFormat,
            PixelDataType pixelDataType
    ) {

        return ImageData.createFloat (pixelData, internalPixelFormat, pixelFormat, pixelDataType, width, height);

    }

    public static ImageData createRGBA8UI (
            int width, int height,
            int r, int g, int b, int a
    ) {

        ByteBuffer bb = BufferUtils.createByteBuffer (4 * width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                bb.put ((byte) r)
                        .put ((byte) g)
                        .put ((byte) b)
                        .put ((byte) a);
            }
        }

        bb.flip ();

        return ImageData.createByte (bb, width, height);

    }

}
