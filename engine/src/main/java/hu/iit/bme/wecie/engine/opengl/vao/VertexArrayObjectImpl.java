package hu.iit.bme.wecie.engine.opengl.vao;

import hu.iit.bme.wecie.engine.drawing.PrimitiveType;
import hu.iit.bme.wecie.engine.opengl.vbo.BufferTarget;
import hu.iit.bme.wecie.engine.opengl.vbo.IndexBufferObject;
import hu.iit.bme.wecie.engine.opengl.vbo.VertexBufferObject;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

class VertexArrayObjectImpl implements VertexArrayObject {

    private int glId;
    private int currentVboId = -1;
    private Set<Integer> enabledAttribs = new HashSet<> ();

    @Override
    public int getId () {
        return glId;
    }

    @Override
    public VertexArrayObject generate () {

        this.glId = GL45.glGenVertexArrays ();

        return this;
    }

    @Override
    public VertexArrayObject bind () {

        GL45.glBindVertexArray (glId);

        return this;
    }

    @Override
    public VertexArrayObject attribute (
            VertexBufferObject vbo,
            int index,
            int size,
            VertexAttributeType type,
            boolean normalized,
            int stride,
            long offset
    ) {

        if (currentVboId != vbo.getId ()) {
            vbo.bind (BufferTarget.array);
            currentVboId = vbo.getId ();
        }

        GL45.glEnableVertexAttribArray (index);
        GL45.glVertexAttribPointer (
                index,
                size,
                type.getGlValue (),
                normalized,
                stride * type.getSizeInBytes (),
                offset * type.getSizeInBytes ()
        );
        enabledAttribs.add (index);

        return this;
    }

    @Override
    public VertexArrayObject vec2Attribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    ) {

        return attribute (vbo, index, 2, VertexAttributeType.glFloat, normalized, stride, offset);

    }

    @Override
    public VertexArrayObject vec3Attribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    ) {

        return attribute (vbo, index, 3, VertexAttributeType.glFloat, normalized, stride, offset);

    }

    @Override
    public VertexArrayObject vec4Attribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    ) {

        return attribute (vbo, index, 4, VertexAttributeType.glFloat, normalized, stride, offset);

    }

    @Override
    public VertexArrayObject floatAttribute (
            VertexBufferObject vbo,
            int index,
            boolean normalized,
            int stride,
            long offset
    ) {

        return attribute (vbo, index, 1, VertexAttributeType.glFloat, normalized, stride, offset);

    }

    @Override
    public VertexArrayObject draw (PrimitiveType primitiveType, int first, int count) {

        bind ();
        GL20.glDrawArrays (primitiveType.getGlValue (), first, count);

        return this;
    }

    @Override
    public VertexArrayObject drawIndexed (PrimitiveType primitiveType, IndexBufferObject ebo, int first, int count) {

        bind ();
        GL20.glDrawElements (
                primitiveType.getGlValue (),
                count,
                GL20.GL_UNSIGNED_INT,
                MemoryUtil.NULL
        );

        return this;

    }

    @Override
    public VertexArrayObject delete () {

        GL30.glBindVertexArray (0);
        GL30.glDeleteVertexArrays (glId);

        return this;
    }

}
