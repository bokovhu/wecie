package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;

public final class LayerFactory {

    private LayerFactory () {
        throw new UnsupportedOperationException ();
    }

    public static <P> Layer<Environment3D, Model, Light3D, P> new3DLayer () {
        return new LayerImpl<> ();
    }

}
