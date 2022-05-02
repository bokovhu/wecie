package hu.iit.bme.wecie.engine.debug;

import hu.iit.bme.wecie.engine.drawing.renderable.IndexedRenderable;
import hu.iit.bme.wecie.engine.drawing.renderable.Renderable;
import hu.iit.bme.wecie.engine.drawing.renderable.RenderableFactory;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.shader.ProgramFactory;
import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import hu.iit.bme.wecie.engine.opengl.texture.TextureTarget;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class GLDebug {

    private static GLDebug INSTANCE = new GLDebug ();

    private boolean initialized = false;

    private Renderable fullScreenQuad;
    private Renderable cube;
    private Program depthToScreenProgram;
    private Program colorToScreenProgram;
    private Program depthCubemapToScreenProgram;

    public static GLDebug getInstance () {
        if (!INSTANCE.initialized) {
            INSTANCE.initialize ();
        }
        return INSTANCE;
    }

    private void initialize () {

        this.fullScreenQuad = RenderableFactory.quad (new Vector2f (2f, 2f));
        this.cube = RenderableFactory.cube (new Vector3f (2f));
        this.depthToScreenProgram = ProgramFactory.createFromResources (
                "glsl/debug/depth-to-screen/vertex.glsl",
                "glsl/debug/depth-to-screen/fragment.glsl",
                getClass ().getClassLoader ()
        );
        this.colorToScreenProgram = ProgramFactory.createFromResources (
                "glsl/debug/color-to-screen/vertex.glsl",
                "glsl/debug/color-to-screen/fragment.glsl",
                getClass ().getClassLoader ()
        );
        depthCubemapToScreenProgram = ProgramFactory.createFromResources (
                "glsl/debug/depth-cubemap-to-screen/vertex.glsl",
                "glsl/debug/depth-cubemap-to-screen/fragment.glsl",
                getClass ().getClassLoader ()
        );

        this.initialized = true;

    }

    public void drawDepthBufferToScreen (
            FrameBuffer frameBuffer,
            float scale,
            boolean isPerspective,
            float near, float far
    ) {

        GL33.glBindFramebuffer (GL33.GL_FRAMEBUFFER, 0);
        GL33.glClearColor (0f, 0f, 0f, 1f);
        GL33.glClear (GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

        depthToScreenProgram.use ();
        frameBuffer.depthAttachment ()
                .bind (
                        TextureTarget.texture2D,
                        0
                );
        depthToScreenProgram.setUniform ("u_depthMap", 0);
        depthToScreenProgram.setUniform ("u_scale", scale);
        if (isPerspective) {
            depthToScreenProgram.setUniform ("u_perspective", 1);
            depthToScreenProgram.setUniform ("u_near", near);
            depthToScreenProgram.setUniform ("u_far", far);
        } else {
            depthToScreenProgram.setUniform ("u_perspective", 0);
        }

        fullScreenQuad.draw ();

    }

    public void drawDepthCubemapBufferToScreen (
            FrameBuffer frameBuffer,
            Matrix4f projectionMatrix
    ) {

        GL33.glBindFramebuffer (GL33.GL_FRAMEBUFFER, 0);
        GL33.glClearColor (0f, 0f, 0f, 1f);
        GL33.glClear (GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);


        depthCubemapToScreenProgram.use ();
        frameBuffer.colorAttachment (0)
                .bind (
                        TextureTarget.cubemap,
                        0
                );
        depthCubemapToScreenProgram.setUniform ("u_map", 0);
        depthCubemapToScreenProgram.setUniform ("u_projection", projectionMatrix);

        cube.draw ();

    }

    public void drawColorBufferToScreen (
            FrameBuffer frameBuffer,
            int attachmentIndex
    ) {

        GL33.glBindFramebuffer (GL33.GL_FRAMEBUFFER, 0);
        GL33.glClearColor (0f, 0f, 0f, 1f);
        GL33.glClear (GL33.GL_COLOR_BUFFER_BIT | GL33.GL_DEPTH_BUFFER_BIT);

        colorToScreenProgram.use ();
        frameBuffer.colorAttachment (attachmentIndex)
                .bind (
                        TextureTarget.texture2D,
                        0
                );
        colorToScreenProgram.setUniform ("u_map", 0);
        fullScreenQuad.draw ();

    }

}
