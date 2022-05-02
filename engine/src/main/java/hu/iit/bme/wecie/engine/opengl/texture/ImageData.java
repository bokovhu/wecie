package hu.iit.bme.wecie.engine.opengl.texture;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public final class ImageData {

    private final FloatBuffer floatPixelData;
    private final ByteBuffer bytePixelData;
    private final InternalPixelFormat internalPixelFormat;
    private final PixelFormat pixelFormat;
    private final PixelDataType pixelDataType;
    private final int width, height;

    private ImageData (
            FloatBuffer floatBuffer,
            ByteBuffer byteBuffer,
            InternalPixelFormat internalPixelFormat,
            PixelFormat pixelFormat,
            PixelDataType pixelDataType,
            int width, int height
    ) {
        this.floatPixelData = floatBuffer;
        this.bytePixelData = byteBuffer;
        this.internalPixelFormat = internalPixelFormat;
        this.pixelFormat = pixelFormat;
        this.pixelDataType = pixelDataType;
        this.width = width;
        this.height = height;
    }

    public static ImageData createFloat (FloatBuffer fb, int w, int h) {
        return new ImageData (
                fb,
                null,
                InternalPixelFormat.rgba32f,
                PixelFormat.rgba,
                PixelDataType.glFloat,
                w, h
        );
    }

    public static ImageData createFloat (
            FloatBuffer fb,
            InternalPixelFormat ipf,
            PixelFormat pf,
            PixelDataType pdt,
            int w, int h
    ) {
        return new ImageData (
                fb, null,
                ipf, pf, pdt,
                w, h
        );
    }

    public static ImageData createByte (ByteBuffer bb, int w, int h) {
        return new ImageData (
                null,
                bb,
                InternalPixelFormat.rgba8,
                PixelFormat.rgba,
                PixelDataType.glUnsignedByte,
                w, h
        );
    }

    public static ImageData createByte (
            ByteBuffer bb,
            InternalPixelFormat ipf,
            PixelFormat pf,
            PixelDataType pdt,
            int w, int h
    ) {
        return new ImageData (
                null, bb,
                ipf, pf, pdt,
                w, h
        );
    }

    public void upload2D (
            TextureTarget target
    ) {

        if (floatPixelData != null) {
            GL11.glTexImage2D (
                    target.getGlValue (),
                    0,
                    this.internalPixelFormat.getGlValue (),
                    width,
                    height,
                    0,
                    this.pixelFormat.getGlValue (),
                    this.pixelDataType.getGlValue (),
                    floatPixelData
            );
        } else {
            GL11.glTexImage2D (
                    target.getGlValue (),
                    0,
                    this.internalPixelFormat.getGlValue (),
                    width,
                    height,
                    0,
                    this.pixelFormat.getGlValue (),
                    this.pixelDataType.getGlValue (),
                    bytePixelData
            );
        }

    }

    public int getWidth () {
        return width;
    }

    public int getHeight () {
        return height;
    }

}
