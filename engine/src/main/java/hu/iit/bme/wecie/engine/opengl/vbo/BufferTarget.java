package hu.iit.bme.wecie.engine.opengl.vbo;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL20;

public final class BufferTarget extends OpenGLEnumeration {

    public static final BufferTarget array = new BufferTarget (GL20.GL_ARRAY_BUFFER);
    public static final BufferTarget elementArray = new BufferTarget (GL20.GL_ELEMENT_ARRAY_BUFFER);

    protected BufferTarget (int glValue) {
        super (glValue);
    }

}
