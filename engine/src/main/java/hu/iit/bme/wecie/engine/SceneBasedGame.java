package hu.iit.bme.wecie.engine;

import hu.iit.bme.wecie.engine.asset.AssetSystem;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.scene.Layer;
import hu.iit.bme.wecie.engine.scene.Scene;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class SceneBasedGame implements Game {

    private Scene currentScene;
    private Integer numAssetsToLoad = null;
    private Program loadingBarProgram;

    public SceneBasedGame (Scene firstScene) {
        this.currentScene = firstScene;
    }

    private void displayLoadingScreen () {

        if (!AssetSystem.getInstance ().isFullyLoaded ()) {

            Renderable loadingBar = RenderableFactory.quad (
                    new Vector2f (-1f, 0.1f),
                    new Vector2f (2f * AssetSystem.getInstance ().getProgress () - 1f, -0.1f)
            );

            GL20.glClearColor (0f, 0f, 0f, 1f);
            GL20.glClear (GL20.GL_COLOR_BUFFER_BIT);

            loadingBarProgram.use ();
            loadingBar.draw ();

            GameWindow.getInstance ().onFrameRendered ();

        }

    }

    @Override
    public void onCreate () {

        loadingBarProgram = ProgramFactory.createFromResources (
                "glsl/loading-screen-vertex.glsl",
                "glsl/loading-screen-fragment.glsl",
                getClass ().getClassLoader ()
        );

        currentScene.onSceneCreated ();

        AssetSystem.getInstance ().performPreProcessing ();

        displayLoadingScreen ();
        while (!AssetSystem.getInstance ().isFullyLoaded ()) {
            displayLoadingScreen ();
            AssetSystem.getInstance ().loadNext ();
        }

        GL20.glUseProgram (0);
        loadingBarProgram.delete ();

        currentScene.onSceneAssetsLoaded ();
        currentScene.onSceneResumed ();

    }

    @Override
    public void onUpdate (float delta) {

        currentScene.onUpdate (delta);
        Scene next = currentScene.next ();
        if (next != currentScene) {
            currentScene.onScenePaused ();
            currentScene.onSceneFinished ();

            currentScene = next;
            currentScene.onSceneCreated ();
            currentScene.onSceneResumed ();
        }

    }

    @Override
    public void onRender (float delta) {

        GL30.glBindFramebuffer (GL30.GL_FRAMEBUFFER, 0);
        GL20.glClearColor (0f, 0f, 0f, 1f);
        GL20.glClear (GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        currentScene.onBeforeRender (delta);

        for (Layer <?, ?, ?, ?> layer : currentScene.getLayers ()) {
            layer.draw ();
        }

        currentScene.onAfterRender (delta);

    }

    @Override
    public void onExit () {

        currentScene.onScenePaused ();
        currentScene.onSceneFinished ();

    }

}
