package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

public interface RenderPipeline<W extends World<E, D, L>, E extends Environment<L>, L extends Light, D extends DrawableComponent, P> {

    RenderPipeline<W, E, L, D, P> then (
            RenderPipelineStage<W, E, L, D, P> stage
    );

    void run (
            W world,
            Camera camera,
            P pipelineParams
    );

}
