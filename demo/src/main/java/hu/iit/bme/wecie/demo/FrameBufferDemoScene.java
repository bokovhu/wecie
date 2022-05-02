package hu.iit.bme.wecie.demo;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.asset.AssetSystem;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBufferFactory;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;
import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import hu.iit.bme.wecie.engine.scene.GameScene;
import hu.iit.bme.wecie.engine.scene.Scene;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FrameBufferDemoScene extends GameScene {

    private Program raymarcherProgram;
    private Program postProcessProgram;
    private FrameBuffer frameBuffer;
    private Renderable fullScreenQuad;
    private float timer = 0.0f;
    private Vector2f resolution;

    @Override
    public void onSceneCreated () {

        AssetSystem.getInstance ()
                .enqueProgram (
                        "raymarcher",
                        "shaders/raymarcher",
                        getClass ().getClassLoader ()
                )
                .enqueProgram (
                        "raymarcher-postprocess",
                        "shaders/raymarcher/postprocess",
                        getClass ().getClassLoader ()
                );

    }

    @Override
    public void onSceneAssetsLoaded () {

        frameBuffer = FrameBufferFactory.create ()
                .attachColor (
                        0,
                        TextureFactory.empty (
                                GameWindow.getInstance ().getWindowWidth (),
                                GameWindow.getInstance ().getWindowHeight (),
                                new Vector4f (0f, 0f, 0f, 1f)
                        )
                );

        raymarcherProgram = AssetSystem.getInstance ().getProgram ("raymarcher");
        postProcessProgram = AssetSystem.getInstance ().getProgram ("raymarcher-postprocess");
        fullScreenQuad = RenderableFactory.quad (
                new Vector2f (-1f, 1f),
                new Vector2f (1f, -1f)
        );
        resolution = new Vector2f (
                GameWindow.getInstance ().getWindowWidth (),
                GameWindow.getInstance ().getWindowHeight ()
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

    }

    @Override
    public void onBeforeRender (float delta) {

    }

    @Override
    public void onAfterRender (float delta) {

        // Draw into the frame buffer
        frameBuffer.bind ()
                .clear (GL11.GL_COLOR_BUFFER_BIT);

        raymarcherProgram.use ()
                .setUniform ("u_resolution", resolution)
                .setUniform ("u_time", timer);
        fullScreenQuad.draw ();

        GL30.glBindFramebuffer (GL30.GL_FRAMEBUFFER, 0);

        // Draw frame buffer's content onto screen
        postProcessProgram.use ();
        frameBuffer.colorAttachment (0)
                .bind (TextureTarget.texture2D, 0);
        postProcessProgram.setUniform ("u_texture", 0);
        fullScreenQuad.draw ();

    }

    @Override
    public Scene next () {
        return this;
    }

}
