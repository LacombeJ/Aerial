package jonl.ge;

public class MeshRenderer extends Component {

    public boolean cullFace = true;
    public boolean castShadows = true;
    public boolean recieveShadows = false;
    public boolean recieveLight = true;
    
    Mesh mesh; //TODO hide variables, getters and setters?
    Material material = null;
    Mode mode = Mode.TRIANGLES;
    
    public enum Mode {
        TRIANGLES(jonl.jgl.GraphicsLibrary.Mode.TRIANGLES),
        LINES(jonl.jgl.GraphicsLibrary.Mode.LINES),
        POINTS(jonl.jgl.GraphicsLibrary.Mode.POINTS);
        
        final jonl.jgl.GraphicsLibrary.Mode mode;
        
        Mode(jonl.jgl.GraphicsLibrary.Mode mode) {
            this.mode = mode;
        }
    }
    
    public MeshRenderer(Mesh mesh, Material material) {
        this.mesh = mesh;
        this.material = material;
    }
    
    public Mesh getMesh() {
        return mesh;
    }
    
    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material mat) {
        material = mat;
    }
    
    public Mode getMode() {
        return mode;
    }
    
    public void setMode(Mode mode) {
        this.mode = mode;
    }

}
