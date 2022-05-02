package hu.iit.bme.wecie.engine.opengl.shader;

import hu.iit.bme.wecie.engine.opengl.OpenGLEnumeration;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;

public final class ShaderType extends OpenGLEnumeration {

    public static final ShaderType vertex = new ShaderType (GL20.GL_VERTEX_SHADER);
    public static final ShaderType fragment = new ShaderType (GL20.GL_FRAGMENT_SHADER);
    public static final ShaderType geometry = new ShaderType (GL32.GL_GEOMETRY_SHADER);
    public static final ShaderType tesselationControl = new ShaderType (GL40.GL_TESS_CONTROL_SHADER);
    public static final ShaderType tesselationEvaluation = new ShaderType (GL40.GL_TESS_EVALUATION_SHADER);

    protected ShaderType (int glValue) {
        super (glValue);
    }

}
