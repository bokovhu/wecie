package hu.iit.bme.wecie.engine.scene;

import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import hu.iit.bme.wecie.engine.scene.GameObject;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseGameObject<C extends DrawableComponent> implements GameObject<C> {

    private final int typeId;
    private final int id;
    private final List<Integer> tags = new ArrayList<> ();
    private final Matrix4f modelMatrix = new Matrix4f ().identity ();
    private final Vector3f position = new Vector3f ();
    private final Vector3f scale = new Vector3f (1.0f, 1.0f, 1.0f);
    private final Quaternionf rotation = new Quaternionf ();
    private final C drawableComponent;

    public BaseGameObject (int typeId, int id, C drawableComponent) {
        this.typeId = typeId;
        this.id = id;
        this.drawableComponent = drawableComponent;
    }

    private void update () {

        this.modelMatrix.identity ()
                .scale (scale)
                .translate (position)
                .rotate (rotation);

    }

    @Override
    public int getTypeId () {
        return typeId;
    }

    @Override
    public int getId () {
        return id;
    }

    @Override
    public List<Integer> getTags () {
        return Collections.unmodifiableList (tags);
    }

    @Override
    public GameObject<C> tag (int tag) {
        this.tags.add (tag);
        return null;
    }

    @Override
    public GameObject<C> untag (int tag) {
        this.tags.removeIf (t -> t == tag);
        return this;
    }

    @Override
    public Matrix4f model () {
        return modelMatrix;
    }

    @Override
    public GameObject transform (Matrix4f transformation) {
        this.modelMatrix.set (transformation);
        return this;
    }

    @Override
    public GameObject translate (Vector3f position) {
        this.position.set (position);
        update ();
        return this;
    }

    @Override
    public GameObject scale (Vector3f scale) {
        this.scale.set (scale);
        update ();
        return this;
    }

    @Override
    public GameObject rotate (Quaternionf rotation) {
        this.rotation.set (rotation);
        update ();
        return this;
    }

    @Override
    public C getDrawableComponent () {
        return drawableComponent;
    }

}
