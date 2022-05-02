package hu.iit.bme.wecie.engine.scene.pbr;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.drawing.three.Model;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBufferFactory;
import hu.iit.bme.wecie.engine.opengl.texture.*;
import hu.iit.bme.wecie.engine.scene.BaseRenderPipeline;
import hu.iit.bme.wecie.engine.scene.RenderPipeline;
import hu.iit.bme.wecie.engine.scene.World;
import hu.iit.bme.wecie.engine.scene.pipeline.BlitInputToOutputStage;
import hu.iit.bme.wecie.engine.scene.world.Environment3D;
import hu.iit.bme.wecie.engine.scene.world.Light3D;
import hu.iit.bme.wecie.engine.scene.world.World3D;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class PBRPipeline extends
        BaseRenderPipeline<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> {

    private FrameBuffer gBuffer = FrameBufferFactory.create ()
            .attachColor ( // World position and mask buffer
                    0,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0f, 0f, 0f, 0f)
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.linear,
                            TextureMagFilter.linear,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            ).attachColor ( // Normal and roughness buffer
                    1,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0f, 0f, 0f, 0f)
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.linear,
                            TextureMagFilter.linear,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            ).attachColor ( // Albedo and metallic buffer
                    2,
                    TextureFactory.empty (
                            ImageDataFactory.createRGBA8UI (
                                    GameWindow.getInstance ().getWindowWidth (),
                                    GameWindow.getInstance ().getWindowHeight (),
                                    0, 0, 0, 0
                            )
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.linear,
                            TextureMagFilter.linear,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            ).attachColor ( // Ambient occlusion buffer
                    3,
                    TextureFactory.empty (
                            ImageDataFactory.createRGBA8UI (
                                    GameWindow.getInstance ().getWindowWidth (),
                                    GameWindow.getInstance ().getWindowHeight (),
                                    0, 0, 0, 0
                            )
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.linear,
                            TextureMagFilter.linear,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            ).generateDepthStencilRenderBuffer (
                    GameWindow.getInstance ().getWindowWidth (),
                    GameWindow.getInstance ().getWindowHeight ()
            ).drawBuffers (
                    GL30.GL_COLOR_ATTACHMENT0,
                    GL30.GL_COLOR_ATTACHMENT1,
                    GL30.GL_COLOR_ATTACHMENT2,
                    GL30.GL_COLOR_ATTACHMENT3
            );
    private FrameBuffer lightingResult = FrameBufferFactory.create ()
            .attachColor (
                    0,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0, 0, 0, 0)
                    )
            ).attachColor (
                    1,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0, 0, 0, 0)
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.nearest,
                            TextureMagFilter.nearest,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            ).drawBuffers (
                    GL33.GL_COLOR_ATTACHMENT0,
                    GL33.GL_COLOR_ATTACHMENT1
            );
    private FrameBuffer bloomResult = FrameBufferFactory.create ()
            .attachColor (
                    0,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0, 0, 0, 0)
                    ).configure (
                            TextureTarget.texture2D,
                            TextureMinFilter.nearest,
                            TextureMagFilter.nearest,
                            TextureWrap.clampToEdge,
                            TextureWrap.clampToEdge
                    )
            );
    private FrameBuffer compositionResult = FrameBufferFactory.create ()
            .attachColor (
                    0,
                    TextureFactory.empty (
                            GameWindow.getInstance ().getWindowWidth (),
                            GameWindow.getInstance ().getWindowHeight (),
                            new Vector4f (0, 0, 0, 0)
                    )
            ).generateDepthStencilRenderBuffer (
                    GameWindow.getInstance ().getWindowWidth (),
                    GameWindow.getInstance ().getWindowHeight ()
            );
    private FrameBuffer compositionInput = FrameBufferFactory.create ()
            .attachColor (0, lightingResult.colorAttachment (0))
            .attachColor (1, bloomResult.colorAttachment (0));

    public PBRPipeline () {
        this (null);
    }

    public PBRPipeline (FrameBuffer output) {
        this.then (
                new ShadowMapStage ().input (null).output (null)
        ).then (
                new GBufferStage ().input (null)
                        .output (gBuffer)
        ).then (
                new LightingStage ().input (gBuffer)
                        .output (lightingResult)
        ).then (
                new BloomStage ().input (lightingResult)
                        .output (bloomResult)
        ).then (
                new CompositionStage ().input (compositionInput)
                        .output (compositionResult)
        ).then (
                new BlitInputToOutputStage<World<Environment3D, Model, Light3D>, Environment3D, Light3D, Model, PBRPipelineParameters> ()
                        .input (compositionResult)
                        .output (output)
        );
    }

}
