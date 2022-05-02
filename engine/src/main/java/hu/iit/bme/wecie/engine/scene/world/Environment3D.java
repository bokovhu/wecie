package hu.iit.bme.wecie.engine.scene.world;

import hu.iit.bme.wecie.engine.scene.BaseEnvironment;

public class Environment3D extends BaseEnvironment<Light3D> {

    public Environment3D () {
        super (Light3D::new);
    }

}
