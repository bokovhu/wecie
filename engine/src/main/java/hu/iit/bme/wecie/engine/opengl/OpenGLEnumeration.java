package hu.iit.bme.wecie.engine.opengl;

public class OpenGLEnumeration {

    private final int glValue;

    protected OpenGLEnumeration (int glValue) {
        this.glValue = glValue;
    }

    public int getGlValue () {
        return glValue;
    }

}
