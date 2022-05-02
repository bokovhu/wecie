package hu.iit.bme.wecie.engine.scene.pbr;

import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.World;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import hu.iit.bme.wecie.engine.scene.world.World3D;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class LightingStage extends
        BaseRenderPipelineStage<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> {

    private static final int LIGHT_BATCH_SIZE = 8;
    private final Program program = ProgramFactory.createFromResources (
            "glsl/pbr/lighting-pass/vertex.glsl",
            "glsl/pbr/lighting-pass/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private final Renderable fullScreenQuad = RenderableFactory.quad (new Vector2f (2.0f, 2.0f));

    @Override
    public Program getProgram () {
        return program;
    }

    @Override
    public void execute (
            World<Environment3D, Model, Light3D> world, Camera camera, PBRPipelineParameters pipelineParams
    ) {

        this.camera = camera;

        bindOutput ();
        GL30.glClearColor (0f, 0f, 0f, 0f);
        clearOutput (GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        program.use ();

        input.colorAttachment (0)
                .bind (TextureTarget.texture2D, 0);
        input.colorAttachment (1)
                .bind (TextureTarget.texture2D, 1);
        input.colorAttachment (2)
                .bind (TextureTarget.texture2D, 2);
        input.colorAttachment (3)
                .bind (TextureTarget.texture2D, 3);
        program.setUniform ("u_gBuffer.worldPositionMap", 0);
        program.setUniform ("u_gBuffer.normalAndRoughnessMap", 1);
        program.setUniform ("u_gBuffer.albedoAndMetallicMap", 2);
        program.setUniform ("u_gBuffer.ambientOcclusionMap", 3);
        program.setUniform ("u_bloom.threshold", pipelineParams.getBloomThreshold ());

        this.camera.setProgramUniforms (program, "u_camera");

        GL30.glEnable (GL30.GL_BLEND);
        GL30.glBlendFunc (
                GL30.GL_ONE,
                GL30.GL_ONE
        );

        List<Light3D> lights = world.getEnvironment ().getLights ();

        for (int i = 0; i < lights.size (); i += LIGHT_BATCH_SIZE) {

            int batchLoopTo = Math.min (lights.size (), i + LIGHT_BATCH_SIZE);
            int shadowMapTextureUnit = 4;

            for (int j = 0; j < 8; j++) {
                program.setUniform ("u_lightShadowMaps[" + j + "]", shadowMapTextureUnit);
                program.setUniform ("u_lightShadowCubes[" + j + "]", shadowMapTextureUnit);
            }

            for (int j = i; j < batchLoopTo; j++) {

                lights.get (j).setProgramUniforms (program, "u_lights[" + (j - i) + "]");
                if (lights.get (j).isCastsShadow ()) {
                    lights.get (j).setShadowUniforms (
                            j - i,
                            program,
                            "u_lightShadowMaps",
                            "u_lightShadowCubes",
                            shadowMapTextureUnit++
                    );
                }

            }
            for (int j = batchLoopTo - i; j < 8; j++) {
                program.setUniform ("u_lights[" + j + "].enabled", 0);
            }

            fullScreenQuad.draw ();

        }

        GL30.glDisable (GL30.GL_BLEND);

    }

}
