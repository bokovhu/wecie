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
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.World;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

public class CompositionStage extends
        BaseRenderPipelineStage<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> {

    private Program compositionProgram = ProgramFactory.createFromResources (
            "glsl/pbr/composition-pass/vertex.glsl",
            "glsl/pbr/composition-pass/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private Camera unitCamera = CameraFactory.create ();
    private Renderable unitCube = RenderableFactory.cube (new Vector3f (2f));
    private Vector2f resolution = new Vector2f ();
    private Vector3f unitCameraPosition = new Vector3f (0f);
    private Vector3f unitCameraTarget = new Vector3f (0f);
    private Vector3f unitCameraUp = new Vector3f (0f, 1f, 0f);

    @Override
    public void execute (
            World<Environment3D, Model, Light3D> world, Camera camera, PBRPipelineParameters pipelineParams
    ) {

        bindOutput ();
        clearOutput (GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

        compositionProgram.use ();

        input.colorAttachment (0).bind (TextureTarget.texture2D, 0);
        input.colorAttachment (1).bind (TextureTarget.texture2D, 1);
        compositionProgram.setUniform ("u_lightingResult", 0);
        compositionProgram.setUniform ("u_bloomMap", 1);
        compositionProgram.setUniform (
                "u_resolution",
                resolution.set (
                        GameWindow.getInstance ().getWindowWidth (),
                        GameWindow.getInstance ().getWindowHeight ()
                )
        );
        if (pipelineParams.getSkymap () != null) {
            pipelineParams.getSkymap ().bind (TextureTarget.cubemap, 2);
            compositionProgram.setUniform ("u_skymap", 2);
            compositionProgram.setUniform ("u_useSkymap", 1);
        } else {
            compositionProgram.setUniform ("u_useSkymap", 0);
        }

        this.camera = unitCamera.set (camera)
                .lookAt (
                        unitCameraPosition,
                        camera.front (),
                        camera.up ()
                );
        compositionProgram.setUniform ("u_viewProjection", unitCamera.viewProjection ());

        unitCube.draw ();

    }

    @Override
    public Program getProgram () {
        return compositionProgram;
    }

}
