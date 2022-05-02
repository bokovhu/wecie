package hu.iit.bme.wecie.engine.util;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;

public final class Buffers {

    private Buffers () {
        throw new UnsupportedOperationException ();
    }

    public static FloatBuffer put (FloatBuffer fb, Vector2f v) {
        return fb.put (v.x).put (v.y);
    }

    public static FloatBuffer put (FloatBuffer fb, Vector3f v) {
        return fb.put (v.x).put (v.y).put (v.z);
    }

    public static FloatBuffer put (FloatBuffer fb, Vector4f v) {
        return fb.put (v.x).put (v.y).put (v.z).put (v.w);
    }

}
