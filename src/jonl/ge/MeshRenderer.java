package jonl.ge;

public class MeshRenderer extends Component {

    public Mesh mesh; //TODO hide variables, getters and setters?
    public boolean castShadows = true;
    public boolean recieveShadows = false;
    Material material = EngineAssets.DEFAULT_MATERIAL;
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
