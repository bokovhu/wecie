package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL44;

public final class TextureWrap extends OpenGLEnumeration {

    public static final TextureWrap repeat = new TextureWrap (GL11.GL_REPEAT);
    public static final TextureWrap clampToEdge = new TextureWrap (GL20.GL_CLAMP_TO_EDGE);
    public static final TextureWrap clampToBorder = new TextureWrap (GL20.GL_CLAMP_TO_BORDER);
    public static final TextureWrap mirroredRepeat = new TextureWrap (GL20.GL_MIRRORED_REPEAT);
    public static final TextureWrap mirrorClampToEdge = new TextureWrap (GL44.GL_MIRROR_CLAMP_TO_EDGE);

    private TextureWrap (int glValue) {
        super (glValue);
    }

}
