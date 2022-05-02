package hu.iit.bme.wecie.engine.scene.pbr;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;

public class PBRPipelineParameters {

    private Texture skymap;
    private float bloomThreshold = 0.8f;
    private int bloomIterations = 10;

    public Texture getSkymap () {
        return skymap;
    }

    public void setSkymap (Texture skymap) {
        this.skymap = skymap;
    }

    public float getBloomThreshold () {
        return bloomThreshold;
    }

    public void setBloomThreshold (float bloomThreshold) {
        this.bloomThreshold = bloomThreshold;
    }

    public int getBloomIterations () {
        return bloomIterations;
    }

    public void setBloomIterations (int bloomIterations) {
        this.bloomIterations = bloomIterations;
    }

}
