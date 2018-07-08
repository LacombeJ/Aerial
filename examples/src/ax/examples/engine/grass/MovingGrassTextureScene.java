package ax.examples.engine.grass;

import ax.aui.Widget;
import ax.aui.Window;
import ax.editor.ui.SubApp;
import ax.engine.core.Camera;
import ax.engine.core.Geometry;
import ax.engine.core.Material;
import ax.engine.core.Mesh;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.geometry.BoxGeometry;
import ax.engine.core.material.TextureMaterial;
import ax.math.vector.Color;
import ax.math.vector.Vector3;
import ax.std.misc.FPCToggle;
import ax.std.misc.FirstPersonControl;
import ax.std.render.Light;
import ax.std.render.StandardMaterialBuilder;

public class MovingGrassTextureScene {

    Window window;
    Widget widget;
    
    MovingGrassTextureScene(Window window, Widget widget) {
        this.window = window;
        this.widget = widget;
        app();
    }
    
    MovingGrassTexture mgt;
    
    void app() {
        
        SubApp app = new SubApp(window,widget);
        app.setResizable(true);
        
        mgt = new MovingGrassTexture();
        
        Scene scene = new Scene();
        
        SceneObject player = player();
        
        SceneObject light = light();
        
        SceneObject cube = cube();
        
        scene.add(mgt.sceneObject);
        scene.add(player);
        scene.add(light);
        scene.add(cube);
        
        app.addScene(scene);
        
        app.start();
        
    }
    
    SceneObject player() {
        SceneObject go = new SceneObject();
        
        Camera cam = new Camera();
        cam.setClearColor(0.1f, 0.2f, 0.4f, 1);
        
        go.transform().translation.set(0,0,2);
        
        FirstPersonControl fpc = new FirstPersonControl();
        FPCToggle mgt = new FPCToggle(true);
        Light light = new Light();
        
        go.addComponent(light);
        go.addComponent(mgt);
        go.addComponent(fpc);
        go.addComponent(cam);
        
        return go;
    }
    
    SceneObject cube() {
        SceneObject go = new SceneObject();
        
        Geometry geometry = new BoxGeometry();
        Material material = new StandardMaterialBuilder().build();
        material = new TextureMaterial(mgt.texture);
        
        Mesh mesh = new Mesh(geometry, material);
        
        go.addComponent(mesh);
        
        return go;
    }
    
    SceneObject light() {
        SceneObject go = new SceneObject();
        
        Light light = new Light();
        light.setType(Light.DIRECTIONAL);
        light.setColor(Color.WHITE.toVector().xyz());
        light.setDirection(new Vector3(-1.3f,-1.2f,-1.9f));
        light.setAmbient(Color.fromInt(13,13,26).toVector().scale(0.1f).xyz());
        
        go.addComponent(light);
        
        go.transform().translation.set(3,3,-3);
        
        return go;
    }
}
