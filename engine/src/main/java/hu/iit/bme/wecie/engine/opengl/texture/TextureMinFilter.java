package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL11;

public final class TextureMinFilter extends OpenGLEnumeration {

    public static final TextureMinFilter nearest = new TextureMinFilter (GL11.GL_NEAREST);
    public static final TextureMinFilter linear = new TextureMinFilter (GL11.GL_LINEAR);
    public static final TextureMinFilter nearestMipMapNearest = new TextureMinFilter (GL11.GL_NEAREST_MIPMAP_NEAREST);
    public static final TextureMinFilter nearestMipMapLinear = new TextureMinFilter (GL11.GL_NEAREST_MIPMAP_LINEAR);
    public static final TextureMinFilter linearMipMapNearest = new TextureMinFilter (GL11.GL_LINEAR_MIPMAP_NEAREST);
    public static final TextureMinFilter linearMipMapLinear = new TextureMinFilter (GL11.GL_LINEAR_MIPMAP_LINEAR);

    private TextureMinFilter (int glValue) {
        super (glValue);
    }

}
