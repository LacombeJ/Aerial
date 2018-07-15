package ax.examples.engine.shadow;

import ax.engine.core.*;
import ax.engine.core.geometry.BoxGeometry;
import ax.engine.core.geometry.ConeGeometry;
import ax.engine.core.geometry.PlaneGeometry;
import ax.engine.core.geometry.SphereGeometry;
import ax.engine.core.material.TextureMaterial;
import ax.math.vector.Color;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;
import ax.std.StandardApplication;
import ax.std.misc.*;
import ax.std.render.Light;
import ax.std.render.StandardMaterialBuilder;
import ax.std.render.shadow.ShadowMap;
import ax.std.render.shadow.ShadowModule;

public class ShadowMain {

    public static void main(String[] args) {
        new ShadowMain().run();
    }

    void run() {
        scene();
    }

    ScreenLog log = new ScreenLog();

    ShadowModule shadowModule;
    Camera viewCamera;
    Light lightSource;

    void scene() {

        Application app = new StandardApplication();
        shadowModule = app.get(ShadowModule.class);

        Scene scene = new Scene();

        buildScene(scene);

        app.addScene(scene);

        app.start();

    }


    void buildScene(Scene scene) {

        scene.add(player());
        scene.add(floor());
        scene.add(sun());
        scene.add(box());
        scene.add(cone());
        scene.add(sphere());
        scene.add(display());
        scene.add(log.get());

    }

    SceneObject player() {

        SceneObject so = new SceneObject();

        Camera camera = new Camera();
        camera.setClearColor(Color.SLATE.toVector());

        FirstPersonControl fpc = new FirstPersonControl();
        MouseGrabToggle mgt = new MouseGrabToggle();

        so.addComponent(camera);
        so.addComponent(fpc);
        so.addComponent(mgt);

        so.transform().translation = new Vector3(-3, 9, 8);
        so.transform().rotation = Quaternion.euler(13, 10, 55);

        viewCamera = camera;

        CameraControl cc = new CameraControl();
        so.addComponent(cc);
        so.addUpdate(()->{
            if (so.input().isKeyPressed(Input.K_SPACE)) {
                cc.lookAt(new Vector3(0,0,0));
            }
        });

        return so;

    }

    SceneObject floor() {

        SceneObject so = new SceneObject();

        Geometry geometry = new BoxGeometry(10,1,10);
        Material material = standard(Color.MANGO.toVector());
        Mesh mesh = new Mesh(geometry,material);

        so.addComponent(mesh);

        return so;

    }

    SceneObject box() {

        SceneObject so = new SceneObject();

        Geometry geometry = new BoxGeometry(1,1,1);
        Material material = standard(Color.RED.toVector());
        Mesh mesh = new Mesh(geometry,material);

        so.addComponent(mesh);

        so.transform().translation = new Vector3(2,1f,2);

        return so;

    }

    SceneObject cone() {

        SceneObject so = new SceneObject();

        Geometry geometry = new ConeGeometry(0.5f, 1f);
        Material material = standard(Color.GREEN.toVector());
        Mesh mesh = new Mesh(geometry,material);

        so.addComponent(mesh);

        so.transform().translation = new Vector3(-2,1f,2);

        return so;

    }

    SceneObject sphere() {

        SceneObject so = new SceneObject();

        Geometry geometry = new SphereGeometry(0.5f);
        Material material = standard(Color.BLUE.toVector());
        Mesh mesh = new Mesh(geometry,material);

        so.addComponent(mesh);

        so.transform().translation = new Vector3(-2,1f,-2);

        return so;

    }

    SceneObject sun() {

        SceneObject so = new SceneObject();

        Light light = new Light();
        light.setType(Light.DIRECTIONAL);
        light.setDirection(new Vector3(1.1f,-0.9f,0.8f).norm());

        so.addComponent(light);

        lightSource = light;

        return so;

    }

    SceneObject display() {

        ShadowMap shadowMap = shadowModule.getOrCreateShadowMap(viewCamera, lightSource);

        CanvasObject canvas = new CanvasObject();

        SceneObject so = new SceneObject();
        Geometry box = new PlaneGeometry();
        Material mat = new TextureMaterial(shadowMap.texture());
        Mesh mesh = new Mesh(box,mat);
        so.addComponent(mesh);
        so.transform().scale.set(150,150,1);
        so.transform().translation.set(100,450,0);
        mesh.setDepthTest(false);

        canvas.addChild(so);

        return canvas.get();

    }

    Material standard(Vector4 color) {

        StandardMaterialBuilder sl = new StandardMaterialBuilder();

        sl.diffuse = sl.vec3(color.xyz());

        return sl.build();

    }

}
