package jonl.ge.core.material;

import jonl.ge.core.shaders.SLUtils;
import jonl.ge.core.shaders.SLImports.Attenuation;
import jonl.ge.core.shaders.SLImports.DiffuseOrenNayer;
import jonl.ge.core.shaders.SLImports.GLSLGamma;
import jonl.ge.core.shaders.SLImports.SpecularPhong;

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
		build_0();
	}
    
    // Build only occurs once
    public StandardMaterial build() {
    	
    	//TODO find a better way to do this (multiple shaders with different build calls)
    	if (build == null) {
    		build_1();
    	}    	
    	
        StandardMaterial mat = new StandardMaterial(0);
        
        mat.diffuse = copyData(diffuse);
        mat.specular = copyData(specular);
        mat.normal = copyData(normal);
        
        ShaderLanguage vs = SLUtils.normalVert();
        ShaderLanguage fs = this;
        
        mat.vertexShader = vs.shader();
        mat.fragmentShader = fs.shader();
        mat.add(vs);
        mat.add(fs);
        
        build = mat;
        
        return build;
    }
    
    class SLLight extends SLStruct {
        SLInt type;
    	SLVec3 position;
    	SLVec3 color;
    	SLVec3 ambient;
    	SLFloat falloff;
    	SLFloat radius;
    	SLVec3 direction;
		@Override
		public void init(SLStructBuilder sb) {
		    type = sb.slInt("type");
			position = sb.vec3("position");
			color = sb.vec3("color");
			ambient = sb.vec3("ambient");
			falloff = sb.slFloat("falloff");
			radius = sb.slFloat("radius");
			direction = sb.vec3("direction");
		}
    }
    
    private void build_0() {
    	
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
    private void build_1() {
    	
    	ShaderLanguage sl = this;
    	
    	// Declares
    	// ================================================================================================== //
    	
        DiffuseOrenNayer orenNayer = sl.include(new DiffuseOrenNayer());
        SpecularPhong phong = sl.include(new SpecularPhong());
        Attenuation attenuation = sl.include(new Attenuation());
    	
        SLDeclare<SLLight> Light = sl.declare(() -> new SLLight());
        
        int MAX_NUM_LIGHTS = 5;
        float specularScale = 3f;
        float shininess = 20.0f;
        float roughness = 0.1f;
        float albedo = 0.95f;
        
        SLArray<SLLight> array = sl.arrayu(Light, "light", MAX_NUM_LIGHTS);
        
        // Parameters
        // ================================================================================================== //
        
        SLVec3 fDiffuse = (this.diffuse==null) ? sl.vec3(0.5f) : this.diffuse;
        SLVec3 fNormal = (this.normal==null) ? vNormal : this.normal;
        SLVec3 fSpecular = (this.specular==null) ? sl.vec3(0.5f) : this.specular;
        
        // Main
        // ================================================================================================== //
        
        // Light-invariant variables
        SLVec3 eyeVector = sl.sub( eye, vPosition );
        SLVec3 finalColor = sl.vec3(0f);
        SLVec3 V = sl.normalize(eyeVector);
        
        // Multi-light rendering
        SLInt i = sl.slLoop(0,numLights,1); {
        	
        	SLLight light = sl.index(array, i);
            
        	// Point light
        	sl.slIf(light.type+"=="+1); {
        	    
        	    SLVec4 lightPosition = sl.vec4(light.position,1f);
                SLVec3 lightVector = sl.sub( lightPosition.xyz() , vPosition );
                
                SLFloat lightDistance = sl.length(lightVector);
                SLFloat falloff = sl.call(attenuation.attenuation, light.radius, light.falloff, lightDistance);
                
                SLVec3 L = sl.normalize(lightVector);
                
                SLVec3 specular = sl.mul(fSpecular, sl.call(phong.phong, L, V, fNormal, shininess), specularScale, falloff);
                SLVec3 diffuse = sl.mul(light.color, sl.call(orenNayer.orenNayer, L, V, fNormal, roughness, albedo), falloff);
                SLVec3 ambient = light.ambient;
                
                sl.set(finalColor, sl.add(finalColor, sl.add(fDiffuse.mul(sl.add(diffuse, ambient)), specular)));
        	    
        	}
        	// Spot light
        	sl.slElseIf(light.type+"=="+2); {
        	    
        	}
        	// Directional light
    	    sl.slElseIf(light.type+"=="+3); {
                
    	        // Same as point light but direction is set by light and there is no falloff
    	        
                SLVec3 lightVector = sl.neg(light.direction);
                
                SLVec3 L = sl.normalize(lightVector);
                
                SLVec3 specular = sl.mul(fSpecular, sl.call(phong.phong, L, V, fNormal, shininess), specularScale);
                SLVec3 diffuse = sl.mul(light.color, sl.call(orenNayer.orenNayer, L, V, fNormal, roughness, albedo));
                SLVec3 ambient = light.ambient;
                
                sl.set(finalColor, sl.add(finalColor, sl.add(fDiffuse.mul(sl.add(diffuse, ambient)), specular)));
    	        
            }
    	    sl.slEndIf();
    	    
        }
        sl.slEndLoop();
        
        finalColor = sl.call(glslGamma.toGammaVec3, finalColor);
        
    	gl_FragColor(sl.vec4(finalColor, 1f));
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
