package hu.iit.bme.wecie.engine.drawing.renderable;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class Vertex {

    public static final int SIZE = 4 + 3 + 2 + 4;

    private final Vector4f position = new Vector4f ();
    private final Vector3f normal = new Vector3f ();
    private final Vector2f texCoord = new Vector2f ();
    private final Vector4f color = new Vector4f (1.0f);

    public Vertex (Vector4f position, Vector3f normal, Vector2f texCoord, Vector4f color) {
        this.position.set (position);
        this.normal.set (normal);
        this.texCoord.set (texCoord);
        this.color.set (color);
    }

    public Vertex () {
    }

    public Vertex position (float x, float y, float z) {
        this.position.set (x, y, z, 1.0f);
        return this;
    }

    public Vertex position (float x, float y) {
        this.position.set (x, y, 0.0f, 1.0f);
        return this;
    }

    public Vertex position (float x, float y, float z, float w) {
        this.position.set (x, y, z, w);
        return this;
    }

    public Vertex position (Vector3f v) {
        this.position.set (v, 1.0f);
        return this;
    }

    public Vertex position (Vector2f v) {
        this.position.set (v, 0.0f, 1.0f);
        return this;
    }

    public Vertex position (Vector4f v) {
        this.position.set (v);
        return this;
    }

    public Vertex normal (float x, float y, float z) {
        this.normal.set (x, y, z);
        return this;
    }

    public Vertex normal (Vector3f v) {
        this.normal.set (v);
        return this;
    }

    public Vertex texCoord (float u, float v) {
        this.texCoord.set (u, v);
        return this;
    }

    public Vertex texCoord (Vector2f v) {
        this.texCoord.set (v);
        return this;
    }

    public Vertex color (float r, float g, float b) {
        this.color.set (r, g, b, 1.0f);
        return this;
    }

    public Vertex color (float r, float g, float b, float a) {
        this.color.set (r, g, b, a);
        return this;
    }

    public Vertex color (Vector3f c) {
        this.color.set (c, 1.0f);
        return this;
    }

    public Vertex color (Vector4f c) {
        this.color.set (c);
        return this;
    }

    public Vector4f getPosition () {
        return position;
    }

    public Vector3f getNormal () {
        return normal;
    }

    public Vector2f getTexCoord () {
        return texCoord;
    }

    public Vector4f getColor () {
        return color;
    }

}
