package jonl.ge.core.material;

import jonl.ge.core.shaders.SLUtils;

/**
 * This material builder extends the fragment shader
 * @author Jonathan
 *
 */
public class BasicMaterialBuilder extends ShaderLanguage {
	
	private BasicMaterial build = null;

    public BasicMaterialBuilder() {
		super();
	}
    
    // Build only occurs once
    public BasicMaterial build() {
        
    	BasicMaterial mat = new BasicMaterial(0);

        ShaderLanguage vs = SLUtils.normalVert();
        ShaderLanguage fs = this;
        
        mat.vertexShader = vs.shader();
        mat.fragmentShader = fs.shader();
        mat.add(vs);
        mat.add(fs);
        
        build = mat;
        
        return build;
    }

}
