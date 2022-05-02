package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.drawing.PrimitiveType;
import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObject;
import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObjectFactory;

class IndexedRenderableImpl implements IndexedRenderable {

    private final PrimitiveType primitiveType;
    private VertexArrayObject vao;
    private IndexedVertexData vertexData;

    IndexedRenderableImpl (PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override
    public Renderable init () {

        vao = VertexArrayObjectFactory.create ();
        vertexData = VertexDataFactory.createIndexed ();
        vertexData.init ().configureVAO (vao);

        return this;
    }

    @Override
    public VertexArrayObject getVAO () {
        return vao;
    }

    @Override
    public IndexedVertexData getVertexData () {
        return vertexData;
    }

    @Override
    public PrimitiveType getPrimitiveType () {
        return primitiveType;
    }

    @Override
    public Renderable draw () {

        vao.bind ()
                .drawIndexed (primitiveType, vertexData.getEBO (), 0, vertexData.numVertices ());

        return this;
    }

    @Override
    public Renderable delete () {

        vertexData.delete ();
        vao.delete ();
        return this;

    }

}
