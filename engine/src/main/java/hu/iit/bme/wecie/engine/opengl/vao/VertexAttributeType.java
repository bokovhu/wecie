package hu.iit.bme.wecie.engine.opengl.vao;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL20;

public final class VertexAttributeType extends OpenGLEnumeration {

    public static final VertexAttributeType glFloat = new VertexAttributeType (GL20.GL_FLOAT, Float.BYTES);

    private final int sizeInBytes;

    protected VertexAttributeType (int glValue, int sizeInBytes) {
        super (glValue);
        this.sizeInBytes = sizeInBytes;
    }

    public int getSizeInBytes () {
        return sizeInBytes;
    }

}
