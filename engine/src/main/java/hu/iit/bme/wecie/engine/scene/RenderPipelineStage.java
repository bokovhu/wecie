package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;

public interface RenderPipelineStage<W extends World<E, D, L>, E extends Environment<L>, L extends Light, D extends DrawableComponent, P> {

    void execute (
            W world,
            Camera camera,
            P pipelineParams
    );

    RenderPipelineStage<W, E, L, D, P> input (FrameBuffer inputFramebuffer);

    RenderPipelineStage<W, E, L, D, P> output (FrameBuffer outputFramebuffer);

}
