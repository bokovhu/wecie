package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.asset.ModelDescriptor;
import hu.iit.bme.wecie.engine.opengl.texture.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModelLoadingContext {

    public final Map<String, Material> materialMap = new HashMap<> ();
    public final List<Material> materials = new ArrayList<> ();
    public final List<Mesh> meshes = new ArrayList<> ();
    public final List<Texture> textures = new ArrayList<> ();
    public final Map<String, Texture> textureMap = new HashMap<> ();
    public final String resourceBaseName;
    public final ClassLoader resourceClassLoader;
    public ModelDescriptor modelDescriptor = null;

    public ModelLoadingContext (String resourceBaseName, ClassLoader resourceClassLoader) {
        this.resourceBaseName = resourceBaseName;
        this.resourceClassLoader = resourceClassLoader;
    }

}
