package hu.iit.bme.wecie.engine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface VertexBufferObject {

    int getId ();

    VertexBufferObject generate ();

    VertexBufferObject bind (BufferTarget target);

    VertexBufferObject upload (ByteBuffer data, DataUsage dataUsage);

    VertexBufferObject upload (FloatBuffer data, DataUsage dataUsage);

    VertexBufferObject upload (ShortBuffer data, DataUsage dataUsage);

    VertexBufferObject upload (IntBuffer data, DataUsage dataUsage);

    VertexBufferObject upload (float[] data, DataUsage dataUsage);

    VertexBufferObject upload (short[] data, DataUsage dataUsage);

    VertexBufferObject upload (int[] data, DataUsage dataUsage);

    VertexBufferObject delete ();

}
