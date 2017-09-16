package jonl.ge;

import jonl.ge.utils.PresetData;

public class EngineAssets {
    
    //TODO remove these meshes with generated Geometry
    
    /* Mesh Assets */
    public final Mesh RECT_MESH = Loader.loadMesh(PresetData.rectMesh());
    public final Mesh PLANE_MESH = Loader.loadMesh(PresetData.planeMesh());
    public final Mesh CUBE_MESH = Loader.loadMesh(PresetData.cubeMesh());
    public final Mesh SPHERE_MESH = Loader.loadMesh(PresetData.sphereMesh());
    public final Mesh CONE_MESH = Loader.loadMesh(PresetData.coneMesh());
    public final Mesh[] MESHS = {
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
        Mesh mesh = RECT_MESH;
        Material mat = new GeneratedMaterial();
        MeshRenderer cubeRenderer = new MeshRenderer(mesh,mat);
        rect.addComponent(cubeRenderer);
        return rect;
    }
    
    public GameObject plane() {
        GameObject plane = new GameObject();
        plane.setName("Plane");
        Mesh mesh = PLANE_MESH;
        Material mat = new GeneratedMaterial();
        MeshRenderer cubeRenderer = new MeshRenderer(mesh,mat);
        plane.addComponent(cubeRenderer);
        return plane;
    }
    
    public GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        Mesh mesh = CUBE_MESH;
        Material mat = new GeneratedMaterial();
        MeshRenderer cubeRenderer = new MeshRenderer(mesh,mat);
        cube.addComponent(cubeRenderer);
        return cube;
    }
    
    public GameObject sphere() {
        GameObject sphere = new GameObject();
        sphere.setName("Sphere");
        Mesh mesh = SPHERE_MESH;
        Material mat = new GeneratedMaterial();
        MeshRenderer sphereRenderer = new MeshRenderer(mesh,mat);
        sphere.addComponent(sphereRenderer);
        return sphere;
    }
    
    public GameObject cone() {
        GameObject cone = new GameObject();
        cone.setName("Cone");
        Mesh mesh = CONE_MESH;
        Material mat = new GeneratedMaterial();
        MeshRenderer coneRenderer = new MeshRenderer(mesh,mat);
        cone.addComponent(coneRenderer);
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
