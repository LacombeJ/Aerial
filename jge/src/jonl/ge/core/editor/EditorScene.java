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
        
        AxisHandler ah = new AxisHandler(camera);
        control.addComponent(ah);
        
        //scene.add(cube());
        scene.add(sphere());
        
        GameObject grid = grid();
        scene.add(grid);
        
        GameObject translateAxis = translateAxis();
        scene.add(translateAxis);
        
        scene.add(control);
        
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
    
    public GameObject translateAxis() {
        
        GameObject go = new GameObject();
        
        ScreenSpaceScale sss = new ScreenSpaceScale(camera);
        go.addComponent(sss);
        
        GameObject xAxis = axis("_xaxis",new Vector3(1,0,0));
        GameObject yAxis = axis("_yaxis",new Vector3(0,1,0));
        GameObject zAxis = axis("_zaxis",new Vector3(0,0,1));
        
        go.addChild(xAxis);
        go.addChild(yAxis);
        go.addChild(zAxis);
        
        return go;
        
    }
    
    public GameObject axis(String name, Vector3 axis) {
        
        GameObject go = new GameObject();
        go.setName(name);
        
        SolidMaterial material = new SolidMaterial(new Vector4(axis,1f));
        
        // Some geometry operations
        GeometryOperation scaleOp = GeometryOperation.transform(Matrix4.scaled(0.1f,0.2f,0.1f));
        GeometryOperation translateOp = GeometryOperation.transform(Matrix4.translation(0,1,0));
        GeometryOperation rotateOp = null;
        if (axis.equals(new Vector3(0,0,1))) {
            rotateOp = GeometryOperation.transform(Matrix4.rotation(Mathf.PI_OVER_2,0,0));
        } else if (axis.equals(new Vector3(1,0,0))) {
            //rotateOp = GeometryOperation.transform(Matrix4.rotation(axis.get().scale(Mathf.PI_OVER_2)));
            rotateOp = GeometryOperation.transform(Matrix4.rotation(0,0,-Mathf.PI_OVER_2));
        }
        
        // Line
        GameObject goLine = new GameObject();
        GeometryBuilder gb = new GeometryBuilder();
        gb.addVertex(new Vector3(0,0,0));
        gb.addVertex(new Vector3(0,1,0));
        Geometry lineGeometry = gb.build();
        if (rotateOp!=null) {
            lineGeometry.modify(rotateOp);
        }
        Mesh lineMesh = new Mesh(lineGeometry,material);
        lineMesh.setMode(Mode.LINES);
        lineMesh.setThickness(3f);
        lineMesh.setDepthTest(false);
        goLine.addComponent(lineMesh);
        
        // Arrow / Cone
        GameObject goArrow = new GameObject();
        ConeGeometry arrow = new ConeGeometry();
        arrow.modify(scaleOp);
        arrow.modify(translateOp);
        if (rotateOp!=null) {
            arrow.modify(rotateOp);
        }
        Mesh arrowMesh = new Mesh(arrow,material);
        arrowMesh.setDepthTest(false);
        goArrow.addComponent(arrowMesh);
        
        go.addChild(goArrow);
        go.addChild(goLine);
        
        return go;
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
    
    static class AxisHandler extends Property {

        Camera camera;
        GameObject xaxis;
        GameObject yaxis;
        GameObject zaxis;
        
        Vector3 highlight = Color.YELLOW.toVector().xyz();
        
        AxisHandler(Camera camera) {
            this.camera = camera;
        }
        
        @Override
        public void create() {
            xaxis = findGameObject("_xaxis");
            yaxis = findGameObject("_yaxis");
            zaxis = findGameObject("_zaxis");
        }

        @Override
        public void update() {
            Vector2 mouse = window().toUnitSpace(input().getXY());
            
            ScreenRayTracer srt = new ScreenRayTracer(camera,mouse.x,mouse.y);
            
            float scale = xaxis.parent().transform().scale.x;
            
            float r = 0.1f;
            float l = 1f;
            
            RayHit x = null;
            RayHit y = null;
            RayHit z = null;
            
            {
                Vector3 min = new Vector3(0,-r,-r).scale(scale);
                Vector3 max = new Vector3(l,r,r).scale(scale);
                x = srt.boundingBox(min,max);
                
                if (x!=null) {
                    setColor(xaxis,highlight);
                } else {
                    setColor(xaxis,new Vector3(1,0,0));
                }
            }
            {
                Vector3 min = new Vector3(-r,0,-r).scale(scale);
                Vector3 max = new Vector3(r,1,r).scale(scale);
                y = srt.boundingBox(min,max);
            }
            {
                Vector3 min = new Vector3(-r,-r,0).scale(scale);
                Vector3 max = new Vector3(r,r,1).scale(scale);
                z = srt.boundingBox(min,max);
            }
            
            setColor(xaxis,new Vector3(1,0,0));
            setColor(yaxis,new Vector3(0,1,0));
            setColor(zaxis,new Vector3(0,0,1));
            
            String hit = hit(x,y,z);
            if (hit!=null) {
                if (hit.equals("x")) {
                    setColor(xaxis,highlight);
                } else if (hit.equals("y")) {
                    setColor(yaxis,highlight);
                } else if (hit.equals("z")) {
                    setColor(zaxis,highlight);
                } 
            }
            
        }
        
        private String hit(RayHit x, RayHit y, RayHit z) {
            float shortest = Float.MAX_VALUE;
            String hit = "";
            if (x!=null) {
                if (x.distance()<shortest) {
                    hit = "x";
                    shortest = x.distance();
                }
            }
            if (y!=null) {
                if (y.distance()<shortest) {
                    hit = "y";
                    shortest = y.distance();
                }
            }
            if (z!=null) {
                if (z.distance()<shortest) {
                    hit = "z";
                    shortest = z.distance();
                }
            }
            if (shortest!=Float.MAX_VALUE) {
                return hit;
            }
            return null;
        }
        
        private void setColor(GameObject axis, Vector3 color) {
            GameObject line = axis.getChildAt(0);
            Mesh mesh = line.getComponent(Mesh.class);
            SolidMaterial material = mesh.getMaterial().asSolidMaterial();
            material.setColor(color);
        }
        
    }
    
}
