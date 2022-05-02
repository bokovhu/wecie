package hu.iit.bme.wecie.engine.scene;

import java.util.List;

public interface Environment<L extends Light> {

    List<L> getLights ();

    Environment<L> addLight (L light);

    Environment<L> removeLight (L light);

    Environment<L> removeLight (int id);

    L getLight (int id);

    L newLight ();

}
