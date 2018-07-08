package ax.std.render;

import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLVec3;

public class DeferredMaterial extends GeneratedShader {

	public SLVec3   diffuse     = null;
    public SLVec3   specular    = null;
    public SLVec3   normal      = null;
	
	DeferredMaterial(int _construct) {
		
	}
	
	//TODO make default construct public (look at GeneratedMaterial) and it's apply function
	DeferredMaterial() {
		
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
	
	void vertexShader(String vs) {
	    this.vertexShader = vs;
	}
	
	void fragmentShader(String fs) {
	    this.fragmentShader = fs;
	}
	
	void addShader(ShaderLanguage sl) {
	    this.add(sl);
	}
	
	@Override
    protected String shaderKey() {
        return "_shader_"+id;
    }

}
