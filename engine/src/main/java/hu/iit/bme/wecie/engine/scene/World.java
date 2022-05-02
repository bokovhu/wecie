package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

import java.util.List;

public interface World<E extends Environment<L>, D extends DrawableComponent, L extends Light> {

    List<GameObject<D>> getGameObjects ();

    List<GameObject<D>> findGameObjects (int tag);

    World<E, D, L> addGameObject (GameObject<D> gameObject);

    World<E, D, L> removeGameObject (GameObject<D> gameObject);

    World<E, D, L> removeGameObject (int id);

    World<E, D, L> removeTaggedGameObjects (int tag);

    World<E, D, L> removeAllGameObjects ();

    GameObject<D> newGameObject (D drawableComponent);

    GameObject<D> findGameObject (int id);

    E getEnvironment ();

}
