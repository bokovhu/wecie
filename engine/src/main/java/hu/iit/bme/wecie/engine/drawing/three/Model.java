package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.drawing.DrawableComponent;

import java.util.List;

public interface Model extends DrawableComponent {

    List<Mesh> getMeshes ();

}
