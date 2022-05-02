package hu.iit.bme.wecie.engine.opengl.vao;

import hu.iit.bme.wecie.engine.drawing.PrimitiveType;
import hu.iit.bme.wecie.engine.opengl.vbo.IndexBufferObject;
import hu.iit.bme.wecie.engine.opengl.vbo.VertexBufferObject;

public interface VertexArrayObject {

    int getId ();

    VertexArrayObject generate ();

    VertexArrayObject bind ();

    VertexArrayObject attribute (
            VertexBufferObject vbo,
            int index,
            int size,
            VertexAttributeType type,
            boolean normalized,
            int stride,
            long offset
    );

    VertexArrayObject vec2Attribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    );

    VertexArrayObject vec3Attribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    );

    VertexArrayObject vec4Attribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    );

    VertexArrayObject floatAttribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    );

    VertexArrayObject draw (PrimitiveType primitiveType, int first, int count);

    VertexArrayObject drawIndexed (PrimitiveType primitiveType, IndexBufferObject ebo, int first, int count);

    VertexArrayObject delete ();

}
