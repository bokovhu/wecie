package hu.iit.bme.wecie.engine.scene.pbr;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.CameraFactory;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import hu.iit.bme.wecie.engine.scene.GameObject;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.World;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import hu.iit.bme.wecie.engine.scene.world.World3D;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class GBufferStage extends
        BaseRenderPipelineStage<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> {

    private final Program program = ProgramFactory.createFromResources (
            "glsl/pbr/gbuffer-pass/vertex.glsl",
            "glsl/pbr/gbuffer-pass/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private final Renderable fullScreenQuad = RenderableFactory.quad (new Vector2f (2f, 2f));
    private final Renderable skymapCube = RenderableFactory.cube (new Vector3f (2f));
    private final Program skymapProgram = ProgramFactory.createFromResources (
            "glsl/pbr/gbuffer-pass/skymap/vertex.glsl",
            "glsl/pbr/gbuffer-pass/skymap/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private final Camera skymapCamera = CameraFactory.create ()
            .perspective (
                    3.141592653f / 3.0f,
                    (float) GameWindow.getInstance ().getWindowWidth ()
                    / (float) GameWindow.getInstance ().getWindowHeight (),
                    0.01f,
                    100.0f
            );

    @Override
    public void execute (
            World<Environment3D, Model, Light3D> world, Camera camera, PBRPipelineParameters pipelineParams
    ) {

        GL33.glClearColor (0f, 0f, 0f, 0f);

        GL33.glViewport (
                0, 0,
                GameWindow.getInstance ().getWindowWidth (),
                GameWindow.getInstance ().getWindowHeight ()
        );

        bindOutput ();
        clearOutput (GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);

        this.camera = camera;

        program.use ();

        GL33.glShadeModel (GL33.GL_SMOOTH);
        GL30.glEnable (GL30.GL_DEPTH_TEST);

        camera.setProgramUniforms (program, "u_camera");

        for (GameObject<Model> gameObject : world.getGameObjects ()) {

            program.setUniform ("u_model", gameObject.model ());
            gameObject.getDrawableComponent ().draw (gameObject.model (), this);

        }

    }

    @Override
    public Program getProgram () {
        return program;
    }

}
