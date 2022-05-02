package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.drawing.renderable.*;
import org.joml.Math;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class MeshFactory {

    private static final Logger log = LoggerFactory.getLogger (MeshFactory.class);

    private MeshFactory () {
        throw new UnsupportedOperationException ();
    }

    public static Mesh sphere (
            float r,
            int sectorCount, int stackCount,
            Material material
    ) {

        float sectorStep = 2.0f * (float) Math.PI / (float) sectorCount;
        float stackStep = (float) Math.PI / (float) stackCount;
        float li = 1.0f / r;

        IndexedRenderable renderable = RenderableFactory.createIndexed ();
        VertexData vd = renderable.getVertexData ();

        List<Vertex> vertices = new ArrayList<> ();

        for (int i = 0; i <= stackCount; i++) {

            float stackAngle = (float) Math.PI / 2.0f - (float) i * stackStep;
            float xy = r * (float) Math.cos (stackAngle);
            float z = r * (float) Math.sin (stackAngle);

            for (int j = 0; j <= sectorCount; j++) {

                float sectorAngle = (float) j * sectorStep;

                float x = xy * (float) Math.cos (sectorAngle);
                float y = xy * (float) Math.sin (sectorAngle);
                vertices.add (
                        new Vertex ()
                                .position (x, y, z)
                                .normal (x * li, y * li, z * li)
                                .texCoord (
                                        (float) j / sectorCount,
                                        (float) i / stackCount
                                )
                );

            }

        }

        int k1, k2;
        for (int i = 0; i < stackCount; i++) {
            k1 = i * (sectorCount + 1);
            k2 = k1 + sectorCount + 1;

            for (int j = 0; j < sectorCount; j++) {

                if (i != 0) {
                    vd.add (vertices.get (k1 % vertices.size ()))
                            .add (vertices.get (k2 % vertices.size ()))
                            .add (vertices.get ((k1 + 1) % vertices.size ()));
                }

                if (i != (stackCount - 1)) {
                    vd.add (vertices.get ((k1 + 1) % vertices.size ()))
                            .add (vertices.get (k2 % vertices.size ()))
                            .add (vertices.get ((k2 + 1) % vertices.size ()));
                }

                k1++;
                k2++;
            }
        }

        vd.uploadToVBO ();

        return new MeshImpl (renderable, "sphere", material);

    }

    public static Mesh box (
            Vector3f d,
            Material material
    ) {

        IndexedRenderable renderable = RenderableFactory.createIndexed ();
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

        return new MeshImpl (renderable, "box", material);

    }

    public static Mesh create (IndexedRenderable renderable, String name, Material material) {

        MeshImpl mesh = new MeshImpl (renderable, name, material);
        return mesh;

    }

}
