package jonl.ge.core.editor;

import java.util.ArrayList;

import jonl.ge.core.Camera;
import jonl.ge.core.Editor;
import jonl.ge.core.GameObject;
import jonl.ge.core.Geometry;
import jonl.ge.core.Mesh;
import jonl.ge.core.Mesh.Mode;
import jonl.ge.core.Scene;
import jonl.ge.core.geometry.GeometryBuilder;
import jonl.ge.core.material.SolidMaterial;
import jonl.ge.mod.misc.CameraControl;
import jonl.ge.mod.misc.PerspectiveUpdate;
import jonl.vmath.Color;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class EditorScene {

    EditorCore core;
    Editor editor;
    
    public Scene scene;
    
    public Camera camera;
    
    public Vector4 background = new Vector4(0.5f,0.5f,0.55f,1f);
    
    Geometry gridGeometry;
    
    int gridSize = 5;
    float gridScale = 1f;
    
    public EditorScene(EditorCore core, Editor editor) {
        this.core = core;
        this.editor = editor;
    }
    
    public void create() {
        scene = new Scene();
        
        GameObject control = EditorAssets.control();
        
        camera  = new Camera();
        camera.setClearColor(background);
        control.addComponent(camera);
        
        PerspectiveUpdate pu = new PerspectiveUpdate();
        control.addComponent(pu);
        
        CameraControl cc = new CameraControl();
        control.addComponent(cc);
        control.addCreate(()->{
            cc.lookAt(new Vector3(0,0,0));
        });
        
        control.transform().translation.set(5,5,5);
        
        
        GameObject b = EditorAssets.cube();
        scene.add(b);
        
        GameObject grid = grid();
        scene.add(grid);
        
        scene.add(control);
        
    }
    
    public GameObject grid() {
        
        GameObject go = new GameObject();
        
        GeometryBuilder gb = new GeometryBuilder();
        
        gb.addVertex(new Vector3(0,0,0));
        gb.addVertex(new Vector3(0,0,10));
        
        
        
        gridGeometry = new Geometry();
        gridGeometry.setVectorArray(gridVerts());
        
        SolidMaterial material = new SolidMaterial(Color.fromColor(Color.WHITE,0.5f).toVector());
        Mesh mesh = new Mesh(gridGeometry,material);
        mesh.setMode(Mode.LINES);
        mesh.setThickness(1f);
        
        go.addComponent(mesh);
        
        return go;
        
    }
    
    void updateGrid() {
        gridGeometry.setVectorArray(gridVerts());
    }
    
    Vector3[] gridVerts() {
        if (gridSize==0 || gridScale<=0) {
            return new Vector3[] {};
        }
        
        ArrayList<Vector3> grid = new ArrayList<>();
        
        float sx = - gridScale * gridSize;
        float sy = - gridScale * gridSize;
        
        int lines = gridSize * 2 + 1;
        for (int i=0; i<lines; i++) {
            float d = i * gridScale;
            grid.add(new Vector3(sx+d,0,sy));
            grid.add(new Vector3(sx+d,0,-sy));
            
            grid.add(new Vector3(sx,0,sy+d));
            grid.add(new Vector3(-sx,0,sy+d));
        }
        
        return grid.toArray(new Vector3[grid.size()]);
    }
    
    
    
}
