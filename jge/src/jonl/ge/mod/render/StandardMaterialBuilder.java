package jonl.ge.mod.render;

import jonl.ge.core.shaders.SLUtils;
import jonl.ge.core.material.ShaderLanguage;
import jonl.ge.core.shaders.SLImports.GLSLGamma;

/**
 * This material builder extends the fragment shader
 * @author Jonathan
 *
 */
public class StandardMaterialBuilder extends ShaderLanguage {
	
	private StandardMaterial build = null;
	
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
    
    public StandardMaterialBuilder() {
		super();
		prepare();
	}
    
    // Build only occurs once
    public StandardMaterial build() {
    	
    	//TODO find a better way to do this (multiple shaders with different build calls)
    	if (build == null) {
    	    finish();
    	}    	
    	
        StandardMaterial mat = new StandardMaterial(0);
        
        mat.diffuse = copyData(diffuse);
        mat.specular = copyData(specular);
        mat.normal = copyData(normal);
        
        ShaderLanguage vs = SLUtils.normalVert();
        ShaderLanguage fs = this;
        
        ShaderLanguage vsd = deferredVert();
        ShaderLanguage fsd = deferred();
        
        mat.vertexShader(vs.shader());
        mat.fragmentShader(fs.shader());
        mat.vertexShaderDeferred(vsd.shader());
        mat.fragmentShaderDeferred(fsd.shader());
        mat.addShader(vs);
        mat.addShader(fs);
        
        build = mat;
        
        return build;
    }
    
    private ShaderLanguage deferredVert() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        sl.layoutIn(1,"vec3 normal");
        sl.layoutIn(2,"vec2 texCoord");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 M");
        
        sl.attributeOut("vec3 vPosition");
        sl.attributeOut("vec3 vNormal");
        sl.attributeOut("vec2 vTexCoord");
        
        sl.putStatement("mat3 mNormal = transpose(inverse(mat3(M)))");
        
        sl.putStatement("vPosition = vec3(M * vertex)");
        sl.putStatement("vNormal = normalize ( mNormal * normal )");
        sl.putStatement("vTexCoord = texCoord");
        
        sl.attributeOut("vec3 vStencil");
        sl.putStatement("vStencil = vec3(1,1,1)");
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        return sl;
    }
    
    private ShaderLanguage deferred() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        SLVec4 gPosition = sl.layoutOut(SLVec4.class, 0, "gPosition");
        SLVec4 gNormal = sl.layoutOut(SLVec4.class, 1, "gNormal");
        SLVec4 gTexCoord = sl.layoutOut(SLVec4.class, 2, "gTexCoord");
        SLVec4 gStencil = sl.layoutOut(SLVec4.class, 3, "gStencil");
        
        SLVec4 vPosition = sl.attributeIn(SLVec4.class, "vPosition");
        SLVec4 vNormal = sl.attributeIn(SLVec4.class, "vNormal");
        SLVec4 vTexCoord = sl.attributeIn(SLVec4.class, "vTexCoord");
        SLVec4 vStencil = sl.attributeIn(SLVec4.class, "vStencil");
        
        sl.set(gPosition,sl.vec4(vPosition.xyz(),1));
        sl.set(gNormal,sl.vec4(sl.normalize(vNormal.xyz()),1));
        sl.set(gTexCoord,vTexCoord);
        sl.set(gStencil,vStencil);
        
        return sl;
    }
    
    private void prepare() {
    	
    	ShaderLanguage sl = this;
		
    	glslGamma = sl.include(new GLSLGamma());
    	
    	vPosition = sl.attributeIn(SLVec3.class, "vPosition");
        vNormal = sl.attributeIn(SLVec3.class, "vNormal");
        vTexCoord = sl.attributeIn(SLVec2.class, "vTexCoord");
        
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
