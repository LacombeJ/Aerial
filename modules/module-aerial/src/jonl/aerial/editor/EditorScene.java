package jonl.aerial.editor;

public class EditorScene {
    /*
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
        
        translationAxis = new TranslationAxis(camera);
        
        control.addComponent(new Intersector());
        control.addComponent(new Selector());
        
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
        
        GameObject cube = cube();
        scene.add(cube);
        
        GameObject grid = grid();
        scene.add(grid);
        
        
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
        cube.addComponent(new RayComponent());

        return cube;
    }
    
    public GameObject sphere() {
        GameObject sphere = new GameObject();
        sphere.setName("Sphere");
        Geometry geometry = new SphereGeometry();
        Material material = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,material);
        
        sphere.addComponent(mesh);
        sphere.addComponent(new RayComponent());
        
        sphere.transform().translation.set(1,0,2);

        return sphere;
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
    
    class Intersector extends Property {

        Selector selector;
        RayComponents rayComponents;
        
        @Override
        public void create() {
            rayComponents = scene().data().getAsType("ray-components",RayComponents.class);
            selector = getComponent(Selector.class);
        }

        @Override
        public void update() {
            if (input().isButtonPressed(Input.MB_LEFT)) {
                Vector2 mouse = window().toUnitSpace(input().getXY());
                ScreenRayTracer srt = new ScreenRayTracer(camera,mouse.x,mouse.y);
                
                RayComponentHit hit = rayComponents.hit(srt.ray());
                if (hit != null) {
                    if (!translationAxis.isHovered()) {
                        RayComponent component = hit.component();
                        GameObject object = component.gameObject();
                        selector.select(object);
                    }
                }
            }
        }
        
    }
    
    class Selector extends Property {

        GameObject selected;
        
        @Override
        public void create() {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void update() {
            if (selected!=null) {
                selected.transform().translation.set(translationAxis.get().transform().translation);
            }
        }
        
        public void select(GameObject go) {
            if (selected!=go) {
                if (selected!=null) {
                    scene().remove(translationAxis.get());
                }
                scene().add(translationAxis.get());
                translationAxis.get().transform().set(go.transform());
                selected = go;
            }
        }
        
    }
    */
    
}
