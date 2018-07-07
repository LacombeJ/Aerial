package ax.editor.spline;

import ax.engine.core.Camera;
import ax.engine.core.Input;
import ax.engine.core.Mesh;
import ax.engine.core.Property;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.engine.core.geometry.SphereGeometry;
import ax.engine.core.material.SolidMaterial;
import ax.engine.mod.misc.OrthoBox;
import ax.engine.mod.misc.PerspectiveUpdate;
import ax.math.vector.Color;
import ax.math.vector.Vector2;

public class SplineScene {

    SplineEditor editor;
    Scene scene;
    
    SceneObject control;
    
    SplineScene(SplineEditor editor) {
        this.editor = editor;
        
        scene = new Scene();
        
        control = control();
        
        SceneObject sphere = sphere();
        
        scene.add(control);
        scene.add(sphere);
        
        load();
    }
    
    void load() {
        synchronized (editor) {
            control.transform().translation.x = editor.state.position.x;
            control.transform().translation.y = editor.state.position.y;
            
            for (Vector2 pos : editor.state.spheres) {
                createSphere(pos);
            }
        }
    }
    
    void save() {
        synchronized (editor) {
            editor.state.position.x = control.transform().translation.x;
            editor.state.position.y = control.transform().translation.y;
            editor.tool.store.setStore(editor);
        }
    }
    
    void saveSphere(Vector2 sphere) {
        synchronized (editor) {
            editor.state.spheres.add(sphere);
            editor.tool.store.setStore(editor);
        }
    }
    
    void createSphere(Vector2 pos) {
        SceneObject sphere = sphere();
        sphere.transform().translation.set(pos.x, pos.y, 0);
        scene.add(sphere);
    }
    
    SceneObject control() {
        SceneObject go = new SceneObject();
        
        Camera camera = new Camera();
        camera.setOrthographic(10,1,-1,1);
        camera.setClearColor(Color.LIGHT_GRAY.toVector());
        
        go.addComponent(camera);
        go.addComponent(new PerspectiveUpdate(camera));
        go.addComponent(new Control());
        go.addComponent(new OrthoBox());
        
        return go;
    }
    
    SceneObject sphere() {
        SceneObject go = new SceneObject();
        
        SphereGeometry geometry = new SphereGeometry(0.2f);
        SolidMaterial material = new SolidMaterial(Color.BLUE.toVector());
        Mesh mesh = new Mesh(geometry, material);
        
        go.addComponent(mesh);
        
        return go;
    }
    

    
    class Control extends Property {

        OrthoBox orthoBox;
        
        @Override
        public void create() {
            orthoBox = getComponent(OrthoBox.class);
        }

        @Override
        public void update() {
            if (input().isButtonPressed(Input.MB_LEFT)) {
                orthoBox.drag();
            }
            if (input().isButtonReleased(Input.MB_LEFT)) {
                orthoBox.release();
            }
            orthoBox.zoom(input().getScrollY());
            if (input().isButtonPressed(Input.MB_RIGHT)) {
                Vector2 pos = orthoBox.toWorldSpace(input().getXY());
                createSphere(pos);
                saveSphere(pos);
            }
            save();
        }
        
    }
    
}
