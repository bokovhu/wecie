package hu.iit.bme.wecie.engine.scene.world;

import hu.iit.bme.wecie.engine.GameWindow;
import hu.iit.bme.wecie.engine.drawing.Camera;
import hu.iit.bme.wecie.engine.drawing.CameraFactory;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBuffer;
import hu.iit.bme.wecie.engine.opengl.fbo.FrameBufferFactory;
import hu.iit.bme.wecie.engine.opengl.shader.Program;
import hu.iit.bme.wecie.engine.opengl.texture.*;
import hu.iit.bme.wecie.engine.scene.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.List;

public class Light3D implements Light {

    private static final Vector3f[][] pointLightDirections = {
            {new Vector3f (1f, 0f, 0f), new Vector3f (0.0f, -1.0f, 0.0f)},
            {new Vector3f (-1f, 0f, 0f), new Vector3f (0.0f, -1.0f, 0.0f)}, // CUBEMAP_NEGATIVE_X
            {new Vector3f (0f, 1f, 0f), new Vector3f (0.0f, 0.0f, 1.0f)}, // CUBEMAP_POSITIVE_Y
            {new Vector3f (0f, -1f, 0f), new Vector3f (0.0f, 0.0f, -1.0f)}, // CUBEMAP_NEGATIVE_Y
            {new Vector3f (0f, 0f, 1f), new Vector3f (0.0f, -1.0f, 0.0f)}, // CUBEMAP_POSITIVE_Z
            {new Vector3f (0f, 0f, -1f), new Vector3f (0.0f, -1.0f, 0.0f)} // CUBEMAP_NEGATIVE_Z
    };

    private final int id;
    private Light3D.Type type;
    private Vector3f position;
    private Vector3f direction;
    private float cutOffAngle;
    private float outerCutOffAngle;
    private Vector3f energy;
    private boolean castsShadow = false;
    private FrameBuffer shadowBuffer;
    private List<Matrix4f> matrices = new ArrayList<> ();
    private float constant;
    private float linear;
    private float quadratic;

    private Camera camera = CameraFactory.create ();

    private Vector3f target = new Vector3f ();
    private Vector3f up = new Vector3f ();

    private void createShadowBuffer () {

        if (shadowBuffer != null) {
            shadowBuffer.delete ();
        }

        switch (type) {
            case Point:

                shadowBuffer = FrameBufferFactory.create ()
                        .attachDepth (
                                TextureFactory.emptyDepth (
                                        GameWindow.getInstance ().getShadowMapWidth (),
                                        GameWindow.getInstance ().getShadowMapHeight ()
                                )
                        ).attachColor (
                                0,
                                TextureFactory.emptyCubemap (
                                        GameWindow.getInstance ().getShadowMapWidth (),
                                        GameWindow.getInstance ().getShadowMapHeight (),
                                        InternalPixelFormat.r32f,
                                        PixelFormat.red,
                                        PixelDataType.glFloat
                                )
                        );
                break;
            case Spot:
            case Directional:

                shadowBuffer = FrameBufferFactory.create ()
                        .attachDepth (
                                TextureFactory.emptyDepth (
                                        GameWindow.getInstance ().getShadowMapWidth (),
                                        GameWindow.getInstance ().getShadowMapHeight ()
                                )
                        );

                break;
        }

        GL33.glDrawBuffer (GL33.GL_NONE);
        GL33.glReadBuffer (GL33.GL_NONE);

        GL33.glBindFramebuffer (GL33.GL_FRAMEBUFFER, 0);

    }

    public Light3D (int id) {
        this.id = id;
        this.type = Light3D.Type.Ambient;
        this.position = new Vector3f ();
        this.direction = new Vector3f ();
        this.cutOffAngle = 0.0f;
        this.energy = new Vector3f ();
    }

    public Light3D ambient (Vector3f energy) {
        this.type = Light3D.Type.Ambient;
        this.energy.set (energy);
        return this;
    }

    public Light3D point (Vector3f position, Vector3f energy, float constant, float linear, float quadratic) {
        this.type = Light3D.Type.Point;
        this.position.set (position);
        this.energy.set (energy);
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
        if (castsShadow) {
            calculateMatrices ();
        }
        return this;
    }

    public Light3D directional (Vector3f direction, Vector3f energy) {
        this.type = Light3D.Type.Directional;
        this.direction.set (direction);
        this.energy.set (energy);
        return this;
    }

    public Light3D spot (
            Vector3f position,
            Vector3f direction,
            float cutOffAngle,
            float outerCutOffAngle,
            Vector3f energy,
            float constant,
            float linear,
            float quadratic
    ) {
        this.type = Light3D.Type.Spot;
        this.position.set (position);
        this.direction.set (direction).normalize ();
        this.cutOffAngle = cutOffAngle;
        this.outerCutOffAngle = outerCutOffAngle;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
        this.energy.set (energy);
        if (castsShadow) {
            calculateMatrices ();
        }
        return this;
    }

    public Light3D enableShadows () {
        this.castsShadow = true;
        createShadowBuffer ();
        calculateMatrices ();
        return this;
    }

    public Light3D disableShadows () {
        this.castsShadow = false;
        if (shadowBuffer != null) {
            shadowBuffer.delete ();
        }
        return this;
    }

    private void calculateMatrices () {

        switch (type) {
            case Point:

                matrices.clear ();
                for (int i = 0; i < 6; i++) {
                    camera.perspective (
                            (float) Math.toRadians (90.0),
                            (float) GameWindow.getInstance ().getShadowMapWidth ()
                                    / (float) GameWindow.getInstance ().getShadowMapHeight (),
                            0.1f, 30
                    ).lookAt (
                            this.position,
                            this.target.set (this.position).add (pointLightDirections[i][0]),
                            pointLightDirections[i][1]
                    );
                    matrices.add (new Matrix4f (camera.viewProjection ()));
                }

                break;
            case Spot:

                // We use an outer cutoff angle of angle + PI / 24
                final float spotAngle = this.outerCutOffAngle;

                matrices.clear ();

                this.up.set (0f, 1f, 0f);
                final float upDirectionAngle = this.direction.angle (this.up);
                if (upDirectionAngle <= 0.01f || upDirectionAngle >= (float) Math.PI - 0.01f) {
                    this.up.set (0f, 0f, 1f);
                }

                camera.perspective (
                        spotAngle * 2.0f,
                        (float) GameWindow.getInstance ().getShadowMapWidth ()
                                / (float) GameWindow.getInstance ().getShadowMapHeight (),
                        0.1f, 30
                ).lookAt (
                        this.position,
                        this.target.set (this.position).add (this.direction),
                        this.up
                );

                matrices.add (camera.viewProjection ());

                break;
        }

    }

    public Light3D.Type getType () {
        return type;
    }

    public Vector3f getPosition () {
        return position;
    }

    public Vector3f getDirection () {
        return direction;
    }

    public float getCutOffAngle () {
        return cutOffAngle;
    }

    public Vector3f getEnergy () {
        return energy;
    }

    public boolean isCastsShadow () {
        return castsShadow;
    }

    public FrameBuffer getShadowBuffer () {
        return shadowBuffer;
    }

    public List<Matrix4f> getMatrices () {
        return matrices;
    }

    @Override
    public int getId () {
        return id;
    }

    public void setShadowUniforms (
            int lightIndex,
            Program program,
            String shadowMapUniformArrayName,
            String shadowCubeUniformArrayName,
            int shadowMapTextureUnit
    ) {

        if (castsShadow) {

            switch (type) {
                case Point:

                    shadowBuffer.colorAttachment (0)
                            .bind (
                                    TextureTarget.cubemap,
                                    shadowMapTextureUnit
                            );
                    program.setUniform (
                            shadowCubeUniformArrayName + "[" + lightIndex + "]",
                            shadowMapTextureUnit
                    );

                    break;
                case Directional:
                case Spot:

                    shadowBuffer.depthAttachment ()
                            .bind (
                                    TextureTarget.texture2D,
                                    shadowMapTextureUnit
                            );
                    program.setUniform (
                            shadowMapUniformArrayName + "[" + lightIndex + "]",
                            shadowMapTextureUnit
                    );

                    break;
            }

        }

    }

    @Override
    public void setProgramUniforms (Program program, String objectPrefix) {

        program.setUniform (objectPrefix + ".type", getType ().typeId);
        program.setUniform (objectPrefix + ".enabled", 1);
        program.setUniform (objectPrefix + ".position", position);
        program.setUniform (objectPrefix + ".direction", direction);
        program.setUniform (objectPrefix + ".position", position);
        program.setUniform (objectPrefix + ".cutOffAngle", cutOffAngle);
        program.setUniform (objectPrefix + ".energy", energy);
        program.setUniform (objectPrefix + ".outerCutOffAngle", outerCutOffAngle);
        program.setUniform (objectPrefix + ".constant", constant);
        program.setUniform (objectPrefix + ".linear", linear);
        program.setUniform (objectPrefix + ".quadratic", quadratic);

        if (castsShadow) {

            program.setUniform (
                    objectPrefix + ".castsShadow",
                    1
            );

            switch (type) {
                case Point:

                    break;
                case Directional:
                case Spot:

                    program.setUniform (
                            objectPrefix + ".viewProjection",
                            matrices.get (0)
                    );


                    break;
            }

        } else {
            program.setUniform (
                    objectPrefix + ".castsShadow",
                    0
            );
        }

    }

    public enum Type {

        Ambient (0),
        Point (1),
        Directional (2),
        Spot (3);

        public int typeId;

        Type (int typeId) {
            this.typeId = typeId;
        }

    }

    public float getOuterCutOffAngle () {
        return outerCutOffAngle;
    }

    public float getConstant () {
        return constant;
    }

    public float getLinear () {
        return linear;
    }

    public float getQuadratic () {
        return quadratic;
    }

}
