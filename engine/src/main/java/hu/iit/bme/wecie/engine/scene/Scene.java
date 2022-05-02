package hu.iit.bme.wecie.engine.scene;

import java.util.List;

public interface Scene {

    void onSceneCreated ();

    void onSceneAssetsLoaded ();

    void onScenePaused ();

    void onSceneResumed ();

    void onSceneFinished ();

    void onUpdate (float delta);

    void onBeforeRender (float delta);

    void onAfterRender (float delta);

    List<Layer<?, ?, ?, ?>> getLayers ();

    Scene addLayer (Layer<?, ?, ?, ?> layer);

    Scene removeLayer (Layer<?, ?, ?, ?> layer);

    Scene next ();

}
