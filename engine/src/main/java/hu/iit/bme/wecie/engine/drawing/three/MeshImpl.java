package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.drawing.renderable.IndexedRenderable;

class MeshImpl implements Mesh {

    private final IndexedRenderable renderable;
    private final String name;
    private Material material;

    MeshImpl (IndexedRenderable renderable, String name, Material material) {
        this.renderable = renderable;
        this.name = name;
        this.material = material;
    }

    @Override
    public IndexedRenderable getRenderable () {
        return renderable;
    }

    @Override
    public Material getMaterial () {
        return material;
    }

    @Override
    public Mesh changeMaterial (Material newMaterial) {
        this.material = newMaterial;
        return this;
    }

    @Override
    public String getName () {
        return name;
    }

}
