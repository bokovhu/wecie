package hu.iit.bme.wecie.engine.opengl.texture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL45;

class TextureImpl implements Texture {

    private int glId;
    private int width, height;
    private TextureTarget boundTo;

    @Override
    public int getId () {
        return glId;
    }

    @Override
    public int getWidth () {
        return width;
    }

    @Override
    public int getHeight () {
        return height;
    }

    @Override
    public Texture generate () {

        this.glId = GL11.glGenTextures ();

        return this;
    }

    @Override
    public Texture bind (TextureTarget target, int unit) {

        GL20.glActiveTexture (GL20.GL_TEXTURE0 + unit);
        GL20.glBindTexture (
                target.getGlValue (),
                this.glId
        );
        this.boundTo = target;

        return this;
    }

    @Override
    public Texture upload2D (ImageData imageData, boolean generateMipmaps) {

        bind (TextureTarget.texture2D, 0);
        imageData.upload2D (boundTo);
        if (generateMipmaps) {
            GL45.glGenerateMipmap (boundTo.getGlValue ());
        }
        this.width = imageData.getWidth ();
        this.height = imageData.getHeight ();

        return this;
    }

    @Override
    public Texture configure (
            TextureTarget target,
            TextureMinFilter minFilter, TextureMagFilter magFilter, TextureWrap wrapS, TextureWrap wrapT
    ) {

        bind (target, 0);
        GL20.glTexParameteri (boundTo.getGlValue (), GL20.GL_TEXTURE_WRAP_S, wrapS.getGlValue ());
        GL20.glTexParameteri (boundTo.getGlValue (), GL20.GL_TEXTURE_WRAP_T, wrapT.getGlValue ());
        GL20.glTexParameteri (
                boundTo.getGlValue (),
                GL20.GL_TEXTURE_MIN_FILTER,
                minFilter.getGlValue ()
        );
        GL20.glTexParameteri (
                boundTo.getGlValue (),
                GL20.GL_TEXTURE_MAG_FILTER,
                magFilter.getGlValue ()
        );

        return this;
    }

    @Override
    public Texture configure (
            TextureTarget target,
            TextureMinFilter minFilter,
            TextureMagFilter magFilter,
            TextureWrap wrapS,
            TextureWrap wrapT,
            TextureWrap wrapR
    ) {

        bind (target, 0);
        GL20.glTexParameteri (boundTo.getGlValue (), GL20.GL_TEXTURE_WRAP_S, wrapS.getGlValue ());
        GL20.glTexParameteri (boundTo.getGlValue (), GL20.GL_TEXTURE_WRAP_T, wrapT.getGlValue ());
        GL20.glTexParameteri (boundTo.getGlValue (), GL20.GL_TEXTURE_WRAP_R, wrapR.getGlValue ());
        GL20.glTexParameteri (
                boundTo.getGlValue (),
                GL20.GL_TEXTURE_MIN_FILTER,
                minFilter.getGlValue ()
        );
        GL20.glTexParameteri (
                boundTo.getGlValue (),
                GL20.GL_TEXTURE_MAG_FILTER,
                magFilter.getGlValue ()
        );

        return this;
    }

    @Override
    public Texture uploadCubemap (
            ImageData positiveXData,
            ImageData negativeXData,
            ImageData positiveYData,
            ImageData negativeYData,
            ImageData positiveZData,
            ImageData negativeZData,
            boolean generateMipmaps
    ) {

        bind (TextureTarget.cubemap, 0);
        positiveXData.upload2D (TextureTarget.cubemapPositiveX);
        negativeXData.upload2D (TextureTarget.cubemapNegativeX);
        positiveYData.upload2D (TextureTarget.cubemapPositiveY);
        negativeYData.upload2D (TextureTarget.cubemapNegativeY);
        positiveZData.upload2D (TextureTarget.cubemapPositiveZ);
        negativeZData.upload2D (TextureTarget.cubemapNegativeZ);
        if (generateMipmaps) {
            GL45.glGenerateMipmap (TextureTarget.cubemap.getGlValue ());
        }

        return this;
    }

    @Override
    public Texture delete () {

        GL11.glDeleteTextures (glId);
        return this;

    }

}
