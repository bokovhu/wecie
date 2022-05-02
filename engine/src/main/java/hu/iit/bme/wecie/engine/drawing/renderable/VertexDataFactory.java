package hu.iit.bme.wecie.engine.drawing.renderable;

public final class VertexDataFactory {

    private VertexDataFactory () {
        throw new UnsupportedOperationException ();
    }

    public static VertexData create () {
        return new VertexDataImpl ();
    }

    public static IndexedVertexData createIndexed () {
        return new IndexedVertexDataImpl ();
    }

}
