package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.opengl.shader.Program;

public interface Light {

    int getId ();

    void setProgramUniforms (Program program, String objectPrefix);

}
