package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.opengl.vbo.IndexBufferObject;

public interface IndexedVertexData extends VertexData {

    IndexBufferObject getEBO ();

}
