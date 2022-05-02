package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObject;
import hu.iit.bme.wecie.engine.opengl.vbo.VertexBufferObject;

import java.util.Collection;

public interface VertexData {

    VertexData init ();

    VertexData clear ();

    VertexData add (Vertex vertex);

    VertexData add (Collection<Vertex> vertices);

    VertexData configureVAO (VertexArrayObject vao);

    VertexData uploadToVBO ();

    VertexBufferObject getVBO ();

    int numVertices ();

    VertexData delete ();

}
