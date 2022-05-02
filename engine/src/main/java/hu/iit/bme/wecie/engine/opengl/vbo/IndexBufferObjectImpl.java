package hu.iit.bme.wecie.engine.opengl.vbo;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

class IndexBufferObjectImpl implements IndexBufferObject {

    private int glId;

    @Override
    public int getId () {
        return glId;
    }

    @Override
    public IndexBufferObject generate () {

        this.glId = GL20.glGenBuffers ();

        return this;
    }

    @Override
    public IndexBufferObject bind (BufferTarget target) {

        GL20.glBindBuffer (target.getGlValue (), glId);

        return this;
    }

    @Override
    public IndexBufferObject upload (IntBuffer data, DataUsage dataUsage) {

        bind (BufferTarget.elementArray);
        GL20.glBufferData (
                BufferTarget.elementArray.getGlValue (),
                data,
                dataUsage.getGlValue ()
        );

        return this;
    }

    @Override
    public IndexBufferObject upload (ByteBuffer data, DataUsage dataUsage) {

        bind (BufferTarget.elementArray);
        GL20.glBufferData (
                BufferTarget.elementArray.getGlValue (),
                data,
                dataUsage.getGlValue ()
        );

        return this;
    }

    @Override
    public IndexBufferObject upload (ShortBuffer data, DataUsage dataUsage) {

        bind (BufferTarget.elementArray);
        GL20.glBufferData (
                BufferTarget.elementArray.getGlValue (),
                data,
                dataUsage.getGlValue ()
        );

        return this;
    }

    @Override
    public IndexBufferObject upload (int[] data, DataUsage dataUsage) {

        bind (BufferTarget.elementArray);
        GL20.glBufferData (
                BufferTarget.elementArray.getGlValue (),
                data,
                dataUsage.getGlValue ()
        );

        return this;
    }

    @Override
    public IndexBufferObject upload (short[] data, DataUsage dataUsage) {

        bind (BufferTarget.elementArray);
        GL20.glBufferData (
                BufferTarget.elementArray.getGlValue (),
                data,
                dataUsage.getGlValue ()
        );

        return this;
    }

    @Override
    public IndexBufferObject delete () {

        GL30.glBindBuffer (BufferTarget.elementArray.getGlValue (), 0);
        GL20.glDeleteBuffers (glId);
        return this;

    }

}
