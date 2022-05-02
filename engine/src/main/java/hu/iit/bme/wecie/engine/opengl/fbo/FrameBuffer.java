package hu.iit.bme.wecie.engine.opengl.fbo;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;

import java.util.List;

public interface FrameBuffer {

    int getId ();

    FrameBuffer generate ();

    FrameBuffer attachColor (int attachmentIndex, Texture texture);

    FrameBuffer attachDepth (Texture texture);

    FrameBuffer attachDepthCubemap (Texture texture);

    FrameBuffer attachStencil (Texture texture);

    FrameBuffer attachDepthStencil (Texture texture);

    FrameBuffer generateColorRenderBuffer (int attachmentIndex, int width, int height);

    FrameBuffer generateDepthRenderBuffer (int width, int height);

    FrameBuffer generateStencilRenderBuffer (int width, int height);

    FrameBuffer generateDepthStencilRenderBuffer (int width, int height);

    Texture colorAttachment (int index);

    Texture depthAttachment ();

    Texture stencilAttachment ();

    FrameBuffer bind ();

    FrameBuffer clear (int clearFlags);

    FrameBuffer delete ();

    FrameBuffer drawBuffers (int... attachments);

    FrameBuffer drawBuffers (List<Integer> attachments);

}
