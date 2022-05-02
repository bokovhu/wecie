package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class BaseWorld<E extends Environment<L>, D extends DrawableComponent, L extends Light> implements
        World<E, D, L> {

    private static AtomicInteger gameObjectIdGenerator = new AtomicInteger (1);
    private final List<GameObject<D>> gameObjects = new ArrayList<> ();
    private final E environment;
    private GameObjectInstantiationStrategy<D> gameObjectInstantiationStrategy = (id, d) -> new BaseGameObject<> (
            getGameObjectTypeId (),
            id,
            d
    );

    public BaseWorld (E environment) {
        this.environment = environment;
    }

    protected abstract int getGameObjectTypeId ();

    @Override
    public List<GameObject<D>> getGameObjects () {
        return Collections.unmodifiableList (gameObjects);
    }

    @Override
    public List<GameObject<D>> findGameObjects (int tag) {
        return gameObjects.stream ()
                .filter (go -> go.getTags ().contains (tag))
                .collect (Collectors.toList ());
    }

    @Override
    public World<E, D, L> addGameObject (GameObject<D> gameObject) {
        gameObjects.add (gameObject);
        return this;
    }

    @Override
    public World<E, D, L> removeGameObject (GameObject<D> gameObject) {
        gameObjects.remove (gameObject);
        return this;
    }

    @Override
    public World<E, D, L> removeGameObject (int id) {
        gameObjects.removeIf (go -> go.getId () == id);
        return this;
    }

    @Override
    public World<E, D, L> removeTaggedGameObjects (int tag) {
        gameObjects.removeIf (go -> go.getTags ().contains (tag));
        return null;
    }

    @Override
    public GameObject<D> newGameObject (D drawableComponent) {
        return gameObjectInstantiationStrategy.instantiate (
                gameObjectIdGenerator.getAndIncrement (),
                drawableComponent
        );
    }

    @Override
    public GameObject<D> findGameObject (int id) {
        return gameObjects.stream ()
                .filter (go -> go.getId () == id)
                .findFirst ().orElse (null);
    }

    @Override
    public E getEnvironment () {
        return environment;
    }

    public interface GameObjectInstantiationStrategy<D extends DrawableComponent> {

        GameObject<D> instantiate (int gameObjectId, D drawableComponent);

    }

    @Override
    public World<E, D, L> removeAllGameObjects () {
        this.gameObjects.clear ();
        return this;
    }

}
