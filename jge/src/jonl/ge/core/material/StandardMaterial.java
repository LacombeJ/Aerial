package jonl.ge.core.material;

import jonl.ge.core.material.ShaderLanguage.SLVec3;

public class StandardMaterial extends GeneratedShader {

	public SLVec3   diffuse     = null;
    public SLVec3   specular    = null;
    public SLVec3   normal      = null;
	
	StandardMaterial(int _construct) {
		
	}
	
	//TODO make default construct public (look at GeneratedMaterial) and it's apply function
	StandardMaterial() {
		
	}
	
	

}
