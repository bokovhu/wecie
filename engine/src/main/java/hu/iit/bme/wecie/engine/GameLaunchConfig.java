package hu.iit.bme.wecie.engine;

public class GameLaunchConfig {

    private int windowWidth = 800;
    private int windowHeight = 600;
    private int targetUps = 60;
    private boolean fullScreen = false;
    private String windowTitle = "We Call It Engine";
    private int shadowMapWidth = 1024;
    private int shadowMapHeight = 1024;
    private boolean grabCursor = true;

    public int getWindowWidth () {
        return windowWidth;
    }

    public void setWindowWidth (int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight () {
        return windowHeight;
    }

    public void setWindowHeight (int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public int getTargetUps () {
        return targetUps;
    }

    public void setTargetUps (int targetUps) {
        this.targetUps = targetUps;
    }

    public String getWindowTitle () {
        return windowTitle;
    }

    public void setWindowTitle (String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public boolean isFullScreen () {
        return fullScreen;
    }

    public void setFullScreen (boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public int getShadowMapWidth () {
        return shadowMapWidth;
    }

    public void setShadowMapWidth (int shadowMapWidth) {
        this.shadowMapWidth = shadowMapWidth;
    }

    public int getShadowMapHeight () {
        return shadowMapHeight;
    }

    public void setShadowMapHeight (int shadowMapHeight) {
        this.shadowMapHeight = shadowMapHeight;
    }

    public boolean isGrabCursor () {
        return grabCursor;
    }

    public void setGrabCursor (boolean grabCursor) {
        this.grabCursor = grabCursor;
    }

}
