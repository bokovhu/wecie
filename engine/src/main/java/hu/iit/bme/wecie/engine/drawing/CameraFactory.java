package hu.iit.bme.wecie.engine.drawing;

public final class CameraFactory {

    private CameraFactory () {
        throw new UnsupportedOperationException ();
    }

    public static Camera create () {
        return new CameraImpl ();
    }

}
