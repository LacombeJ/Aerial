package jonl.ge.mod.render;

import jonl.ge.core.shaders.SLUtils;
import jonl.ge.core.material.ShaderLanguage;
import jonl.ge.core.material.StandardMaterialUtil;
import jonl.ge.core.material.ShaderLanguage.SLVec2;
import jonl.ge.core.shaders.SLImports.GLSLGamma;

/**
 * This material builder extends the fragment shader
 * @author Jonathan
 *
 */
public class DeferredMaterialBuilder extends ShaderLanguage {
	
	private DeferredMaterial build = null;
	
	GLSLGamma glslGamma;
	SLVec3 vPosition;
    SLVec3 vNormal;
    SLVec2 vTexCoord;
    SLMat3 mTBN;
    SLVec3 eye;
    SLInt numLights;
    SLFunc<SLVec4> textureLinear;
	
	public SLVec3   diffuse     = null;
    public SLVec3   specular    = null;
    public SLVec3   normal      = null;
    
    public DeferredMaterialBuilder() {
		super();
		prepare();
	}
    
    // Build only occurs once
    public DeferredMaterial build() {
    	
    	//TODO find a better way to do this (multiple shaders with different build calls)
    	if (build == null) {
    	    finish();
    	}    	
    	
        DeferredMaterial mat = new DeferredMaterial(0);
        
        mat.diffuse = copyData(diffuse);
        mat.specular = copyData(specular);
        mat.normal = copyData(normal);
        
        ShaderLanguage vs = SLUtils.normalVert();
        ShaderLanguage fs = this;
        
        mat.vertexShader(vs.shader());
        mat.fragmentShader(fs.shader());
        mat.addShader(vs);
        mat.addShader(fs);
        
        build = mat;
        
        return build;
    }
    
    private void prepare() {
    	
    	ShaderLanguage sl = this;
		
    	glslGamma = sl.include(new GLSLGamma());
    	
    	SLTexU gPosition = sl.texture("gPosition");
    	SLTexU gNormal = sl.texture("gNormal");
    	SLTexU gTexCoord = sl.texture("gTexCoord");
    	
    	SLVec2 texCoordOrig = sl.attributeIn(SLVec2.class, "vTexCoord");
    	SLVec2 texCoord = sl.vec2(texCoordOrig.x(), sl.sub(1,texCoordOrig.y()));
    	
    	vPosition = sl.sample(gPosition, texCoord).xyz();
        vNormal = sl.sample(gNormal, texCoord).xyz();
        vTexCoord = sl.sample(gTexCoord, texCoord).xy();
        
        vNormal = sl.normalize(vNormal);
        
        mTBN = sl.attributeIn(SLMat3.class, "mTBN");
        
        numLights = sl.slIntu("numLights");
        eye = sl.vec3u("eye");
        
        textureLinear = sl.slBegin(SLVec4.class, "sampler2D", "vec2"); {
        	sl.putStatement("return "+glslGamma.toLinearVec4+"(texture2D("+sl.arg(0)+", "+sl.arg(1)+" ))");
        }
        sl.slEnd();
        
    }
    
    // https://github.com/stackgl/glsl-lighting-walkthrough/blob/master/lib/shaders/phong.frag
    private void finish() {
        // Parameters
        // ================================================================================================== //
        
        SLVec3 fDiffuse = (this.diffuse==null) ? vec3(0.5f) : this.diffuse;
        SLVec3 fNormal = (this.normal==null) ? vNormal : this.normal;
        SLVec3 fSpecular = (this.specular==null) ? vec3(0.5f) : this.specular;
        
        StandardMaterialUtil.fragment(this,vPosition,eye,numLights,fDiffuse,fNormal,fSpecular);
    }
    
    /** Use this with StandardMaterial instead of regular SL sampling */
    public SLVec3 sample(SLTexU u) {
    	return this.sample(u, vTexCoord).xyz();
    }
    
    /** Use this with StandardMaterial instead of regular SL sampling */
    public SLVec3 sampleGamma(SLTexU u) {
    	return this.call(textureLinear, u, vTexCoord).xyz();
    }
    
    /** Use this for setting normal values that need to be "globalized" */
    public SLVec3 perturbNormal(SLVec3 n) {
    	return this.normalize( this.mul(mTBN, n ) );
    }
    
    /** Calls perturbNormal(sample(u)*2 - 1) */
    public SLVec3 sampleNormal(SLTexU u) {
    	SLVec3 sample = this.sub(this.mul(sample(u), 2f), 1f);
    	return perturbNormal(sample);
    }
    
    

}
