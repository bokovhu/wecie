package hu.iit.bme.wecie.engine.input;

import hu.iit.bme.wecie.engine.GameLauncher;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLFWKeyCallbackImpl implements GLFWKeyCallbackI {

    private Logger log = LoggerFactory.getLogger (getClass ().getName ());

    @Override
    public void invoke (long window, int key, int scancode, int action, int mods) {

        log.info (
                "EVENT [ window = {}, key = {}, scancode = {}, action = {}, mods = {} ]",
                window,
                key,
                scancode,
                action,
                mods
        );

        switch (action) {
            case GLFW.GLFW_PRESS:
                GameLauncher.getInputHandler ().onKeyPressed (key, mods);
                break;
            case GLFW.GLFW_RELEASE:
                GameLauncher.getInputHandler ().onKeyReleased (key, mods);
                break;
        }

    }

}
