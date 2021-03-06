package ax.engine.core.material;

import java.util.ArrayList;
import java.util.List;

import ax.engine.core.Material;
import ax.engine.utils.PresetData;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

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
    
    public SolidMaterial(Vector3 color) {
        this.color.set(color.x,color.y,color.z,1);
    }

    public SolidMaterial(float r, float g, float b) {
        color.set(r, g, b, 1);
    }

    public Vector4 getColor() {
        return color.get();
    }
    
    public void setColor(Vector4 v) {
        color.set(v);
    }
    
    public void setColor(Vector3 v) {
        color.set(v.x,v.y,v.z,1);
    }
    
    @Override
    public void setUniform(String name, Object object) {
        if (name=="color") {
            color = (Vector4) object;
        }
    }

    @Override
    protected List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        uniforms.add(new Uniform("color",color));
        return uniforms;
    }

    @Override
    protected String shaderKey() {
        return "_solid_";
    }

	@Override
	protected String vertexShader(int version) {
		return PresetData.solidVSSource(version);
	}

	@Override
	protected String geometryShader(int version) {
		return null;
	}

	@Override
	protected String fragmentShader(int version) {
		return PresetData.solidFSSource(version);
	}
    
    
}
