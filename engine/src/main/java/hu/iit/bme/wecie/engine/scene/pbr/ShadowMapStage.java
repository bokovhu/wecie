package hu.iit.bme.wecie.engine.scene.pbr;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.CameraFactory;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.opengl.shader.ShaderFactory;
import hu.iit.bme.wecie.engine.opengl.shader.ShaderType;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.GameObject;
import hu.iit.bme.wecie.engine.scene.RenderingContext;
import hu.iit.bme.wecie.engine.scene.World;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

public class ShadowMapStage extends
        BaseRenderPipelineStage<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> {

    private final Program shadowMappingProgram = ProgramFactory.createFromResources (
            "glsl/shadow-mapping/vertex.glsl",
            "glsl/shadow-mapping/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private final Program pointLightShadowMappingProgram = ProgramFactory.createFromResources (
            "glsl/shadow-mapping/point-light/vertex.glsl",
            "glsl/shadow-mapping/point-light/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private final Matrix4f identityMatrix = new Matrix4f ().identity ();
    private final Matrix4f lightViewProjectionMatrix = new Matrix4f ();
    private Camera lightCamera = CameraFactory.create ();
    private Vector3f lightTarget = new Vector3f ();
    private Program currentProgram = shadowMappingProgram;

    @Override
    public void execute (
            World<Environment3D, Model, Light3D> world, Camera camera, PBRPipelineParameters pipelineParams
    ) {

        GL33.glViewport (
                0, 0,
                GameWindow.getInstance ().getShadowMapWidth (),
                GameWindow.getInstance ().getShadowMapHeight ()
        );
        GL33.glEnable (GL33.GL_DEPTH_TEST);

        for (Light3D light : world.getEnvironment ().getLights ()) {

            if (light.isCastsShadow ()) {

                GL33.glClearColor (
                        10000f,
                        10000f,
                        10000f,
                        10000f
                );

                switch (light.getType ()) {
                    case Point:

                        this.currentProgram = pointLightShadowMappingProgram;

                        pointLightShadowMappingProgram.use ();

                        pointLightShadowMappingProgram.setUniform (
                                "u_lightPosition",
                                light.getPosition ()
                        );

                        for (int i = 0; i < 6; i++) {

                            light.getShadowBuffer ().bind ();

                            GL33.glFramebufferTexture2D (
                                    GL33.GL_FRAMEBUFFER,
                                    GL33.GL_COLOR_ATTACHMENT0,
                                    GL33.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i,
                                    light.getShadowBuffer ().colorAttachment (0).getId (),
                                    0
                            );
                            GL33.glDrawBuffer (GL33.GL_COLOR_ATTACHMENT0);
                            GL33.glClear (GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

                            this.lightViewProjectionMatrix.set (light.getMatrices ().get (i));

                            for (GameObject<Model> gameObject : world.getGameObjects ()) {
                                pointLightShadowMappingProgram.setUniform (
                                        "u_model",
                                        gameObject.model ()
                                );
                                gameObject.getDrawableComponent ().draw (
                                        gameObject.model (),
                                        this
                                );
                            }

                        }

                        break;
                    case Directional:
                    case Spot:

                        this.currentProgram = shadowMappingProgram;

                        light.getShadowBuffer ().bind ();
                        GL33.glClear (GL33.GL_DEPTH_BUFFER_BIT);

                        this.lightViewProjectionMatrix.set (light.getMatrices ().get (0));

                        shadowMappingProgram.use ();

                        for (GameObject<Model> gameObject : world.getGameObjects ()) {
                            gameObject.getDrawableComponent ().draw (
                                    gameObject.model (),
                                    this
                            );
                        }

                        break;
                }

            }

        }

        GL33.glBindFramebuffer (
                GL33.GL_FRAMEBUFFER,
                0
        );

    }

    @Override
    public Program getProgram () {
        return currentProgram;
    }

    @Override
    public RenderingContext transformModel (Matrix4f model) {
        this.mvp.identity ()
                .set (lightViewProjectionMatrix)
                .mul (model);
        return this;
    }

}
