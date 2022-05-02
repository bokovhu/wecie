package hu.iit.bme.wecie.engine.opengl.vao;

public final class VertexArrayObjectFactory {

    private VertexArrayObjectFactory () {
        throw new UnsupportedOperationException ();
    }

    public static VertexArrayObject create () {

        return new VertexArrayObjectImpl ().generate ();

    }

}
