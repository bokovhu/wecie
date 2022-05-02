package hu.iit.bme.wecie.engine.opengl.shader;

import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL45;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

class ProgramImpl implements Program {

    private final Map<String, Integer> uniformLocationCache = new HashMap<> ();
    private final Map<String, Integer> vertexAttributeLocationCache = new HashMap<> ();
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer (4 * 4);
    private int glId;
    private Shader vertexShader;
    private Shader fragmentShader;
    private Shader geometryShader;
    private Shader tesselationControlShader;
    private Shader tesselationEvaluationShader;
    private boolean linked = false;
    private boolean validated = false;

    @Override
    public Shader getVertexShader () {
        return vertexShader;
    }

    @Override
    public Shader getFragmentShader () {
        return fragmentShader;
    }

    @Override
    public Shader getGeometryShader () {
        return geometryShader;
    }

    @Override
    public Shader getTesselationControlShader () {
        return tesselationControlShader;
    }

    @Override
    public Shader getTesselationEvaluationShader () {
        return tesselationEvaluationShader;
    }

    @Override
    public Program generate () {

        glId = GL20.glCreateProgram ();

        return this;
    }

    @Override
    public Program attach (Shader shader) {

        GL20.glAttachShader (glId, shader.getId ());

        if (shader.getType () == ShaderType.vertex) {
            this.vertexShader = shader;
        } else if (shader.getType () == ShaderType.fragment) {
            this.fragmentShader = shader;
        } else if (shader.getType () == ShaderType.geometry) {
            this.geometryShader = shader;
        } else if (shader.getType () == ShaderType.tesselationControl) {
            this.tesselationControlShader = shader;
        } else if (shader.getType () == ShaderType.tesselationEvaluation) {
            this.tesselationEvaluationShader = shader;
        }

        return this;
    }

    @Override
    public Program bindDataLocation (int colorNumber, String name) {

        if (isLinked ()) {
            throw new IllegalStateException ("Can only bind frag data location when the program is not yet linked!");
        }

        GL45.glBindFragDataLocation (glId, colorNumber, name);

        return this;
    }

    @Override
    public Program link () {

        GL20.glLinkProgram (glId);
        int status = GL20.glGetProgrami (glId, GL20.GL_LINK_STATUS);

        if (status == GL11.GL_TRUE) {
            linked = true;

            if (vertexShader != null) {
                vertexShader.delete ();
            }
            if (fragmentShader != null) {
                fragmentShader.delete ();
            }
            if (geometryShader != null) {
                geometryShader.delete ();
            }
            if (tesselationControlShader != null) {
                tesselationControlShader.delete ();
            }
            if (tesselationEvaluationShader != null) {
                tesselationEvaluationShader.delete ();
            }
        }

        return this;
    }

    @Override
    public Program validate () {

        GL20.glValidateProgram (glId);
        int status = GL20.glGetProgrami (glId, GL20.GL_VALIDATE_STATUS);

        if (status == GL11.GL_TRUE) {
            validated = true;
        }

        return this;
    }

    @Override
    public Program use () {

        GL20.glUseProgram (glId);

        return this;
    }

    @Override
    public Program delete () {

        GL20.glDeleteProgram (glId);

        return this;
    }

    @Override
    public Program setUniform (String name, float value) {
        GL20.glUniform1f (getUniformLocation (name), value);
        return this;
    }

    @Override
    public Program setUniform (String name, Vector2f value) {
        GL20.glUniform2f (getUniformLocation (name), value.x, value.y);
        return this;
    }

    @Override
    public Program setUniform (String name, float x, float y) {
        GL20.glUniform2f (getUniformLocation (name), x, y);
        return this;
    }

    @Override
    public Program setUniform (String name, Vector3f value) {
        GL20.glUniform3f (getUniformLocation (name), value.x, value.y, value.z);
        return this;
    }

    @Override
    public Program setUniform (String name, float x, float y, float z) {
        GL20.glUniform3f (getUniformLocation (name), x, y, z);
        return this;
    }

    @Override
    public Program setUniform (String name, Vector4f value) {
        GL20.glUniform4f (getUniformLocation (name), value.x, value.y, value.z, value.w);
        return this;
    }

    @Override
    public Program setUniform (String name, float x, float y, float z, float w) {
        GL20.glUniform4f (getUniformLocation (name), x, y, z, w);
        return this;
    }

    @Override
    public Program setUniform (String name, int value) {
        GL20.glUniform1i (getUniformLocation (name), value);
        return this;
    }

    @Override
    public Program setUniform (String name, Vector2i value) {
        GL20.glUniform2i (getUniformLocation (name), value.x, value.y);
        return this;
    }

    @Override
    public Program setUniform (String name, Vector3i value) {
        GL20.glUniform3i (getUniformLocation (name), value.x, value.y, value.z);
        return this;
    }

    @Override
    public Program setUniform (String name, Vector4i value) {
        GL20.glUniform4i (getUniformLocation (name), value.x, value.y, value.z, value.w);
        return this;
    }

    @Override
    public Program setUniform (String name, Matrix2f value) {
        matrixBuffer.clear ();
        value.get (matrixBuffer);
        GL20.glUniformMatrix2fv (getUniformLocation (name), false, matrixBuffer);
        return this;
    }

    @Override
    public Program setUniform (String name, Matrix3x2f value) {
        matrixBuffer.clear ();
        value.get (matrixBuffer);
        GL21.glUniformMatrix3x2fv (getUniformLocation (name), false, matrixBuffer);
        return this;
    }

    @Override
    public Program setUniform (String name, Matrix3f value) {
        matrixBuffer.clear ();
        value.get (matrixBuffer);
        GL20.glUniformMatrix3fv (getUniformLocation (name), false, matrixBuffer);
        return this;
    }

    @Override
    public Program setUniform (String name, Matrix4x3f value) {
        matrixBuffer.clear ();
        value.get (matrixBuffer);
        GL21.glUniformMatrix4x3fv (getUniformLocation (name), false, matrixBuffer);
        return this;
    }

    @Override
    public Program setUniform (String name, Matrix4f value) {
        matrixBuffer.clear ();
        value.get (matrixBuffer);
        GL20.glUniformMatrix4fv (getUniformLocation (name), false, matrixBuffer);
        return this;
    }

    @Override
    public int getUniformLocation (String name) {
        return uniformLocationCache.computeIfAbsent (
                name,
                uniformName -> GL20.glGetUniformLocation (glId, uniformName)
        );
    }

    @Override
    public int getVertexAttributeLocation (String name) {
        return vertexAttributeLocationCache.computeIfAbsent (
                name,
                attribName -> GL20.glGetAttribLocation (glId, attribName)
        );
    }

    @Override
    public boolean isLinked () {
        return linked;
    }

    @Override
    public boolean isValidated () {
        return validated;
    }

    @Override
    public String getInfoLog () {
        return GL20.glGetProgramInfoLog (glId);
    }

}
