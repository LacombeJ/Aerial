package jonl.ge.core;

import jonl.ge.core.ShaderLanguage.SLVec3;

public class StandardMaterial extends GeneratedShader {

	public SLVec3   diffuse     = null;
    public SLVec3   specular    = null;
    public SLVec3   normal      = null;
	
	StandardMaterial(Construct construct) {
		
	}
	
	//TODO make default construct public (look at GeneratedMaterial) and it's apply function
	StandardMaterial() {
		
	}
	
	

}
