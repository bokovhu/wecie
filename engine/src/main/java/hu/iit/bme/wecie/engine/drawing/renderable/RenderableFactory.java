package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.drawing.PrimitiveType;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class RenderableFactory {

    private RenderableFactory () {
        throw new UnsupportedOperationException ();
    }

    public static Renderable create () {
        return new RenderableImpl (PrimitiveType.triangles).init ();
    }

    public static IndexedRenderable createIndexed () {
        IndexedRenderable renderable = new IndexedRenderableImpl (PrimitiveType.triangles);
        renderable.init ();
        return renderable;
    }

    public static Renderable quad (Vector2f d) {

        return quad (
                new Vector2f (-d.x / 2f, d.y / 2f),
                new Vector2f (d.x / 2f, -d.y / 2f)
        );

    }

    public static Renderable quad (Vector2f topLeft, Vector2f bottomRight) {

        IndexedRenderableImpl renderable = new IndexedRenderableImpl (PrimitiveType.triangles);

        renderable.init ()
                .getVertexData ()
                .add (new Vertex ().position (topLeft.x, topLeft.y).texCoord (0f, 0f))
                .add (new Vertex ().position (bottomRight.x, topLeft.y).texCoord (1f, 0f))
                .add (new Vertex ().position (topLeft.x, bottomRight.y).texCoord (0f, 1f))
                .add (new Vertex ().position (topLeft.x, bottomRight.y).texCoord (0f, 1f))
                .add (new Vertex ().position (bottomRight.x, topLeft.y).texCoord (1f, 0f))
                .add (new Vertex ().position (bottomRight.x, bottomRight.y).texCoord (1f, 1f))
                .uploadToVBO ();

        return renderable;

    }

    public static Renderable cube (Vector3f d) {

        IndexedRenderable renderable = createIndexed ();
        VertexData vd = renderable.getVertexData ();

        // Front face, top left triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 0.0f, -1.0f)
                        .texCoord (0.25f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 0.0f, -1.0f)
                        .texCoord (0.5f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 0.0f, -1.0f)
                        .texCoord (0.25f, 0.66f)
        );
        // Front face, bottom right triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 0.0f, -1.0f)
                        .texCoord (0.25f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 0.0f, -1.0f)
                        .texCoord (0.5f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 0.0f, -1.0f)
                        .texCoord (0.5f, 0.66f)
        );

        // Left face, top left triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (-1.0f, 0.0f, -0.0f)
                        .texCoord (0.0f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (-1.0f, 0.0f, -0.0f)
                        .texCoord (0.25f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (-1.0f, 0.0f, -0.0f)
                        .texCoord (0.0f, 0.66f)
        );
        // Left face, bottom right triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (-1.0f, 0.0f, -0.0f)
                        .texCoord (0.0f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (-1.0f, 0.0f, -0.0f)
                        .texCoord (0.25f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (-1.0f, 0.0f, -0.0f)
                        .texCoord (0.25f, 0.66f)
        );

        // Right face, top left triangle
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (1.0f, 0.0f, 0.0f)
                        .texCoord (0.5f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (1.0f, 0.0f, 0.0f)
                        .texCoord (0.75f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (1.0f, 0.0f, 0.0f)
                        .texCoord (0.5f, 0.66f)
        );
        // Right face, bottom right triangle
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (1.0f, 0.0f, 0.0f)
                        .texCoord (0.5f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (1.0f, 0.0f, 0.0f)
                        .texCoord (0.75f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (1.0f, 0.0f, 0.0f)
                        .texCoord (0.75f, 0.66f)
        );

        // Back face, top left triangle
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 0.0f, 1.0f)
                        .texCoord (0.75f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 0.0f, 1.0f)
                        .texCoord (1.0f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 0.0f, 1.0f)
                        .texCoord (0.75f, 0.66f)
        );
        // Back face, bottom right triangle
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 0.0f, 1.0f)
                        .texCoord (0.75f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 0.0f, 1.0f)
                        .texCoord (1.0f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 0.0f, 1.0f)
                        .texCoord (1.0f, 0.66f)
        );

        // Top face, top left triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 1.0f, 0.0f)
                        .texCoord (0.25f, 0.0f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 1.0f, 0.0f)
                        .texCoord (0.5f, 0.0f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 1.0f, 0.0f)
                        .texCoord (0.25f, 0.33f)
        );
        // Top face, bottom right triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 1.0f, 0.0f)
                        .texCoord (0.25f, 0.33f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, 1.0f, 0.0f)
                        .texCoord (0.5f, 0.0f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, 1.0f, 0.0f)
                        .texCoord (0.5f, 0.33f)
        );

        // Bottom face, top left triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, -1.0f, 0.0f)
                        .texCoord (0.25f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, -1.0f, 0.0f)
                        .texCoord (0.5f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, -1.0f, 0.0f)
                        .texCoord (0.25f, 1.0f)
        );
        // Bottom face, bottom right triangle
        vd.add (
                new Vertex ()
                        .position (-d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, -1.0f, 0.0f)
                        .texCoord (0.25f, 1.0f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, -d.z / 2.0f)
                        .normal (0.0f, -1.0f, 0.0f)
                        .texCoord (0.5f, 0.66f)
        );
        vd.add (
                new Vertex ()
                        .position (d.x / 2.0f, -d.y / 2.0f, d.z / 2.0f)
                        .normal (0.0f, -1.0f, 0.0f)
                        .texCoord (0.5f, 1.0f)
        );

        vd.uploadToVBO ();

        return renderable;

    }

}
