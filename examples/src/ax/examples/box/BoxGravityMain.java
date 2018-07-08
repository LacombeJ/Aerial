package ax.examples.box;

import ax.box.Body2dType;
import ax.box.BoxCollider2d;
import ax.box.Physics2dModule;
import ax.box.RigidBody2d;
import ax.commons.io.Console;
import ax.engine.core.Application;
import ax.engine.core.Camera;
import ax.engine.core.Geometry;
import ax.engine.core.Material;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.geometry.BoxGeometry;
import ax.math.vector.Color;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;
import ax.std.StandardApplication;
import ax.std.misc.FirstPersonControl;
import ax.std.misc.MouseGrabToggle;
import ax.std.render.Light;
import ax.std.render.StandardMaterial;
import ax.std.render.StandardMaterialBuilder;

public class BoxGravityMain {

    public static void main(String[]args) {
        new BoxGravityMain().run();
    }
    
    final static Color SLATE         = Color.fromHex("#404E5A");
    final static Color MANGO         = Color.fromHex("#FFC800");
    
    void run() {
        
        Application app = new StandardApplication();
        //app.setFullscreen(true);
        app.add(new Physics2dModule());
        
        Scene scene = new Scene();
        
        SceneObject sun = new SceneObject();
        Light sunlight = new Light();
        sunlight.setType(Light.DIRECTIONAL);
        sunlight.setDirection(new Vector3(-0.8f, -0.4f, -0.6f));
        sunlight.setAmbient(new Vector3(0.01f, 0.01f, 0.01f));
        sun.addComponent(sunlight);
        
        SceneObject player = new SceneObject();
        player.addComponent(new FirstPersonControl());
        player.addComponent(new MouseGrabToggle());
        Camera camera = new Camera();
        camera.setClearColor(SLATE.toVector());
        player.addComponent(camera);
        player.transform().translation.z = 5;
        player.addUpdate(()->{
            Console.log("Player position: ", player.transform().translation);
        });
        
        SceneObject cube = new SceneObject();
        Geometry box = new BoxGeometry(1,1,1);
        Material mat = standard(MANGO.toVector());
        cube.addComponent(new Mesh(box,mat));
        BoxCollider2d boxCollider = new BoxCollider2d(1,1);
        RigidBody2d boxBody = new RigidBody2d();
        boxBody.setBodyType(Body2dType.DYNAMIC);
        boxBody.setDensity(1.0f);
        boxBody.setFriction(0.3f);
        cube.addComponent(boxCollider);
        cube.addComponent(boxBody);
        cube.transform().translation.x = 5.5f;
        cube.transform().translation.y = 2;
        
        
        SceneObject floor = new SceneObject();
        Geometry floorBox = new BoxGeometry(10,1,1);
        floor.addComponent(new Mesh(floorBox,mat));
        BoxCollider2d floorCollider = new BoxCollider2d(10,1);
        RigidBody2d floorBody = new RigidBody2d();
        floor.addComponent(floorCollider);
        floor.addComponent(floorBody);
        floor.transform().translation.y = -3;
        
        scene.add(sun);
        scene.add(player);
        scene.add(cube);
        scene.add(floor);
        
        app.addScene(scene);
        
        app.start();
        
    }
    
    StandardMaterial standard(Vector4 color) {
        StandardMaterialBuilder smb = new StandardMaterialBuilder();
        
        smb.diffuse = smb.vec3(color.xyz());
        smb.specular = smb.vec3(0.05f);
        
        return smb.build();
    }
    
}
