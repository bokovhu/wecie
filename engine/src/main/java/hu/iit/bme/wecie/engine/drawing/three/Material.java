package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;

public interface Material {

    Texture getAlbedoMap ();

    Texture getMetallicMap ();

    Texture getRoughnessMap ();

    Texture getAmbientOcclusionMap ();

    Texture getNormalMap ();

}
