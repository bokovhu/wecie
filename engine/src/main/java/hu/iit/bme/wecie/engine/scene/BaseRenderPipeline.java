package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

import java.util.ArrayList;
import java.util.List;

public class BaseRenderPipeline<W extends World<E, D, L>, E extends Environment<L>, L extends Light, D extends DrawableComponent, P> implements
        RenderPipeline<W, E, L, D, P> {

    private final List<RenderPipelineStage<W, E, L, D, P>> stages = new ArrayList<> ();

    @Override
    public RenderPipeline<W, E, L, D, P> then (RenderPipelineStage<W, E, L, D, P> stage) {
        this.stages.add (stage);
        return this;
    }

    @Override
    public void run (W world, Camera camera, P pipelineParams) {

        for (RenderPipelineStage<W, E, L, D, P> stage : stages) {
            stage.execute (world, camera, pipelineParams);
        }

    }

}
