package hu.iit.bme.wecie.engine.opengl.vbo;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public interface IndexBufferObject {

    int getId ();

    IndexBufferObject generate ();

    IndexBufferObject bind (BufferTarget target);

    IndexBufferObject upload (IntBuffer data, DataUsage dataUsage);

    IndexBufferObject upload (ByteBuffer data, DataUsage dataUsage);

    IndexBufferObject upload (ShortBuffer data, DataUsage dataUsage);

    IndexBufferObject upload (int[] data, DataUsage dataUsage);

    IndexBufferObject upload (short[] data, DataUsage dataUsage);

    IndexBufferObject delete ();

}
