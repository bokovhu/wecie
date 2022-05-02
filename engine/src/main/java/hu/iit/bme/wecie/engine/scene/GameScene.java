package hu.iit.bme.wecie.engine.scene;

import java.util.ArrayList;
import java.util.List;

public abstract class GameScene implements Scene {

    private final List<Layer <?, ?, ?, ?>> layers = new ArrayList<> ();

    @Override
    public final List<Layer <?, ?, ?, ?>> getLayers () {
        return layers;
    }

    @Override
    public final Scene addLayer (Layer <?, ?, ?, ?> layer) {
        layers.add (layer);
        return this;
    }

    @Override
    public final Scene removeLayer (Layer <?, ?, ?, ?> layer) {
        layers.remove (layer);
        return this;
    }

}
