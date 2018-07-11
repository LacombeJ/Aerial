package ax.engine.core;

import ax.graphics.GL;

public class Mesh extends Component {

    public boolean cullFace = true;
    public boolean castShadows = true;
    public boolean recieveShadows = false;
    public boolean recieveLight = true;
    
    Geometry geometry; //TODO hide variables, getters and setters?
    Material material = null;
    GL.Mode mode;
    float thickness = 1;
    boolean depthTest = true;
    boolean visible = true;
    boolean wireframe = false;
    
    public Mesh(Geometry mesh, Material material, GL.Mode mode) {
        this.geometry = mesh;
        this.material = material;
        this.mode = mode;
    }
    
    public Mesh(Geometry mesh, Material material) {
        this(mesh, material, GL.TRIANGLES);
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
    
    public GL.Mode getMode() {
        return mode;
    }
    
    public void setMode(GL.Mode mode) {
        this.mode = mode;
    }
    
    public boolean isWireframe() {
    	return wireframe;
    }
    
    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }
    
    public float getThickness() {
        return thickness;
    }
    
    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public boolean getDepthTest() {
        return depthTest;
    }
    
    public void setDepthTest(boolean enable) {
        depthTest = enable;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean enable) {
        visible = enable;
    }
    
}
