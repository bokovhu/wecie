package hu.iit.bme.wecie.engine.drawing;

import hu.iit.bme.wecie.engine.opengl.shader.Program;
import org.joml.Matrix4f;
import org.joml.Vector3f;

class CameraImpl implements Camera {

    private Matrix4f projection = new Matrix4f ();
    private Matrix4f view = new Matrix4f ();
    private Matrix4f viewProjection = new Matrix4f ();
    private Matrix4f invProjection = new Matrix4f ();
    private Matrix4f invView = new Matrix4f ();
    private Vector3f position = new Vector3f (0.0f, 0.0f, 0.0f);
    private Vector3f scale = new Vector3f (1.0f, 1.0f, 1.0f);
    private Vector3f up = new Vector3f (0.0f, 1.0f, 0.0f);
    private Vector3f right = new Vector3f (1.0f, 0.0f, 0.0f);
    private Vector3f front = new Vector3f (0.0f, 0.0f, 1.0f);
    private Vector3f target = new Vector3f (front);

    private void update () {

        viewProjection.set (projection)
                .mul (view);
        invProjection.set (projection)
                .invert ();
        invView.set (view)
                .invert ();

    }

    @Override
    public Matrix4f projection () {
        return projection;
    }

    @Override
    public Matrix4f view () {
        return view;
    }

    @Override
    public Matrix4f viewProjection () {
        return viewProjection;
    }

    @Override
    public Matrix4f invProjection () {
        return invProjection;
    }

    @Override
    public Matrix4f invView () {
        return invView;
    }

    @Override
    public Camera lookAt (Vector3f eye, Vector3f target, Vector3f up) {
        view.identity ()
                .lookAt (eye, target, up);
        // .scale (scale);
        this.position.set (eye);
        this.target.set (target);
        this.front.set (target).sub (eye).normalize ();
        this.up.set (up).normalize ();
        this.right.set (up).cross (front).normalize ();
        update ();
        return this;
    }

    @Override
    public Camera perspective (float fovy, float aspect, float zNear, float zFar) {
        projection.identity ()
                .perspective (fovy, aspect, zNear, zFar);
        update ();
        return this;
    }

    @Override
    public Camera orthographic (
            float topLeftX, float topLeftY, float bottomRightX, float bottomRightY, float zNear, float zFar
    ) {
        projection.identity ()
                .ortho (
                        topLeftX, bottomRightX,
                        bottomRightY, topLeftY,
                        zNear, zFar
                );
        update ();
        return this;
    }

    @Override
    public Vector3f up () {
        return up;
    }

    @Override
    public Vector3f right () {
        return right;
    }

    @Override
    public Vector3f front () {
        return front;
    }

    @Override
    public Vector3f position () {
        return position;
    }

    @Override
    public Vector3f scale () {
        return scale;
    }

    @Override
    public Camera setProgramUniforms (
            Program program, String objectNamePrefix
    ) {

        program.setUniform (objectNamePrefix + ".position", position);
        program.setUniform (objectNamePrefix + ".up", up);
        program.setUniform (objectNamePrefix + ".front", front);
        program.setUniform (objectNamePrefix + ".invProjection", invProjection);
        program.setUniform (objectNamePrefix + ".invView", invView);
        program.setUniform (objectNamePrefix + ".view", view);
        program.setUniform (objectNamePrefix + ".projection", projection);
        program.setUniform (objectNamePrefix + ".viewProjection", viewProjection);

        return this;
    }

    @Override
    public Camera set (Camera other) {

        this.position.set (other.position ());
        this.up.set (other.up ());
        this.right.set (other.right ());
        this.front.set (other.front ());
        this.view.set (other.view ());
        this.invView.set (other.invView ());
        this.projection.set (other.projection ());
        this.invProjection.set (other.invProjection ());
        this.viewProjection.set (other.viewProjection ());

        return this;
    }

}
