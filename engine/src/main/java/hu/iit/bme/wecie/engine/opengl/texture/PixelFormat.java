package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL30;

public final class PixelFormat extends OpenGLEnumeration {

    public static final PixelFormat rgba = new PixelFormat (GL30.GL_RGBA);
    public static final PixelFormat red = new PixelFormat (GL30.GL_RED);
    public static final PixelFormat depthComponent = new PixelFormat (GL30.GL_DEPTH_COMPONENT);

    protected PixelFormat (int glValue) {
        super (glValue);
    }

}
