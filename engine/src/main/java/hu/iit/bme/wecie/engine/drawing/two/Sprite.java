package hu.iit.bme.wecie.engine.drawing.two;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import org.joml.Vector2f;

public interface Sprite extends DrawableComponent {

    Texture getTexture ();

    Vector2f getAtlasUV ();

    Vector2f getAtlasDimensions ();

}
