package hu.iit.bme.wecie.engine.drawing;

import hu.iit.bme.wecie.engine.opengl.shader.Program;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface Camera {

    Matrix4f projection ();

    Matrix4f view ();

    Matrix4f viewProjection ();

    Matrix4f invProjection ();

    Matrix4f invView ();

    Camera lookAt (Vector3f eye, Vector3f target, Vector3f up);

    Camera perspective (float fovy, float aspect, float zNear, float zFar);

    Camera orthographic (
            float topLeftX,
            float topLeftY,
            float bottomRightX,
            float bottomRightY,
            float zNear,
            float zFar
    );

    Vector3f up ();

    Vector3f right ();

    Vector3f front ();

    Vector3f position ();

    Vector3f scale ();

    Camera setProgramUniforms (Program program, String objectNamePrefix);

    Camera set (Camera other);

}
