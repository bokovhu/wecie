package hu.iit.bme.wecie.engine.input;

import hu.iit.bme.wecie.engine.GameLauncher;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public class GLFWCursorPosCallbackImpl implements GLFWCursorPosCallbackI {

    @Override
    public void invoke (long window, double xpos, double ypos) {

        GameLauncher.getInputHandler ().onMouseMoved ((float) xpos, (float) ypos);

    }

}
