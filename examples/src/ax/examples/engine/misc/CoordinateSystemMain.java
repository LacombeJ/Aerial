package ax.examples.engine.misc;

import ax.commons.io.Console;
import ax.engine.core.*;
import ax.engine.core.geometry.ConeGeometry;
import ax.engine.core.material.SolidMaterial;
import ax.math.vector.Color;
import ax.math.vector.Vector3;
import ax.std.StandardApplication;
import ax.std.misc.FirstPersonControl;
import ax.std.misc.MouseGrabToggle;
import ax.std.misc.ScreenLog;

public class CoordinateSystemMain {

    public static void main(String[]args) {
        new CoordinateSystemMain().run();
    }

    ScreenLog screen = new ScreenLog();

    void run() {

        Application app = new StandardApplication();

        Scene scene = new Scene();

        add(scene);
        scene.add(screen.get());

        app.addScene(scene);

        Console.log("Up:",Vector3.up());
        Console.log("Right:",Vector3.right());
        Console.log("Forward:",Vector3.forward());

        screen.log("Up:",Vector3.up());
        screen.log("Right:",Vector3.right());
        screen.log("Forward:",Vector3.forward());

        app.start();

    }

    void add(Scene scene) {

        scene.add(player());
        scene.add(up());
        scene.add(right());
        scene.add(forward());

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

        so.transform().translation = new Vector3(0,0,4);

        return so;
    }

    SceneObject cone(Color color) {
        SceneObject so = new SceneObject();

        Geometry geometry = new ConeGeometry();
        Material material = new SolidMaterial(color.toVector());
        Mesh mesh = new Mesh(geometry, material);

        so.addComponent(mesh);

        return so;
    }

    SceneObject up() {
        SceneObject so = cone(Color.BLUE);

        so.transform().translation = Vector3.up();

        return so;
    }

    SceneObject right() {
        SceneObject so = cone(Color.RED);

        so.transform().translation = Vector3.right();

        return so;
    }

    SceneObject forward() {
        SceneObject so = cone(Color.GREEN);

        so.transform().translation = Vector3.forward();

        return so;
    }

}
