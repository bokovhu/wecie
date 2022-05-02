package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.util.Buffers;
import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObject;
import hu.iit.bme.wecie.engine.opengl.vbo.BufferTarget;
import hu.iit.bme.wecie.engine.opengl.vbo.DataUsage;
import hu.iit.bme.wecie.engine.opengl.vbo.VertexBufferObject;
import hu.iit.bme.wecie.engine.opengl.vbo.VertexBufferObjectFactory;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class VertexDataImpl implements VertexData {

    private final List<Vertex> data = new ArrayList<> ();
    private VertexBufferObject vbo;
    private int numVertices = 0;

    @Override
    public VertexData init () {

        vbo = VertexBufferObjectFactory.vbo ();

        return this;
    }

    @Override
    public VertexData clear () {
        data.clear ();
        numVertices = 0;
        return this;
    }

    @Override
    public VertexData add (Vertex vertex) {
        data.add (vertex);
        numVertices = data.size ();
        return this;
    }

    @Override
    public VertexData add (Collection<Vertex> vertices) {
        data.addAll (vertices);
        numVertices = data.size ();
        return this;
    }

    @Override
    public VertexData configureVAO (VertexArrayObject vao) {

        vao.bind ()
                .vec4Attribute (vbo, 0, false, Vertex.SIZE, 0L)
                .vec3Attribute (vbo, 1, false, Vertex.SIZE, 4L)
                .vec2Attribute (vbo, 2, false, Vertex.SIZE, 7L)
                .vec4Attribute (vbo, 3, false, Vertex.SIZE, 9L);

        return this;
    }

    @Override
    public VertexData uploadToVBO () {

        FloatBuffer fb = BufferUtils.createFloatBuffer (numVertices () * Vertex.SIZE);
        for (Vertex v : data) {
            Buffers.put (fb, v.getPosition ());
            Buffers.put (fb, v.getNormal ());
            Buffers.put (fb, v.getTexCoord ());
            Buffers.put (fb, v.getColor ());
        }

        vbo.bind (BufferTarget.array)
                .upload (fb.flip (), DataUsage.staticDraw);

        return this;
    }

    @Override
    public VertexBufferObject getVBO () {
        return vbo;
    }

    @Override
    public int numVertices () {
        return numVertices;
    }

    @Override
    public VertexData delete () {

        vbo.delete ();
        return this;

    }

}
