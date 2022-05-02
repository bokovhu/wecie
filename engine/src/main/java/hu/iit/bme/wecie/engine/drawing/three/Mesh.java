package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.drawing.renderable.IndexedRenderable;

public interface Mesh {

    IndexedRenderable getRenderable ();

    Material getMaterial ();

    Mesh changeMaterial (Material newMaterial);

    String getName ();

}
