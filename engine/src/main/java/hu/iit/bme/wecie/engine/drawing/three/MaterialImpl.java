package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;

class MaterialImpl implements Material {

    private Texture albedoMap;
    private Texture metallicMap;
    private Texture roughnessMap;
    private Texture ambientOcclusionMap;
    private Texture normalMap;

    @Override
    public Texture getAlbedoMap () {
        return albedoMap;
    }

    void setAlbedoMap (Texture albedoMap) {
        this.albedoMap = albedoMap;
    }

    @Override
    public Texture getMetallicMap () {
        return metallicMap;
    }

    void setMetallicMap (Texture metallicMap) {
        this.metallicMap = metallicMap;
    }

    @Override
    public Texture getRoughnessMap () {
        return roughnessMap;
    }

    void setRoughnessMap (Texture roughnessMap) {
        this.roughnessMap = roughnessMap;
    }

    @Override
    public Texture getAmbientOcclusionMap () {
        return ambientOcclusionMap;
    }

    void setAmbientOcclusionMap (Texture ambientOcclusionMap) {
        this.ambientOcclusionMap = ambientOcclusionMap;
    }

    @Override
    public Texture getNormalMap () {
        return normalMap;
    }

    void setNormalMap (Texture normalMap) {
        this.normalMap = normalMap;
    }

}
