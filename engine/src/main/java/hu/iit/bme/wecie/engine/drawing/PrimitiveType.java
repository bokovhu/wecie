package hu.iit.bme.wecie.engine.drawing;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL20;

public class PrimitiveType extends OpenGLEnumeration {

    public static final PrimitiveType triangles = new PrimitiveType (GL20.GL_TRIANGLES);
    public static final PrimitiveType triangleStrip = new PrimitiveType (GL20.GL_TRIANGLE_STRIP);
    public static final PrimitiveType triangleFan = new PrimitiveType (GL20.GL_TRIANGLE_FAN);
    public static final PrimitiveType lines = new PrimitiveType (GL20.GL_LINES);
    public static final PrimitiveType lineStrip = new PrimitiveType (GL20.GL_LINE_STRIP);
    public static final PrimitiveType lineLoop = new PrimitiveType (GL20.GL_LINE_LOOP);
    public static final PrimitiveType quads = new PrimitiveType (GL20.GL_QUADS);

    protected PrimitiveType (int glValue) {
        super (glValue);
    }

}
