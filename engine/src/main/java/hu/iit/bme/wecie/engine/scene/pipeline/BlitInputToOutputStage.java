package hu.iit.bme.wecie.engine.scene.pipeline;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.DrawableComponent;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipelineStage;
import hu.iit.bme.wecie.engine.scene.Environment;
import hu.iit.bme.wecie.engine.scene.Light;
import hu.iit.bme.wecie.engine.scene.World;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class BlitInputToOutputStage<W extends World<E, D, L>, E extends Environment<L>, L extends Light, D extends DrawableComponent, P> extends
        BaseRenderPipelineStage<W, E, L, D, P> {

    private int colorAttachmentIndex = 0;

    public BlitInputToOutputStage <W, E, L, D, P> forColorAttachment (int i) {
        this.colorAttachmentIndex = i;
        return this;
    }

    @Override
    public Program getProgram () {
        return null;
    }

    @Override
    public void execute (W world, Camera camera, P pipelineParams) {

        GL33.glUseProgram (0);

        GL30.glBindFramebuffer (GL30.GL_FRAMEBUFFER, 0);
        GL30.glBindFramebuffer (GL30.GL_READ_FRAMEBUFFER, input == null ? 0 : input.getId ());
        GL30.glReadBuffer (GL30.GL_COLOR_ATTACHMENT0 + colorAttachmentIndex);
        GL30.glBindFramebuffer (GL30.GL_DRAW_FRAMEBUFFER, output == null ? 0 : output.getId ());

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

}
