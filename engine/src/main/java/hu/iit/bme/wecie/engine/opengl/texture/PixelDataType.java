package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL30;

public final class PixelDataType extends OpenGLEnumeration {

    public static final PixelDataType glFloat = new PixelDataType (GL30.GL_FLOAT);
    public static final PixelDataType glUnsignedInt = new PixelDataType (GL30.GL_UNSIGNED_INT);
    public static final PixelDataType glUnsignedByte = new PixelDataType (GL30.GL_UNSIGNED_BYTE);

    protected PixelDataType (int glValue) {
        super (glValue);
    }

}
