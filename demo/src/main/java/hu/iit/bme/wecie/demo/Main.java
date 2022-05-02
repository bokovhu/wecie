package hu.iit.bme.wecie.demo;

import hu.iit.bme.wecie.engine.GameLaunchConfig;
import hu.iit.bme.wecie.engine.GameLauncher;
import hu.iit.bme.wecie.engine.SceneBasedGame;

public class Main {

    public static void main (String[] args) {

        GameLaunchConfig config = new GameLaunchConfig ();
        config.setWindowWidth (1270);
        config.setWindowHeight (720);
        config.setFullScreen(false);
        GameLauncher.start (
                new SceneBasedGame (new DemoScene ()),
                config
        );

    }

}
