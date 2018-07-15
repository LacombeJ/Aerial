package ax.std.render.shadow;

import ax.engine.core.*;
import ax.graphics.GL;
import ax.graphics.Program;
import ax.math.vector.Matrix4;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;
import ax.std.Std;
import ax.std.render.Light;

import java.util.ArrayList;

public class ShadowMap {

    ShadowControl control;

    int shadowWidth = 1024;
    int shadowHeight = 1024;
    Texture texture;
    FrameBuffer frameBuffer;
    Camera lightView;
    Camera camera;
    Light light;

    public ShadowMap(ShadowControl control, Camera camera, Light light) {

        this.control = control;
        this.camera = camera;
        this.light = light;

        lightView = new Camera();

        texture = new Texture(shadowWidth, shadowHeight, GL.RGBA16F, GL.REPEAT, GL.NEAREST);

        frameBuffer = new FrameBuffer(texture);

    }

    public void render(Service service) {

        GL gl = service.getGL();

        Scene scene = light.scene();

        ArrayList<SceneObject> sceneObjects = scene.getAllSceneObjects();

        Transform transform = light.transform();

        if (light.getType()==Light.DIRECTIONAL) {
            Vector3 direction = light.getDirection();

            Quaternion rot = Std.rotationFromVector(direction);

            //TODO check if lights are rotated properly when transform rotated
            transform.rotation = rot;
        }

        Matrix4 V = Camera.computeViewMatrix(transform);
        Matrix4 P = Matrix4.orthographic(-10, 10, -10, 10, -100, 100);
        Matrix4 VP = P.get().multiply(V);

        ax.graphics.FrameBuffer fb = service.getOrCreateFrameBuffer(frameBuffer);

        gl.glBindFramebuffer(fb);

        gl.glViewport(0,0,texture.width(),texture.height());

        gl.glClearColor(0,0,0,1);

        gl.glClear(GL.COLOR_BUFFER_BIT, GL.DEPTH_BUFFER_BIT);

        for (SceneObject so : sceneObjects) {

            Mesh mesh = so.getComponent(Mesh.class);
            if (mesh != null && mesh.isVisible()) {

                Geometry geometry = mesh.getGeometry();

                Matrix4 M = service.getWorldTransform(so).computeMatrix();
                Matrix4 MVP = VP.get().multiply(M);

                Program program = control.program;

                gl.glUseProgram(program);

                program.setUniformMat4("MVP",MVP.toArray());

                if (mesh.cullFace) {
                    gl.glEnable(GL.CULL_FACE);
                } else {
                    gl.glDisable(GL.CULL_FACE);
                }

                if (mesh.isWireframe()) {
                    gl.glPolygonMode(GL.FRONT_AND_BACK, GL.LINE);
                }

                if (!mesh.getDepthTest()) {
                    gl.glDisable(GL.DEPTH_TEST);
                }

                gl.glLineWidth(mesh.getThickness());
                gl.glPointSize(mesh.getThickness());

                gl.glRender(service.getOrCreateMesh(geometry),mesh.getMode());

                gl.glLineWidth(1);
                gl.glPointSize(1);

                if (!mesh.getDepthTest()) {
                    gl.glEnable(GL.DEPTH_TEST);
                }

                if (mesh.isWireframe()) {
                    gl.glPolygonMode(GL.FRONT_AND_BACK, GL.FILL);
                }

                gl.glEnable(GL.CULL_FACE);

                gl.glUseProgram(null);
            }

        }

        gl.glBindFramebuffer(null);

    }

    public Texture texture() {
        return texture;
    }



}
