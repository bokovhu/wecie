package hu.iit.bme.wecie.engine.scene.pipeline;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.Environment;
import hu.iit.bme.wecie.engine.scene.Light;
import hu.iit.bme.wecie.engine.scene.World;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;

public class PostProcessStage<W extends World<E, D, L>, E extends Environment<L>, L extends Light, D extends DrawableComponent, P> extends
        BaseRenderPipelineStage<W, E, L, D, P> {

    private final Program postProcessor;
    private final Renderable fullScreenQuad = RenderableFactory.quad (new Vector2f (2f, 2f));

    public PostProcessStage (Program postProcessor) {
        this.postProcessor = postProcessor;
    }

    @Override
    public void execute (W world, Camera camera, P pipelineParams) {

        bindOutput ();
        clearOutput (GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        input.colorAttachment (0).bind (TextureTarget.texture2D, 0);
        postProcessor.use ();
        postProcessor.setUniform ("u_input", 0);
        fullScreenQuad.draw ();

    }

    @Override
    public Program getProgram () {
        return null;
    }

}
