package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import org.joml.Matrix4f;

public interface RenderingContext {

    Camera getCamera ();

    Matrix4f modelViewProjection ();

    Program getProgram ();

    RenderingContext transformModel (Matrix4f model);

}
