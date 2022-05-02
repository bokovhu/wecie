package hu.iit.bme.wecie.engine.input;

import hu.iit.bme.wecie.engine.GameLauncher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLFWMouseButtonCallbackImpl implements GLFWMouseButtonCallbackI {

    private final double[] mouseXPosBuffer = new double[1];
    private final double[] mouseYPosBuffer = new double[1];
    private Logger log = LoggerFactory.getLogger (getClass ().getName ());

    @Override
    public void invoke (long window, int button, int action, int mods) {

        GLFW.glfwGetCursorPos (
                window,
                mouseXPosBuffer,
                mouseYPosBuffer
        );

        log.info (
                "EVENT [ window = {}, button = {}, action = {}, mods = {}, x = {}, y = {} ]",
                window,
                button,
                action,
                mods,
                mouseXPosBuffer[0],
                mouseYPosBuffer[0]
        );

        switch (action) {
            case GLFW.GLFW_PRESS:
                GameLauncher.getInputHandler ().onMousePressed (
                        button,
                        mods,
                        (float) mouseXPosBuffer[0],
                        (float) mouseYPosBuffer[0]
                );
                break;
            case GLFW.GLFW_RELEASE:
                GameLauncher.getInputHandler ().onMouseReleased (
                        button,
                        mods,
                        (float) mouseXPosBuffer[0],
                        (float) mouseYPosBuffer[0]
                );
                break;
        }

    }

}
