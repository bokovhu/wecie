package hu.iit.bme.wecie.engine.collision;

import org.joml.Vector3f;

public final class BoundingBox {

    private final Vector3f center = new Vector3f ();
    private final Vector3f halfDimensions = new Vector3f ();

    public BoundingBox () {

    }

    public BoundingBox set (Vector3f c, Vector3f hd) {
        this.center.set (c);
        this.halfDimensions.set (hd);
        return this;
    }

    public BoundingBox center (Vector3f c) {
        this.center.set (c);
        return this;
    }

    public BoundingBox halfDimensions (Vector3f hd) {
        this.halfDimensions.set (hd);
        return this;
    }

    public Vector3f getCenter () {
        return center;
    }

    public Vector3f getHalfDimensions () {
        return halfDimensions;
    }

}
