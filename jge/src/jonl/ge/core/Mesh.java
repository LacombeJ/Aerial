package jonl.ge.core;

public class Mesh extends Component {

    public boolean cullFace = true;
    public boolean castShadows = true;
    public boolean recieveShadows = false;
    public boolean recieveLight = true;
    
    Geometry geometry; //TODO hide variables, getters and setters?
    Material material = null;
    Mode mode = Mode.TRIANGLES;
    boolean wireframe = false;
    
    public enum Mode {
        TRIANGLES,
        LINES,
        LINE_STRIP,
        POINTS;
    }
    
    public Mesh(Geometry mesh, Material material) {
        this.geometry = mesh;
        this.material = material;
    }
    
    public Geometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(Geometry mesh) {
        this.geometry = mesh;
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
    
    public boolean isWireframe() {
    	return wireframe;
    }
    
    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

}
