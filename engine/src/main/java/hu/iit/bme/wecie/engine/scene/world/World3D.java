package hu.iit.bme.wecie.engine.scene.world;

import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.scene.GameObject;
import hu.iit.bme.wecie.engine.scene.BaseWorld;

public class World3D extends BaseWorld<Environment3D, Model, Light3D> {

    public World3D () {
        super (new Environment3D ());
    }

    @Override
    protected int getGameObjectTypeId () {
        return GameObject.MODEL3D;
    }

}
