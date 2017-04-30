package jonl.ge;

public class EngineAssets {
    
    /* Mesh Assets */
    public final static Mesh RECT_MESH = Loader.loadMesh("res/models/rect.mesh");
    public final static Mesh PLANE_MESH = Loader.loadMesh("res/models/plane.mesh");
    public final static Mesh CUBE_MESH = Loader.loadMesh("res/models/cube2.mesh");
    public final static Mesh SPHERE_MESH = Loader.loadMesh("res/models/sphere.mesh");
    public final static Mesh CONE_MESH = Loader.loadMesh("res/models/cone.mesh");
    public final static Mesh[] MESHS = {
        RECT_MESH,
        PLANE_MESH,
        CUBE_MESH,
        SPHERE_MESH,
        CONE_MESH
    };
    
    /* Material Assets */
    public final static Material DEFAULT_MATERIAL;
    static {
        MaterialBuilder mb = new MaterialBuilder();
        mb.diffuse = mb.vec3u("diffuse",0.5f,0.5f,0.5f);
        mb.specular = mb.vec3u("specular",0.5f,0.5f,0.5f);
        mb.roughness = mb.mbFloatu("roughness",0.8f);
        mb.fresnel = mb.mbFloatu("fresnel",0.3f);
        DEFAULT_MATERIAL = mb.build();
    }
    
    /* Font Assets */
    public final static Font FONT_CONSOLAS = new Font("Consolas",Font.PLAIN,24,false);
    public final static Font[] FONTS = {
        FONT_CONSOLAS
    };
    
    /* ***** GameObject Assets ***** */
    
    /* Mesh Assets */
    public static GameObject rect() {
        GameObject rect = new GameObject();
        rect.setName("Rect");
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = EngineAssets.RECT_MESH;
        rect.addComponent(cubeRenderer);
        return rect;
    }
    
    public static GameObject plane() {
        GameObject plane = new GameObject();
        plane.setName("Plane");
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = EngineAssets.PLANE_MESH;
        plane.addComponent(cubeRenderer);
        return plane;
    }
    
    public static GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = EngineAssets.CUBE_MESH;
        cube.addComponent(cubeRenderer);
        return cube;
    }
    
    public static GameObject sphere() {
        GameObject sphere = new GameObject();
        sphere.setName("Sphere");
        MeshRenderer sphereRenderer = new MeshRenderer();
        sphereRenderer.mesh = EngineAssets.SPHERE_MESH;
        sphere.addComponent(sphereRenderer);
        return sphere;
    }
    
    public static GameObject cone() {
        GameObject cone = new GameObject();
        cone.setName("Cone");
        MeshRenderer coneRenderer = new MeshRenderer();
        coneRenderer.mesh = EngineAssets.CONE_MESH;
        cone.addComponent(coneRenderer);
        return cone;
    }
    
    /* Component Assets */
    public static GameObject pointLight() {
        GameObject pointLight = new GameObject();
        pointLight.setName("PointLight");
        Light light = new Light();
        light.type = Light.POINT;
        pointLight.addComponent(light);
        return pointLight;
    }
    
    public static GameObject text() {
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
