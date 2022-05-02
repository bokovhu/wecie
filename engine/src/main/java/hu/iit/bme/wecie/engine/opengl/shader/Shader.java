package hu.iit.bme.wecie.engine.opengl.shader;

public interface Shader {

    int getId ();

    ShaderType getType ();

    String getSource ();

    Shader generate ();

    Shader compile ();

    Shader delete ();

    String getInfoLog ();

    boolean isCompiled ();

    boolean isDeleted ();

}
