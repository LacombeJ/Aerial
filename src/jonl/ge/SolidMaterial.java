package jonl.ge;

import java.util.ArrayList;
import java.util.List;
import jonl.vmath.Vector4;

public class SolidMaterial extends Material {

    private Vector4 color = new Vector4(1,1,1,1);
    
    public SolidMaterial() {
        
    }
    
    public SolidMaterial(Vector4 color) {
       this.color.set(color);
    }
    
    public SolidMaterial(float r, float g, float b, float a) {
        color.set(r, g, b, a);
    }

    public Vector4 getColor() {
        return color.get();
    }
    
    public void setColor(Vector4 v) {
        color.set(v);
    }

    @Override
    List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        uniforms.add(new Uniform("color",color));
        return uniforms;
    }

    @Override
    String shaderKey() {
        return "_solid_";
    }
    
    
    
}
