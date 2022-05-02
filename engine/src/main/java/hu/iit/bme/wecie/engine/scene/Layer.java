package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

public interface Layer<E extends Environment<L>, D extends DrawableComponent, L extends Light, P> {

    boolean isEnabled ();

    Layer<E, D, L, P> enable ();

    Layer<E, D, L, P> disable ();

    Layer<E, D, L, P> forWorld (World<E, D, L> world);

    Layer<E, D, L, P> forRenderPipeline (RenderPipeline<World<E, D, L>, E, L, D, P> pipeline);

    Layer<E, D, L, P> withPipelineParameters (P pipelineParameters);

    Layer<E, D, L, P> draw ();

    Camera camera ();

}
