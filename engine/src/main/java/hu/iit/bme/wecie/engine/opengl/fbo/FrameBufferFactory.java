package hu.iit.bme.wecie.engine.opengl.fbo;

public final class FrameBufferFactory {

    private FrameBufferFactory () {
        throw new UnsupportedOperationException ();
    }

    public static FrameBuffer create () {

        return new FrameBufferImpl ()
                .generate ();

    }

}
