package hu.iit.bme.wecie.engine.opengl.texture;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public final class TextureTarget extends OpenGLEnumeration {

    public static TextureTarget texture2D = new TextureTarget (GL11.GL_TEXTURE_2D);
    public static TextureTarget cubemap = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP);
    public static TextureTarget cubemapPositiveX = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_X);
    public static TextureTarget cubemapNegativeX = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X);
    public static TextureTarget cubemapPositiveY = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y);
    public static TextureTarget cubemapNegativeY = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);
    public static TextureTarget cubemapPositiveZ = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z);
    public static TextureTarget cubemapNegativeZ = new TextureTarget (GL20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);

    private TextureTarget (int glValue) {
        super (glValue);
    }

}
