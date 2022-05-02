package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL30;

public final class InternalPixelFormat extends OpenGLEnumeration {

    public static InternalPixelFormat rgba32f = new InternalPixelFormat (GL30.GL_RGBA32F);
    public static InternalPixelFormat rgba8 = new InternalPixelFormat (GL30.GL_RGBA8);
    public static InternalPixelFormat r32f = new InternalPixelFormat (GL30.GL_R32F);
    public static InternalPixelFormat depthComponent = new InternalPixelFormat (GL30.GL_DEPTH_COMPONENT);

    protected InternalPixelFormat (int glValue) {
        super (glValue);
    }

}
