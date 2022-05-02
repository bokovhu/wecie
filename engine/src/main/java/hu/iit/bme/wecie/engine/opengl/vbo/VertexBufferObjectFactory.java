package hu.iit.bme.wecie.engine.opengl.vbo;

public final class VertexBufferObjectFactory {

    private VertexBufferObjectFactory () {
        throw new UnsupportedOperationException ();
    }

    public static VertexBufferObject vbo () {

        return new VertexBufferObjectImpl ()
                .generate ();

    }

    public static IndexBufferObject ebo () {

        return new IndexBufferObjectImpl ()
                .generate ();

    }

}
