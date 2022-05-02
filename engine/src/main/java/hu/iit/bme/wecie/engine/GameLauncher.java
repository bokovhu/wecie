package hu.iit.bme.wecie.engine;

import hu.iit.bme.wecie.engine.input.InputHandler;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameLauncher {

    private static final Logger log = LoggerFactory.getLogger (GameLauncher.class);
    private static final GameLauncher INSTANCE = new GameLauncher ();

    private static final int TARGET_UPS = 60;

    private boolean gameShouldExit = false;
    private Game gameInstance = null;
    private GameLaunchConfig config = null;
    private InputHandler inputHandler = new DefaultInputHandlerImpl ();

    public static void start (Game game, GameLaunchConfig config) {

        if (game == null) {
            throw new IllegalArgumentException ("Game instance cannot be null!");
        }

        if (config == null) {
            throw new IllegalArgumentException ("Game launch configuration cannot be null!");
        }

        INSTANCE.gameInstance = game;
        INSTANCE.config = config;
        INSTANCE.doStartGame ();

    }

    public static InputHandler getInputHandler () {
        return INSTANCE.inputHandler;
    }

    public static void setInputHandler (InputHandler inputHandler) {
        INSTANCE.inputHandler = inputHandler;
    }

    public static void exitGame () {
        INSTANCE.gameShouldExit = true;
    }

    private void doStartGame () {

        GameWindow.getInstance ().init (this.config);

        gameInstance.onCreate ();

        long lastFrame = System.currentTimeMillis ();
        float deltaAccumulator = 0f;
        final float updateTimestep = 1.0f / (float) TARGET_UPS;

        int fps = 0;
        float fpsTimer = 0.0f;

        while (!GameWindow.getInstance ().shouldQuit () && !gameShouldExit) {

            long now = System.currentTimeMillis ();
            long deltaMs = now - lastFrame;
            lastFrame = now;
            float delta = deltaMs / 1000.0f;
            deltaAccumulator += delta;

            fpsTimer += delta;
            if (fpsTimer >= 1f) {
                fpsTimer = 0f;
                log.info ("FPS: {}", fps);
                fps = 0;
            }

            if (this.config.getTargetUps () > 0) {
                while (deltaAccumulator >= updateTimestep) {
                    gameInstance.onUpdate (updateTimestep);
                    deltaAccumulator -= updateTimestep;
                }
            } else {
                gameInstance.onUpdate (delta);
            }

            gameInstance.onRender (delta);

            GameWindow.getInstance ().onFrameRendered ();

            ++fps;

        }

        gameInstance.onExit ();

    }

    private static final class DefaultInputHandlerImpl implements InputHandler {

    }

}
