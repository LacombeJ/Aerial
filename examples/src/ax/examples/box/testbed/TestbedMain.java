package ax.examples.box.testbed;

import ax.box.Physics2dModule;
import ax.commons.io.Console;
import ax.engine.core.Application;
import ax.engine.core.Camera;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.math.vector.Color;
import ax.math.vector.Vector3;
import ax.std.StandardApplication;
import ax.std.misc.FirstPersonControl;
import ax.std.misc.MouseGrabToggle;
import ax.std.render.Light;

public class TestbedMain {

    public static void main(String[]args) {
        new TestbedMain().run();
    }

    void run() {

        Testbed testbed = new Testbed();

        Application app = new StandardApplication();

        app.add(new Physics2dModule());

        Scene scene = new Scene();
        SceneObject player = new SceneObject();
        Camera camera = new Camera();
        camera.setClearColor(TestbedTest.BACK.toVector());
        player.addComponent(camera);
        player.transform().translation = new Vector3(1.5f, 10.5f, 30f);

        scene.add(player);

        SceneObject sun = new SceneObject();
        Light sunlight = new Light();
        sunlight.setType(Light.DIRECTIONAL);
        sunlight.setDirection(new Vector3(-0.8f, -0.4f, -0.6f));
        sunlight.setAmbient(new Vector3(0.01f, 0.01f, 0.01f));
        sun.addComponent(sunlight);

        scene.add(sun);

        testbed.test(0).init(scene);

        app.addScene(scene);

        app.start();

    }

}
