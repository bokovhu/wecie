package hu.iit.bme.wecie.engine.drawing.three;

import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import hu.iit.bme.wecie.engine.scene.RenderingContext;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

class ModelImpl implements Model {

    private final List<Mesh> meshes = new ArrayList<> ();

    @Override
    public List<Mesh> getMeshes () {
        return meshes;
    }

    @Override
    public void draw (Matrix4f transformation, RenderingContext context) {

        context.transformModel (transformation);
        context.getProgram ()
                .setUniform ("u_MVP", context.modelViewProjection ());

        for (Mesh mesh : meshes) {

            mesh.getMaterial ().getAlbedoMap ().bind (TextureTarget.texture2D, 0);
            mesh.getMaterial ().getMetallicMap ().bind (TextureTarget.texture2D, 1);
            mesh.getMaterial ().getRoughnessMap ().bind (TextureTarget.texture2D, 2);
            if (mesh.getMaterial ().getNormalMap () != null) {
                mesh.getMaterial ().getNormalMap ().bind (TextureTarget.texture2D, 3);
            }
            if (mesh.getMaterial ().getAmbientOcclusionMap () != null) {
                mesh.getMaterial ().getAmbientOcclusionMap ().bind (TextureTarget.texture2D, 4);
            }
            context.getProgram ()
                    .setUniform ("u_material.albedoMap", 0)
                    .setUniform ("u_material.metallicMap", 1)
                    .setUniform ("u_material.roughnessMap", 2);
            if (mesh.getMaterial ().getNormalMap () != null) {
                context.getProgram ()
                        .setUniform ("u_material.normalMap", 3)
                        .setUniform ("u_material.hasNormalMap", 1);
            } else {
                context.getProgram ()
                        .setUniform ("u_material.hasNormalMap", 0);
            }
            if (mesh.getMaterial ().getAmbientOcclusionMap () != null) {
                context.getProgram ()
                        .setUniform ("u_material.aoMap", 4)
                        .setUniform ("u_material.hasAoMap", 1);
            } else {
                context.getProgram ()
                        .setUniform ("u_material.hasAoMap", 0);
            }

            mesh.getRenderable ()
                    .draw ();

        }

    }

}
