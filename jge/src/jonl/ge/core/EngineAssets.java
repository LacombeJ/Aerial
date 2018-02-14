package jonl.ge.core;

import jonl.ge.utils.PresetData;

public class EngineAssets {
    
    //TODO remove these meshes with generated Geometry
    
    /* Mesh Assets */
    public final Geometry RECT_MESH = Loader.loadMesh(PresetData.rectMesh());
    public final Geometry PLANE_MESH = Loader.loadMesh(PresetData.planeMesh());
    public final Geometry CUBE_MESH = Loader.loadMesh(PresetData.cubeMesh());
    public final Geometry SPHERE_MESH = Loader.loadMesh(PresetData.sphereMesh());
    public final Geometry CONE_MESH = Loader.loadMesh(PresetData.coneMesh());
    public final Geometry[] MESHS = {
        RECT_MESH,
        PLANE_MESH,
        CUBE_MESH,
        SPHERE_MESH,
        CONE_MESH
    };
    
    /* Font Assets */
    public final Font FONT_CONSOLAS = new Font("Consolas",Font.PLAIN,24,false);
    public final Font[] FONTS = {
        FONT_CONSOLAS
    };
    
    /* ***** GameObject Assets ***** */
    
    /* Mesh Assets */
    public GameObject rect() {
        GameObject rect = new GameObject();
        rect.setName("Rect");
        Geometry geometry = RECT_MESH;
        Material mat = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,mat);
        rect.addComponent(mesh);
        return rect;
    }
    
    public GameObject plane() {
        GameObject plane = new GameObject();
        plane.setName("Plane");
        Geometry geometry = PLANE_MESH;
        Material mat = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,mat);
        plane.addComponent(mesh);
        return plane;
    }
    
    public GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        Geometry geometry = CUBE_MESH;
        Material mat = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,mat);
        cube.addComponent(mesh);
        return cube;
    }
    
    public GameObject sphere() {
        GameObject sphere = new GameObject();
        sphere.setName("Sphere");
        Geometry geometry = SPHERE_MESH;
        Material mat = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,mat);
        sphere.addComponent(mesh);
        return sphere;
    }
    
    public GameObject cone() {
        GameObject cone = new GameObject();
        cone.setName("Cone");
        Geometry geometry = CONE_MESH;
        Material mat = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,mat);
        cone.addComponent(mesh);
        return cone;
    }
    
    /* Component Assets */
    public GameObject pointLight() {
        GameObject pointLight = new GameObject();
        pointLight.setName("PointLight");
        Light light = new Light();
        light.type = Light.POINT;
        pointLight.addComponent(light);
        return pointLight;
    }
    
    public GameObject text() {
        GameObject gameObject = new GameObject();
        gameObject.setName("Text");
        Text text = new Text();
        CanvasRenderer cr = new CanvasRenderer();
        gameObject.addComponent(text);
        gameObject.addComponent(cr);
        return gameObject;
    }
    
    /* ***** ***************** ***** */
    
    
}
