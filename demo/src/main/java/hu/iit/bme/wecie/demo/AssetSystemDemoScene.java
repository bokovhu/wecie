package hu.iit.bme.wecie.demo;

import hu.iit.bme.wecie.engine.asset.AssetSystem;
import hu.iit.bme.wecie.engine.scene.GameScene;
import hu.iit.bme.wecie.engine.scene.Scene;

public class AssetSystemDemoScene extends GameScene {

    @Override
    public void onSceneCreated () {

        AssetSystem.getInstance ()
                .enqueCubemap ("miramar", "skymaps/miramar", getClass ().getClassLoader ())
                .enqueMaterial ("plastic", "materials/plastic", getClass ().getClassLoader ())
                .enqueMaterial ("rock", "materials/rock", getClass ().getClassLoader ())
                .enqueMaterial ("rust", "materials/rust", getClass ().getClassLoader ())
                .enqueProgram ("test", "shaders/test", getClass ().getClassLoader ());

    }

    @Override
    public void onSceneAssetsLoaded () {

    }

    @Override
    public void onScenePaused () {

    }

    @Override
    public void onSceneResumed () {

    }

    @Override
    public void onSceneFinished () {

    }

    @Override
    public void onUpdate (float delta) {

    }

    @Override
    public void onBeforeRender (float delta) {

    }

    @Override
    public void onAfterRender (float delta) {

    }

    @Override
    public Scene next () {
        return this;
    }

}
