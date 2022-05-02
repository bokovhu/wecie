package hu.iit.bme.wecie.engine.scene.pbr;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBufferFactory;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.opengl.texture.*;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.World;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class BloomStage extends
        BaseRenderPipelineStage<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> {

    private Program bloomProgram = ProgramFactory.createFromResources (
            "glsl/bloom/vertex.glsl",
            "glsl/bloom/fragment.glsl",
            getClass ().getClassLoader ()
    );
    private Renderable fullScreenQuad = RenderableFactory.quad (new Vector2f (2f));
    private FrameBuffer[] pingpongFBO = new FrameBuffer[] {
            FrameBufferFactory.create ()
                    .attachColor (
                    0,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0f)
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.nearest,
                            TextureMagFilter.nearest,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            ),
            FrameBufferFactory.create ()
                    .attachColor (
                    0,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0f)
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.nearest,
                            TextureMagFilter.nearest,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            )
    };

    @Override
    public void execute (
            World<Environment3D, Model, Light3D> world, Camera camera, PBRPipelineParameters pipelineParams
    ) {

        bloomProgram.use ();

        pingpongFBO[0].bind ();
        GL33.glClear (GL33.GL_COLOR_BUFFER_BIT);
        pingpongFBO[1].bind ();
        GL33.glClear (GL33.GL_COLOR_BUFFER_BIT);

        FrameBuffer lastBoundFBO = null;

        for (int i = 0; i < pipelineParams.getBloomIterations (); i++) {

            boolean horizontal = i % 2 == 0;
            pingpongFBO [horizontal ? 0 : 1].bind ();
            lastBoundFBO = pingpongFBO [horizontal ? 0 : 1];

            if (i == 0) {
                input.colorAttachment (1).bind (TextureTarget.texture2D, 0);
            } else {
                pingpongFBO [horizontal ? 1 : 0].colorAttachment (0)
                        .bind (TextureTarget.texture2D, 0);
            }
            bloomProgram.setUniform ("u_horizontal", horizontal ? 1 : 0);
            bloomProgram.setUniform ("u_image", 0);
            fullScreenQuad.draw ();

        }

        // Blit to output
        GL30.glBindFramebuffer (GL33.GL_FRAMEBUFFER, 0);;
        GL33.glBindFramebuffer (GL33.GL_READ_FRAMEBUFFER, lastBoundFBO.getId ());
        GL33.glReadBuffer (GL33.GL_COLOR_ATTACHMENT0);
        GL33.glBindFramebuffer (GL33.GL_DRAW_FRAMEBUFFER, output == null ? 0 : output.getId ());

        GL33.glClear (GL33.GL_COLOR_BUFFER_BIT);

        GL30.glBlitFramebuffer (
                0, 0,
                GameWindow.getInstance ().getWindowWidth (), GameWindow.getInstance ().getWindowHeight (),
                0, 0,
                GameWindow.getInstance ().getWindowWidth (), GameWindow.getInstance ().getWindowHeight (),
                GL30.GL_COLOR_BUFFER_BIT,
                GL30.GL_NEAREST
        );

        GL30.glBindFramebuffer (GL30.GL_READ_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer (GL30.GL_DRAW_FRAMEBUFFER, 0);

    }

    @Override
    public Program getProgram () {
        return bloomProgram;
    }

}
