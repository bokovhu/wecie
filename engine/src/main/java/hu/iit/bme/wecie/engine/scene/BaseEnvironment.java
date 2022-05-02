package hu.iit.bme.wecie.engine.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseEnvironment<L extends Light> implements Environment<L> {

    private static final AtomicInteger idGenerator = new AtomicInteger (1);
    private final List<L> lights = new ArrayList<> ();
    private final LightInstantiationStrategy<L> lightInstantiationStrategy;

    protected BaseEnvironment (LightInstantiationStrategy<L> lightInstantiationStrategy) {
        this.lightInstantiationStrategy = lightInstantiationStrategy;
    }

    @Override
    public List<L> getLights () {
        return Collections.unmodifiableList (lights);
    }

    @Override
    public Environment<L> addLight (L light) {
        this.lights.add (light);
        return this;
    }

    @Override
    public Environment<L> removeLight (L light) {
        this.lights.remove (light);
        return this;
    }

    @Override
    public Environment<L> removeLight (int id) {
        this.lights.removeIf (l -> l.getId () == id);
        return this;
    }

    @Override
    public L getLight (int id) {
        return this.lights.stream ()
                .filter (l -> l.getId () == id)
                .findFirst ().orElse (null);
    }

    @Override
    public L newLight () {
        return lightInstantiationStrategy.createLight (idGenerator.getAndIncrement ());
    }

    public interface LightInstantiationStrategy<L extends Light> {

        L createLight (int lightId);

    }

}
