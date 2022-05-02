package hu.iit.bme.wecie.engine;

public interface Game {

    void onCreate ();

    void onUpdate (float delta);

    void onRender (float delta);

    void onExit ();

}
