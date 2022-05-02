package hu.iit.bme.wecie.engine;

import hu.iit.bme.wecie.engine.input.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameWindow {

    private static final Logger log = LoggerFactory.getLogger (GameWindow.class);

    private static final GameWindow INSTANCE = new GameWindow ();

    private long glfwWindow;

    private int windowWidth;
    private int windowHeight;
    private int shadowMapWidth = 1024;
    private int shadowMapHeight = 1024;

    public static GameWindow getInstance () {
        return INSTANCE;
    }

    public int getWindowWidth () {
        return windowWidth;
    }

    public int getWindowHeight () {
        return windowHeight;
    }

    public int getShadowMapWidth () {
        return shadowMapWidth;
    }

    public int getShadowMapHeight () {
        return shadowMapHeight;
    }

    public long getGlfwWindow () {
        return glfwWindow;
    }

    void init (GameLaunchConfig config) {

        GLFWErrorCallback.createPrint (System.err).set ();

        if (!GLFW.glfwInit ()) {
            throw new IllegalStateException ("Could not initialize GLFW!");
        }

        GLFW.glfwDefaultWindowHints ();
        GLFW.glfwWindowHint (GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint (GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        /* GLFW.glfwWindowHint (GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
        GLFW.glfwWindowHint (GLFW.GLFW_CONTEXT_VERSION_MINOR, 5);
        GLFW.glfwWindowHint (GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE); */

        glfwWindow = GLFW.glfwCreateWindow (
                config.getWindowWidth (),
                config.getWindowHeight (),
                config.getWindowTitle (),
                config.isFullScreen () ? GLFW.glfwGetPrimaryMonitor () : MemoryUtil.NULL,
                MemoryUtil.NULL
        );
        this.windowWidth = config.getWindowWidth ();
        this.windowHeight = config.getWindowHeight ();
        this.shadowMapWidth = config.getShadowMapWidth ();
        this.shadowMapHeight = config.getShadowMapHeight ();

        if (glfwWindow == MemoryUtil.NULL) {
            throw new IllegalStateException ("Could not create GLFW window!");
        }

        GLFWKeyCallback.create (new GLFWKeyCallbackImpl ()).set (glfwWindow);
        GLFWMouseButtonCallback.create (new GLFWMouseButtonCallbackImpl ()).set (glfwWindow);
        GLFWMonitorCallback.create (new GLFWMonitorCallbackImpl ()).set ();
        GLFWCursorPosCallback.create (new GLFWCursorPosCallbackImpl ()).set (glfwWindow);

        if (config.isGrabCursor ()) {
            GLFW.glfwSetInputMode (
                    glfwWindow,
                    GLFW.GLFW_CURSOR,
                    GLFW.GLFW_CURSOR_DISABLED
            );
        }

        GLFW.glfwMakeContextCurrent (glfwWindow);
        GLFW.glfwSwapInterval (1);

        GLFW.glfwShowWindow (glfwWindow);

        GL.createCapabilities ();

        GL11.glViewport (
                0, 0,
                windowWidth, windowHeight
        );

        log.info ("GLFW Version: {}", GLFW.glfwGetVersionString ());
        log.info ("OpenGL vendor: {}", GL11.glGetString (GL11.GL_VENDOR));
        log.info ("OpenGL version: {}", GL11.glGetString (GL11.GL_VERSION));
        log.info ("GLSL version: {}", GL11.glGetString (GL20.GL_SHADING_LANGUAGE_VERSION));
        log.info ("OpenGL renderer: {}", GL11.glGetString (GL11.GL_RENDERER));

    }

    void onFrameRendered () {

        GLFW.glfwSwapBuffers (glfwWindow);
        GLFW.glfwPollEvents ();

    }

    boolean shouldQuit () {
        return GLFW.glfwWindowShouldClose (glfwWindow);
    }

}
