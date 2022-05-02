package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.CameraFactory;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LayerImpl<E extends Environment<L>, D extends DrawableComponent, L extends Light, P> implements
        Layer<E, D, L, P> {

    private final List<GameObject<D>> gameObjects = new ArrayList<> ();
    private final Camera camera = CameraFactory.create ();
    private boolean enabled = true;
    private World<E, D, L> world;
    private RenderPipeline<World<E, D, L>, E, L, D, P> renderPipeline;
    private P pipelineParameters;

    LayerImpl () {
    }

    @Override
    public boolean isEnabled () {
        return enabled;
    }

    @Override
    public Layer<E, D, L, P> enable () {
        this.enabled = true;
        return this;
    }

    @Override
    public Layer<E, D, L, P> disable () {
        this.enabled = false;
        return this;
    }

    @Override
    public Layer<E, D, L, P> forWorld (World<E, D, L> world) {
        this.world = world;
        return this;
    }

    @Override
    public Layer<E, D, L, P> forRenderPipeline (RenderPipeline<World<E, D, L>, E, L, D, P> pipeline) {
        this.renderPipeline = pipeline;
        return this;
    }

    @Override
    public Layer<E, D, L, P> withPipelineParameters (P pipelineParameters) {
        this.pipelineParameters = pipelineParameters;
        return this;
    }

    @Override
    public Layer<E, D, L, P> draw () {
        this.renderPipeline.run (
                world,
                camera,
                pipelineParameters
        );
        return this;
    }

    @Override
    public Camera camera () {
        return camera;
    }

}
