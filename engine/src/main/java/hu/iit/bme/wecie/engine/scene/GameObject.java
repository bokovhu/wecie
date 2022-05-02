package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public interface GameObject<C extends DrawableComponent> {

    int SPRITE2D = 0;
    int MODEL3D = 1;
    int MODELANIM3D = 2;

    int getTypeId ();

    int getId ();

    List<Integer> getTags ();

    GameObject<C> tag (int tag);

    GameObject<C> untag (int tag);

    Matrix4f model ();

    GameObject transform (Matrix4f transformation);

    GameObject translate (Vector3f position);

    GameObject scale (Vector3f scale);

    GameObject rotate (Quaternionf rotation);

    C getDrawableComponent ();

}
