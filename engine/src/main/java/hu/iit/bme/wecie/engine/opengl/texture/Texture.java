package hu.iit.bme.wecie.engine.opengl.texture;

public interface Texture {

    // The OpenGL ID of the texture
    int getId ();

    int getWidth ();

    int getHeight ();

    // Generate the new OpenGL texture object
    Texture generate ();

    // Bind this texture to the specified target in the specified unit
    Texture bind (TextureTarget target, int unit);

    // Upload pixel data to the texture
    Texture upload2D (ImageData imageData, boolean generateMipmaps);

    // Configure the texture's properties
    Texture configure (
            TextureTarget target,
            TextureMinFilter minFilter,
            TextureMagFilter magFilter,
            TextureWrap wrapS,
            TextureWrap wrapT
    );

    Texture configure (
            TextureTarget target,
            TextureMinFilter minFilter,
            TextureMagFilter magFilter,
            TextureWrap wrapS,
            TextureWrap wrapT,
            TextureWrap wrapR
    );

    Texture uploadCubemap (
            ImageData positiveXData,
            ImageData negativeXData,
            ImageData positiveYData,
            ImageData negativeYData,
            ImageData positiveZData,
            ImageData negativeZData,
            boolean generateMipmaps
    );

    Texture delete ();

}
