package hu.iit.bme.wecie.engine.drawing.three;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.iit.bme.wecie.engine.asset.ModelDescriptor;
import hu.iit.bme.wecie.engine.opengl.texture.TextureFactory;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public final class ModelFactory {

    private static final Logger log = LoggerFactory.getLogger (ModelFactory.class);

    private ModelFactory () {
        throw new UnsupportedOperationException ();
    }

    public static Model create (List<Mesh> meshes) {

        ModelImpl model = new ModelImpl ();
        model.getMeshes ().addAll (meshes);
        return model;

    }

}
