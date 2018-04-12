package jonl.ge.core.editor;

import java.util.ArrayList;

import jonl.ge.core.Camera;
import jonl.ge.core.Editor;
import jonl.ge.core.GameObject;
import jonl.ge.core.Geometry;
import jonl.ge.core.Material;
import jonl.ge.core.Mesh;
import jonl.ge.core.Mesh.Mode;
import jonl.ge.core.Property;
import jonl.ge.core.Scene;
import jonl.ge.core.geometry.BoxGeometry;
import jonl.ge.core.geometry.ConeGeometry;
import jonl.ge.core.geometry.GeometryBuilder;
import jonl.ge.core.geometry.GeometryOperation;
import jonl.ge.core.geometry.SphereGeometry;
import jonl.ge.core.material.GeneratedMaterial;
import jonl.ge.core.material.SolidMaterial;
import jonl.ge.mod.axis.TranslationAxis;
import jonl.ge.mod.misc.CameraControl;
import jonl.ge.mod.misc.PerspectiveUpdate;
import jonl.ge.mod.misc.RayHit;
import jonl.ge.mod.misc.ScreenRayTracer;
import jonl.ge.mod.misc.ScreenSpaceScale;
import jonl.jutils.io.Console;
import jonl.vmath.Color;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class EditorScene {

    EditorCore core;
    Editor editor;
    
    public Scene scene;
    public Scene overlayScene;
    
    public Camera camera;
    public Camera overlayCamera;
    
    public Vector4 background = new Vector4(0.5f,0.5f,0.55f,1f);
    
    Geometry gridGeometry;
    
    int gridSize = 5;
    float gridScale = 1f;
    
    TranslationAxis translationAxis;
    
    public EditorScene(EditorCore core, Editor editor) {
        this.core = core;
        this.editor = editor;
    }
    
    public void create() {
        scene = new Scene();
        overlayScene = new Scene();
        
        prepareScene();
        prepareOverlayScene();
        
        createScene();
        createOverlayScene();
        
    }
    
    private void prepareScene() {
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
        
        scene.add(control);
    }
    
    private void prepareOverlayScene() {
        GameObject overlay = new GameObject();
        
        overlayCamera = new Camera();
        overlayCamera.enableClearColor(false);
        overlayCamera.setOrthographicBox(1,1,-1,1);
        overlay.transform().translation.set(0.5f,0.5f,0); // To move to unit screen space (0,1)
        
        overlay.addComponent(overlayCamera);
        
        overlayScene.add(overlay);
    }
    
    private void createScene() {
        GameObject sphere = sphere();
        scene.add(sphere);
        
        GameObject grid = grid();
        scene.add(grid);
        
        translationAxis = new TranslationAxis(camera);
        scene.add(translationAxis.get());
        translationAxis.get().addUpdate(()->{
            sphere.transform().translation.set(translationAxis.get().transform().translation);
        });
    }
    
    private void createOverlayScene() {
        overlayScene.add(translationAxis.overlay());
    }
    
    public GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        Geometry geometry = new BoxGeometry();
        Material material = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,material);
        
        cube.addComponent(mesh);

        return cube;
    }
    
    public GameObject sphere() {
        GameObject cube = new GameObject();
        cube.setName("Sphere");
        Geometry geometry = new SphereGeometry();
        Material material = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,material);
        
        cube.addComponent(mesh);
        
        cube.transform().translation.set(1,0,2);

        return cube;
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
