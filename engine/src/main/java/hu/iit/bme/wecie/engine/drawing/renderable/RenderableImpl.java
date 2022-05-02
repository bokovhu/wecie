package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.drawing.PrimitiveType;
import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObject;
import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObjectFactory;

class RenderableImpl implements Renderable {

    private final PrimitiveType primitiveType;
    private VertexArrayObject vao;
    private VertexData vertexData;

    RenderableImpl (PrimitiveType primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override
    public Renderable init () {

        vao = VertexArrayObjectFactory.create ().generate ();
        vertexData = VertexDataFactory.create ()
                .init ()
                .configureVAO (vao);

        return this;
    }

    @Override
    public VertexArrayObject getVAO () {
        return vao;
    }

    @Override
    public VertexData getVertexData () {
        return vertexData;
    }

    @Override
    public PrimitiveType getPrimitiveType () {
        return primitiveType;
    }

    @Override
    public Renderable draw () {

        vao.bind ()
                .draw (primitiveType, 0, vertexData.numVertices ());

        return this;
    }

    @Override
    public Renderable delete () {

        vertexData.delete ();
        vao.delete ();

        return this;
    }

}
