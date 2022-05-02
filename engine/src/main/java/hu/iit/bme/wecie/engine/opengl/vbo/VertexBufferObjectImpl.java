package hu.iit.bme.wecie.engine.opengl.vbo;

import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

class VertexBufferObjectImpl implements VertexBufferObject {

    private int glId;
    private BufferTarget boundTo;

    @Override
    public int getId () {
        return glId;
    }

    @Override
    public VertexBufferObject generate () {

        this.glId = GL20.glGenBuffers ();

        return this;
    }

    @Override
    public VertexBufferObject bind (BufferTarget target) {

        GL20.glBindBuffer (target.getGlValue (), glId);
        boundTo = target;

        return this;
    }

    @Override
    public VertexBufferObject upload (ByteBuffer data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject upload (FloatBuffer data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject upload (ShortBuffer data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject upload (IntBuffer data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject upload (float[] data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject upload (short[] data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject upload (int[] data, DataUsage dataUsage) {

        GL20.glBufferData (boundTo.getGlValue (), data, dataUsage.getGlValue ());

        return this;
    }

    @Override
    public VertexBufferObject delete () {

        if (boundTo != null) {
            GL20.glBindBuffer (boundTo.getGlValue (), 0);
        }

        GL20.glDeleteBuffers (glId);
        return this;

    }


}
