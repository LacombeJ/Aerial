package ax.std.render;

import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLVec3;

public class StandardMaterial extends GeneratedShader {

	public SLVec3   diffuse     = null;
    public SLVec3   specular    = null;
    public SLVec3   normal      = null;
    
    boolean deferred = false;
    
    String vertexShaderDeferred;
    String geometryShaderDeferred;
    String fragmentShaderDeferred;
	
	StandardMaterial(int _construct) {
		
	}
	
	//TODO make default construct public (look at GeneratedMaterial) and it's apply function
	StandardMaterial() {
		
	}
	
	@Override
    protected String vertexShader(int version) {
        return (deferred) ? vertexShaderDeferred : vertexShader;
    }

    @Override
    protected String geometryShader(int version) {
        return (deferred) ? geometryShaderDeferred : geometryShader;
    }

    @Override
    protected String fragmentShader(int version) {
        return (deferred) ? fragmentShaderDeferred : fragmentShader;
    }
	
	void vertexShader(String vs) {
	    this.vertexShader = vs;
	}
	
	void fragmentShader(String fs) {
	    this.fragmentShader = fs;
	}
	
	void vertexShaderDeferred(String vs) {
        this.vertexShaderDeferred = vs;
    }
    
    void fragmentShaderDeferred(String fs) {
        this.fragmentShaderDeferred = fs;
    }
	
	void addShader(ShaderLanguage sl) {
	    this.add(sl);
	}
	
	@Override
    protected String shaderKey() {
	    if (deferred) {
	        return "_shader_"+id+"_deferred";
	    }
        return "_shader_"+id;
    }
	

}
