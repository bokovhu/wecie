package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.CameraFactory;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

public abstract class BaseRenderPipelineStage<W extends World<E, D, L>, E extends Environment<L>, L extends Light, D extends DrawableComponent, P> implements
        RenderPipelineStage<W, E, L, D, P>,
        RenderingContext {

    protected FrameBuffer input;
    protected FrameBuffer output;
    protected Matrix4f mvp = new Matrix4f ();
    protected Camera camera = CameraFactory.create ();

    @Override
    public Camera getCamera () {
        return camera;
    }

    @Override
    public Matrix4f modelViewProjection () {
        return mvp;
    }

    @Override
    public RenderingContext transformModel (Matrix4f model) {
        mvp.identity ()
                .set (getCamera ().viewProjection ())
                .mul (model);
        return this;
    }

    protected void bindInput () {
        if (input != null) {
            input.bind ();
        } else {
            GL30.glBindFramebuffer (GL30.GL_FRAMEBUFFER, 0);
        }
    }

    protected void bindOutput () {
        if (output != null) {
            output.bind ();
        } else {
            GL30.glBindFramebuffer (GL30.GL_FRAMEBUFFER, 0);
        }
    }

    protected void clearOutput (int flags) {
        bindOutput ();
        GL30.glClear (flags);
    }

    @Override
    public RenderPipelineStage<W, E, L, D, P> input (FrameBuffer inputFramebuffer) {
        this.input = inputFramebuffer;
        return this;
    }

    @Override
    public RenderPipelineStage<W, E, L, D, P> output (FrameBuffer outputFramebuffer) {
        this.output = outputFramebuffer;
        return this;
    }

}
