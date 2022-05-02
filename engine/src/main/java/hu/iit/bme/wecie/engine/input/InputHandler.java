package hu.iit.bme.wecie.engine.input;

public interface InputHandler {

    default void onKeyPressed (int key, int mods) {
    }

    default void onKeyReleased (int key, int mods) {
    }

    default void onMousePressed (int button, int mods, float x, float y) {
    }

    default void onMouseReleased (int button, int mods, float x, float y) {
    }

    default void onMouseMoved (float x, float y) {

    }

}
