package hu.iit.bme.wecie.engine.drawing.renderable;

import hu.iit.bme.wecie.engine.opengl.vao.VertexArrayObject;
import hu.iit.bme.wecie.engine.opengl.vbo.*;
import hu.iit.bme.wecie.engine.util.Buffers;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class IndexedVertexDataImpl implements IndexedVertexData {

    private final List<Vertex> data = new ArrayList<> ();
    private final List<Integer> indices = new ArrayList<> ();
    private VertexBufferObject vbo;
    private IndexBufferObject ebo;
    private int numVertices;

    @Override
    public IndexBufferObject getEBO () {
        return ebo;
    }

    @Override
    public VertexData init () {

        vbo = VertexBufferObjectFactory.vbo ();
        ebo = VertexBufferObjectFactory.ebo ();

        return this;
    }

    @Override
    public VertexData clear () {

        data.clear ();
        indices.clear ();
        numVertices = 0;

        return this;
    }

    @Override
    public VertexData add (Vertex vertex) {

        int index = data.indexOf (vertex);
        if (index < 0) {
            data.add (vertex);
            index = data.size () - 1;
        }
        indices.add (index);
        numVertices = indices.size ();

        return this;
    }

    @Override
    public VertexData add (Collection<Vertex> vertices) {
        vertices.forEach (this::add);
        return this;
    }

    @Override
    public VertexData configureVAO (VertexArrayObject vao) {

        vao.bind ()
                .vec4Attribute (vbo, 0, false, Vertex.SIZE, 0L)
                .vec3Attribute (vbo, 1, false, Vertex.SIZE, 4L)
                .vec2Attribute (vbo, 2, false, Vertex.SIZE, 7L)
                .vec4Attribute (vbo, 3, false, Vertex.SIZE, 9L);
        ebo.bind (BufferTarget.elementArray);

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

        IntBuffer ib = BufferUtils.createIntBuffer (numVertices ());
        for (int idx : indices) {
            ib.put (idx);
        }
        ebo.upload (ib.flip (), DataUsage.staticDraw);

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
        ebo.delete ();

        return this;
    }

}
