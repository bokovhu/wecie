package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL11;

public final class TextureMagFilter extends OpenGLEnumeration {

    public static final TextureMagFilter nearest = new TextureMagFilter (GL11.GL_NEAREST);
    public static final TextureMagFilter linear = new TextureMagFilter (GL11.GL_LINEAR);

    private TextureMagFilter (int glValue) {
        super (glValue);
    }

}
