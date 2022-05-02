package hu.iit.bme.wecie.engine.input;

import org.lwjgl.glfw.GLFWMonitorCallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLFWMonitorCallbackImpl implements GLFWMonitorCallbackI {

    private Logger log = LoggerFactory.getLogger (getClass ().getName ());

    @Override
    public void invoke (long monitor, int event) {

        log.info ("EVENT [ monitor = {}, event = {} ]", monitor, event);

    }

}
