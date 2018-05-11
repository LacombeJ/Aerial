package jonl.ge.editor.spline;

import jonl.ge.core.Camera;
import jonl.ge.core.GameObject;
import jonl.ge.core.Input;
import jonl.ge.core.Mesh;
import jonl.ge.core.Property;
import jonl.ge.core.Scene;
import jonl.ge.core.geometry.SphereGeometry;
import jonl.ge.core.material.SolidMaterial;
import jonl.ge.mod.misc.OrthoBox;
import jonl.ge.mod.misc.PerspectiveUpdate;
import jonl.vmath.Color;
import jonl.vmath.Vector2;

public class SplineScene {

    Scene scene;
    
    SplineScene() {
        
        scene = new Scene();
        
        GameObject control = control();

        GameObject sphere = sphere();
        
        scene.add(control);
        scene.add(sphere);
        
    }
    
    GameObject control() {
        GameObject go = new GameObject();
        
        Camera camera = new Camera();
        camera.setOrthographic(10,1,-1,1);
        camera.setClearColor(Color.LIGHT_GRAY.toVector());
        
        go.addComponent(camera);
        go.addComponent(new PerspectiveUpdate(camera));
        go.addComponent(new Control());
        go.addComponent(new OrthoBox());
        
        return go;
    }
    
    GameObject sphere() {
        GameObject go = new GameObject();
        
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
                createSphere(orthoBox.toWorldSpace(input().getXY()));
            }
        }
        
        void createSphere(Vector2 pos) {
            GameObject sphere = sphere();
            sphere.transform().translation.set(pos.x, pos.y, 0);
            scene().add(sphere);
        }
        
    }
    
}
