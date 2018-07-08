package ax.engine.core.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ax.engine.core.Material;

public class ShaderMaterial extends Material {

    HashMap<String,Object> uniformMap = new HashMap<>();
    
    //Shaders are deleted by MaterialProgramMapper on glProgram creation
    protected String vertexShader;
    protected String geometryShader;
    protected String fragmentShader;
    
    protected int id;
    private static int materialCount = 0;
    
    ShaderMaterial() {
        id = materialCount++;
    }
    
    public ShaderMaterial(String vert, String frag) {
        this();
        vertexShader = vert;
        fragmentShader = frag;
    }
    
    public ShaderMaterial(String vert, String geom, String frag) {
        this();
        vertexShader = vert;
        geometryShader = geom;
        fragmentShader = frag;
    }

    public void setUniform(String name, Object uniform) {
        uniformMap.put(name, uniform);
    }
    
    public Object getUniform(String name) {
        return uniformMap.get(name);
    }

    @Override
    protected List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        for (Entry<String,Object> e : uniformMap.entrySet()) {
            uniforms.add(new Uniform(e.getKey(), e.getValue()));
        }
        return uniforms;
    }

    @Override
    protected String shaderKey() {
        return "_shader_"+id;
    }

	@Override
	protected String vertexShader(int version) {
		return vertexShader;
	}

	@Override
	protected String geometryShader(int version) {
		return geometryShader;
	}

	@Override
	protected String fragmentShader(int version) {
		return fragmentShader;
	}
    
    
    
}
