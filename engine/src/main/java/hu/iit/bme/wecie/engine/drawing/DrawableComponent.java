package hu.iit.bme.wecie.engine.drawing;

import hu.iit.bme.wecie.engine.scene.RenderingContext;
import org.joml.Matrix4f;

public interface DrawableComponent {

    void draw (Matrix4f transformation, RenderingContext context);

}
