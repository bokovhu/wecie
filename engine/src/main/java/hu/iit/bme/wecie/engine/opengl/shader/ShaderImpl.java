package hu.iit.bme.wecie.engine.opengl.shader;

import org.lwjgl.opengl.GL20;

class ShaderImpl implements Shader {

    private final ShaderType shaderType;
    private final String source;
    private int glId;
    private boolean compiled = false;
    private boolean deleted = false;

    ShaderImpl (ShaderType shaderType, String source) {
        this.shaderType = shaderType;
        this.source = source;
    }

    @Override
    public int getId () {
        return glId;
    }

    @Override
    public ShaderType getType () {
        return shaderType;
    }

    @Override
    public String getSource () {
        return source;
    }

    @Override
    public Shader generate () {

        this.glId = GL20.glCreateShader (shaderType.getGlValue ());

        return this;
    }

    @Override
    public Shader compile () {

        GL20.glShaderSource (glId, source);
        GL20.glCompileShader (glId);
        int status = GL20.glGetShaderi (glId, GL20.GL_COMPILE_STATUS);
        if (status == GL20.GL_TRUE) {
            compiled = true;
        }

        return this;
    }

    @Override
    public Shader delete () {

        if (!deleted) {
            GL20.glDeleteShader (glId);
            deleted = true;
        }

        return this;
    }

    @Override
    public String getInfoLog () {

        return GL20.glGetShaderInfoLog (glId);

    }

    @Override
    public boolean isCompiled () {
        return compiled;
    }

    @Override
    public boolean isDeleted () {
        return deleted;
    }

}
