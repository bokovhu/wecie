package hu.iit.bme.wecie.engine.opengl.shader;

import org.joml.*;

public interface Program {

    Shader getVertexShader ();

    Shader getFragmentShader ();

    Shader getGeometryShader ();

    Shader getTesselationControlShader ();

    Shader getTesselationEvaluationShader ();

    Program generate ();

    Program attach (Shader shader);

    Program bindDataLocation (int colorNumber, String name);

    Program link ();

    Program validate ();

    Program use ();

    Program delete ();

    Program setUniform (String name, float value);

    Program setUniform (String name, Vector2f value);

    Program setUniform (String name, float x, float y);

    Program setUniform (String name, Vector3f value);

    Program setUniform (String name, float x, float y, float z);

    Program setUniform (String name, Vector4f value);

    Program setUniform (String name, float x, float y, float z, float w);

    Program setUniform (String name, int value);

    Program setUniform (String name, Vector2i value);

    Program setUniform (String name, Vector3i value);

    Program setUniform (String name, Vector4i value);

    Program setUniform (String name, Matrix2f value);

    Program setUniform (String name, Matrix3x2f value);

    Program setUniform (String name, Matrix3f value);

    Program setUniform (String name, Matrix4x3f value);

    Program setUniform (String name, Matrix4f value);

    int getUniformLocation (String name);

    int getVertexAttributeLocation (String name);

    boolean isLinked ();

    boolean isValidated ();

    String getInfoLog ();

}
