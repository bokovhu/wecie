package hu.iit.bme.wecie.engine.opengl.fbo;

import hu.iit.bme.wecie.engine.opengl.texture.Texture;
import org.lwjgl.opengl.*;

import java.util.*;

final class FrameBufferImpl implements FrameBuffer {

    private int glId;

    private Texture depthAttachment;
    private Texture stencilAttachment;
    private Map<Integer, Texture> colorAttachments = new HashMap<> ();
    private List<Integer> renderBufferObjects = new ArrayList<> ();

    @Override
    public int getId () {
        return glId;
    }

    @Override
    public FrameBuffer generate () {

        this.glId = GL30.glGenFramebuffers ();

        return this;
    }

    @Override
    public FrameBuffer attachColor (int attachmentIndex, Texture texture) {

        bind ();
        GL30.glFramebufferTexture2D (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_COLOR_ATTACHMENT0 + attachmentIndex,
                GL30.GL_TEXTURE_2D,
                texture.getId (),
                0
        );
        colorAttachments.put (attachmentIndex, texture);

        return this;
    }

    @Override
    public FrameBuffer attachDepth (Texture texture) {

        bind ();
        GL30.glFramebufferTexture2D (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_TEXTURE_2D,
                texture.getId (),
                0
        );
        depthAttachment = texture;

        return this;
    }

    @Override
    public FrameBuffer attachDepthCubemap (Texture texture) {

        bind ();
        GL33.glFramebufferTexture (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT,
                texture.getId (),
                0
        );
        depthAttachment = texture;

        return this;
    }

    @Override
    public FrameBuffer attachStencil (Texture texture) {

        bind ();
        GL30.glFramebufferTexture2D (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_STENCIL_ATTACHMENT,
                GL30.GL_TEXTURE_2D,
                texture.getId (),
                0
        );
        stencilAttachment = texture;

        return this;
    }

    @Override
    public FrameBuffer attachDepthStencil (Texture texture) {

        bind ();
        GL30.glFramebufferTexture2D (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_STENCIL_ATTACHMENT,
                GL30.GL_TEXTURE_2D,
                texture.getId (),
                0
        );
        depthAttachment = texture;
        stencilAttachment = texture;

        return this;
    }

    private int generateRbo (int internalFormat, int w, int h) {

        int rboId = GL30.glGenRenderbuffers ();
        GL30.glBindRenderbuffer (
                GL30.GL_RENDERBUFFER,
                rboId
        );
        GL30.glRenderbufferStorage (
                GL30.GL_RENDERBUFFER,
                internalFormat, w, h
        );
        renderBufferObjects.add (rboId);
        GL30.glBindRenderbuffer (
                GL30.GL_RENDERBUFFER,
                0
        );

        return rboId;

    }

    @Override
    public FrameBuffer generateColorRenderBuffer (int attachmentIndex, int width, int height) {

        int rboId = generateRbo (GL30.GL_RGBA8, width, height);

        bind ();
        GL30.glFramebufferRenderbuffer (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_COLOR_ATTACHMENT0 + attachmentIndex,
                GL30.GL_RENDERBUFFER,
                rboId
        );

        return this;
    }

    @Override
    public FrameBuffer generateDepthRenderBuffer (int width, int height) {

        int rboId = generateRbo (GL30.GL_DEPTH_COMPONENT32, width, height);

        bind ();
        GL30.glFramebufferRenderbuffer (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER,
                rboId
        );

        return this;
    }

    @Override
    public FrameBuffer generateStencilRenderBuffer (int width, int height) {

        int rboId = generateRbo (GL30.GL_STENCIL_INDEX16, width, height);

        bind ();
        GL30.glFramebufferRenderbuffer (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_STENCIL_ATTACHMENT,
                GL30.GL_RENDERBUFFER,
                rboId
        );

        return this;
    }

    @Override
    public FrameBuffer generateDepthStencilRenderBuffer (int width, int height) {

        int rboId = generateRbo (GL30.GL_DEPTH24_STENCIL8, width, height);

        bind ();
        GL30.glFramebufferRenderbuffer (
                GL30.GL_FRAMEBUFFER,
                GL30.GL_DEPTH_STENCIL_ATTACHMENT,
                GL30.GL_RENDERBUFFER,
                rboId
        );

        return this;
    }

    @Override
    public Texture colorAttachment (int index) {
        return colorAttachments.get (index);
    }

    @Override
    public Texture depthAttachment () {
        return depthAttachment;
    }

    @Override
    public Texture stencilAttachment () {
        return stencilAttachment;
    }

    @Override
    public FrameBuffer bind () {

        GL30.glBindFramebuffer (
                GL30.GL_FRAMEBUFFER,
                this.glId
        );

        return this;
    }

    @Override
    public FrameBuffer clear (int clearFlags) {

        bind ();
        GL11.glClear (clearFlags);

        return this;
    }

    @Override
    public FrameBuffer delete () {

        GL30.glBindFramebuffer (GL30.GL_FRAMEBUFFER, 0);

        for (int rboId : renderBufferObjects) {
            GL30.glDeleteRenderbuffers (rboId);
        }

        GL30.glDeleteFramebuffers (glId);

        return this;
    }

    @Override
    public FrameBuffer drawBuffers (int... attachments) {
        List<Integer> list = new ArrayList<> ();
        for (int i : attachments) {
            list.add (i);
        }
        return drawBuffers (list);
    }

    @Override
    public FrameBuffer drawBuffers (List<Integer> attachments) {

        int[] arr = new int[attachments.size ()];
        for (int i = 0; i < attachments.size (); i++) {
            arr[i] = attachments.get (i);
        }

        bind ();
        GL30.glDrawBuffers (arr);

        return this;
    }

}
