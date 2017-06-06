package jonl.ge;

public class EngineAssets {
    
    public final Material DEFAULT_MATERIAL;
    
    public EngineAssets() {
        DEFAULT_MATERIAL = defaultMaterial();
    }
    
    private Material defaultMaterial() {
        MaterialBuilder mb = new MaterialBuilder();
        mb.diffuse = mb.vec3u("diffuse",0.5f,0.5f,0.5f);
        mb.specular = mb.vec3u("specular",0.5f,0.5f,0.5f);
        mb.roughness = mb.mbFloatu("roughness",0.8f);
        mb.fresnel = mb.mbFloatu("fresnel",0.3f);
        return mb.build();
    }
    
    /* Mesh Assets */
    //TODO Hardcode mesh data?
    public final Mesh RECT_MESH = Loader.loadMesh("res/models/rect.mesh");
    public final Mesh PLANE_MESH = Loader.loadMesh("res/models/plane.mesh");
    public final Mesh CUBE_MESH = Loader.loadMesh("res/models/cube2.mesh");
    public final Mesh SPHERE_MESH = Loader.loadMesh("res/models/sphere.mesh");
    public final Mesh CONE_MESH = Loader.loadMesh("res/models/cone.mesh");
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
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = RECT_MESH;
        rect.addComponent(cubeRenderer);
        return rect;
    }
    
    public GameObject plane() {
        GameObject plane = new GameObject();
        plane.setName("Plane");
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = PLANE_MESH;
        plane.addComponent(cubeRenderer);
        return plane;
    }
    
    public GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = CUBE_MESH;
        cube.addComponent(cubeRenderer);
        return cube;
    }
    
    public GameObject sphere() {
        GameObject sphere = new GameObject();
        sphere.setName("Sphere");
        MeshRenderer sphereRenderer = new MeshRenderer();
        sphereRenderer.mesh = SPHERE_MESH;
        sphere.addComponent(sphereRenderer);
        return sphere;
    }
    
    public GameObject cone() {
        GameObject cone = new GameObject();
        cone.setName("Cone");
        MeshRenderer coneRenderer = new MeshRenderer();
        coneRenderer.mesh = CONE_MESH;
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
