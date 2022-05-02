package hu.iit.bme.wecie.engine.collision;

import org.joml.Vector3f;

public class BoundingSphere {

    private final Vector3f center = new Vector3f ();
    private float radius = 0.0f;

    public BoundingSphere () {
    }

    public BoundingSphere set (Vector3f c, float r) {
        this.center.set (c);
        this.radius = r;
        return this;
    }

    public BoundingSphere center (Vector3f c) {
        this.center.set (c);
        return this;
    }

    public BoundingSphere radius (float r) {
        this.radius = r;
        return this;
    }

    public Vector3f getCenter () {
        return center;
    }

    public float getRadius () {
        return radius;
    }

}
