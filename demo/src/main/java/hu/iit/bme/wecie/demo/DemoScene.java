package hu.iit.bme.wecie.demo;

import hu.iit.bme.wecie.engine.GameLauncher;
import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.asset.AssetSystem;
import hu.iit.bme.wecie.engine.debug.GLDebug;
import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.input.InputHandler;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBufferFactory;
import hu.iit.bme.wecie.engine.opengl.texture.ImageDataFactory;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;
import hu.iit.bme.wecie.engine.scene.*;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.scene.pbr.PBRPipeline;
import hu.iit.bme.wecie.engine.scene.pbr.PBRPipelineParameters;
import hu.iit.bme.wecie.engine.scene.pipeline.PostProcessStage;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import hu.iit.bme.wecie.engine.scene.world.World3D;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DemoScene extends GameScene implements InputHandler {

    private Layer<Environment3D, Model, Light3D, PBRPipelineParameters> layer;

    private World3D world;
    private Environment3D environment;
    private GameObject<Model> testGameObject;

    private Light3D ambientLight;
    private Light3D spotLight;
    private boolean spotLightFollowsCamera = true;

    private float timer = 0.0f;
    private Vector3f cameraPosition = new Vector3f (0.0f, 0.0f, 0f);
    private Vector3f cameraTarget = new Vector3f (0.0f, 0f, -1.0f);
    private Vector3f cameraUp = new Vector3f (0.0f, 1.0f, 0.0f);
    private Vector3f lightPosition = new Vector3f (0f, 1f, 0f);
    private Vector3f lightEnergy = new Vector3f (40f, 38f, 20f);

    private static final float CAMERA_SPEED = 2.0f;
    private Vector3f actualCameraVelocity = new Vector3f (0f);
    private Vector3f cameraVelocity = new Vector3f (0f);
    private Vector3f cameraTargetVelocity = new Vector3f (0f);
    private Vector3f tmp = new Vector3f (0f);
    private Vector3f cameraLookDirection = new Vector3f (0f, 0f, 0f)
            .set (cameraTarget)
            .sub (cameraPosition)
            .normalize ();
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private float lastMouseX = 0.0f;
    private float lastMouseY = 0.0f;

    private float cameraInterpolationTimer = 0.0f;

    private boolean[] cameraMovement = new boolean[4];

    @Override
    public void onSceneCreated () {

        GameLauncher.setInputHandler (this);

        AssetSystem.getInstance ()
                .enqueModel ("test-game-object", "models/corridor-scene", getClass ().getClassLoader ())
                .enqueCubemap ("miramar", "skymaps/miramar", getClass ().getClassLoader ())
                .enqueProgram ("postprocess", "shaders/demo/postprocess", getClass ().getClassLoader ());

        world = new World3D ();
        environment = world.getEnvironment ();

        ambientLight = environment.newLight ()
                .ambient (new Vector3f (0.1f, 0.075f, 0.02f));
        environment.addLight (ambientLight);

        spotLight = environment.newLight ()
                .spot (
                        lightPosition.set (cameraPosition),
                        cameraLookDirection,
                        (float) Math.toRadians (15f),
                        (float) Math.toRadians (20f),
                        lightEnergy,
                        1.0f,
                        0.09f,
                        0.032f
                ).enableShadows ();
        environment.addLight (spotLight);

        GL30.glShadeModel (GL33.GL_SMOOTH);

    }


    private void createGameObject () {
        world.removeAllGameObjects ();
        testGameObject = world.newGameObject (
                AssetSystem.getInstance ().getModel ("test-game-object")
        );
        world.addGameObject (testGameObject);
    }

    @Override
    public void onSceneAssetsLoaded () {

        layer = LayerFactory.new3DLayer ();
        addLayer (layer);
        createGameObject ();

        PBRPipelineParameters pipelineParameters = new PBRPipelineParameters ();
        pipelineParameters.setSkymap (AssetSystem.getInstance ().getCubemap ("miramar"));

        PBRPipeline pipeline = new PBRPipeline ();

        layer.forRenderPipeline (
                pipeline
        )
                .forWorld (world)
                .withPipelineParameters (pipelineParameters);
        layer.camera ()
                .lookAt (
                        cameraPosition, cameraTarget, cameraUp
                ).perspective (
                (float) Math.toRadians (60.0),
                (float) GameWindow.getInstance ().getWindowWidth () / (float) GameWindow.getInstance ()
                        .getWindowHeight (),
                0.1f,
                100.0f
        );

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

        timer += delta;
        cameraInterpolationTimer += delta;

        cameraLookDirection.set (
                (float) (Math.cos (Math.toRadians (pitch)) * Math.cos (Math.toRadians (yaw))),
                (float) (Math.sin (Math.toRadians (pitch))),
                (float) (Math.cos (Math.toRadians (pitch)) * Math.sin (Math.toRadians (yaw)))
        );
        cameraLookDirection.normalize ();
        cameraTarget.set (cameraPosition)
                .add (cameraLookDirection);

        cameraTargetVelocity.set (0f, 0f, 0f);

        if (cameraMovement[0]) {
            // Move forward
            cameraTargetVelocity.add (layer.camera ().front ());
        }

        if (cameraMovement[1]) {
            cameraTargetVelocity.sub (layer.camera ().right ());
        }

        if (cameraMovement[2]) {
            cameraTargetVelocity.add (
                    tmp.set (layer.camera ().front ()).mul (-1.0f)
            );
        }

        if (cameraMovement[3]) {
            cameraTargetVelocity.add (layer.camera ().right ());
        }

        cameraTargetVelocity.normalize ()
                .mul (CAMERA_SPEED);

        if (!cameraMovement [0] && !cameraMovement [1] && !cameraMovement [2] && !cameraMovement [3]) {
            cameraTargetVelocity.set (0f);
        }

        cameraVelocity.lerp (cameraTargetVelocity, Math.min (cameraInterpolationTimer, 1.0f));

        layer.camera ()
                .lookAt (
                        cameraPosition.add (
                                actualCameraVelocity.set (cameraVelocity)
                                        .mul (delta)
                        ),
                        cameraTarget,
                        cameraUp
                );

        if (spotLightFollowsCamera) {
            spotLight.spot (
                    lightPosition.set (cameraPosition),
                    cameraLookDirection,
                    spotLight.getCutOffAngle (),
                    spotLight.getOuterCutOffAngle (),
                    lightEnergy,
                    spotLight.getConstant (),
                    spotLight.getLinear (),
                    spotLight.getQuadratic ()
            );
        }

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

    @Override
    public void onKeyPressed (int key, int mods) {

        switch (key) {
            case GLFW.GLFW_KEY_W:
                cameraMovement[0] = true;
                break;
            case GLFW.GLFW_KEY_D:
                cameraMovement[1] = true;
                break;
            case GLFW.GLFW_KEY_S:
                cameraMovement[2] = true;
                break;
            case GLFW.GLFW_KEY_A:
                cameraMovement[3] = true;
                break;
        }

        cameraInterpolationTimer = 0f;

    }

    @Override
    public void onKeyReleased (int key, int mods) {

        switch (key) {
            case GLFW.GLFW_KEY_ESCAPE:
                GameLauncher.exitGame ();
                break;
            case GLFW.GLFW_KEY_W:
                cameraMovement[0] = false;
                break;
            case GLFW.GLFW_KEY_D:
                cameraMovement[1] = false;
                break;
            case GLFW.GLFW_KEY_S:
                cameraMovement[2] = false;
                break;
            case GLFW.GLFW_KEY_A:
                cameraMovement[3] = false;
                break;
            case GLFW.GLFW_KEY_ENTER:
                spotLightFollowsCamera = !spotLightFollowsCamera;
                break;
        }

        cameraInterpolationTimer = 0f;

    }

    @Override
    public void onMouseMoved (float x, float y) {

        float dx = lastMouseX - x;
        float dy = lastMouseY - y;

        lastMouseX = x;
        lastMouseY = y;

        yaw -= dx * 0.1f;
        pitch += dy * 0.1f;

        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if (pitch < -89.0f) {
            pitch = -89.0f;
        }

    }

}
