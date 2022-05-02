package hu.iit.bme.wecie.engine.opengl.vbo;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL20;

public final class DataUsage extends OpenGLEnumeration {

    public static final DataUsage staticDraw = new DataUsage (GL20.GL_STATIC_DRAW);
    public static final DataUsage dynamicDraw = new DataUsage (GL20.GL_DYNAMIC_DRAW);

    protected DataUsage (int glValue) {
        super (glValue);
    }

}
