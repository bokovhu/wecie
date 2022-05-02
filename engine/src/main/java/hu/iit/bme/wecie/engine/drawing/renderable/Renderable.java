package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.drawing.PrimitiveType;
import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObject;

public interface Renderable {

    Renderable init ();

    VertexArrayObject getVAO ();

    VertexData getVertexData ();

    PrimitiveType getPrimitiveType ();

    Renderable draw ();

    Renderable delete ();

}
