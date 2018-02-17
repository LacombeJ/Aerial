package jonl.ge.core.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jonl.ge.base.Construct;
import jonl.ge.core.Material;
import jonl.ge.core.material.GeneratedMaterialBuilder.SLShader;
import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLFloatU;
import jonl.ge.core.material.ShaderLanguage.SLTexU;
import jonl.ge.core.material.ShaderLanguage.SLUniform;
import jonl.ge.core.material.ShaderLanguage.SLVec3;
import jonl.ge.core.material.ShaderLanguage.SLVec3U;
import jonl.vmath.Vector3;

public class GeneratedMaterial extends Material {
    //TODO rename GeneratedMaterial class
    ArrayList<SLUniform> slUniformList;
    HashMap<String,SLUniform> slUniformMap; //TODO different hashCode?s
    String slUniforms;
    String slStatements;
    String slFunctions;
    
    // Used by shader generator
    SLShader shader      = null; //TODO remove this, unlit materials should be another class
    SLVec3   diffuse     = null;
    SLVec3   specular    = null;
    SLVec3   normal      = null;
    SLTexU   height      = null;
    SLFloat  roughness   = null;
    SLFloat  fresnel     = null;
    
    String id;
    
    static GeneratedMaterialBuilder gslDSRF;
    
    {
        GeneratedMaterialBuilder sl = new GeneratedMaterialBuilder();
        sl.diffuse = sl.vec3u("diffuse",0.5f,0.5f,0.5f);
        sl.specular = sl.vec3u("specular",0.5f,0.5f,0.5f);
        sl.roughness = sl.slFloatu("roughness",0.8f);
        sl.fresnel = sl.slFloatu("fresnel",0.3f);
        
        gslDSRF = sl;
    }
    
    /**
     * Constructs a generated material with diffuse, specular, roughness, and fresnel uniforms.
     * <p>
     * Example: getUniform("specular",SLVec3U.class) returns a valid value
     */
    public GeneratedMaterial(Vector3 diffuse, Vector3 specular, float roughness, float fresnel) {
        gslDSRF.apply(this);
        getUniform("diffuse",SLVec3U.class).set(diffuse);
        getUniform("specular",SLVec3U.class).set(specular);
        getUniform("roughness",SLFloatU.class).set(roughness);
        getUniform("fresnel",SLFloatU.class).set(fresnel);
    }
    
    public GeneratedMaterial(Vector3 diffuse) {
        this(diffuse,new Vector3(0.5f,0.5f,0.5f),0.8f,0.3f);
    }
    
    public GeneratedMaterial() {
        this(new Vector3(0.5f,0.5f,0.5f));
    }
    
    GeneratedMaterial(Construct construct) {
        
    }
    
    /** @return Unordered set of uniform ID's */
    public String[] getUniformIDs() {
        String[] m = new String[slUniformMap.size()];
        int i=0;
        for (SLUniform slu : slUniformMap.values()) {
            m[i++] = slu.name;
        }
        return m;
    }
    
    public SLUniform getUniform(String id) {
        return slUniformMap.get(id);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLUniform> T getUniform(String id, Class<T> c) {
        SLUniform slu = slUniformMap.get(id);
        if (c.isInstance(slu)) {
            return (T) slu;
        }
        return null;
    }
    
    
    
    @Override
    public void setUniform(String name, Object object) {
        SLUniform slu = slUniformMap.get(name);
        slu.data = object;
    }

    @Override
	protected List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        for (SLUniform slu : slUniformList) {
            uniforms.add(new Uniform(slu.name,slu.data));
        }
        return uniforms;
    }

    @Override
	protected String shaderKey() {
        return id;
    }

	@Override
	protected String vertexShader(int version) {
		return ShaderGeneratorStandard.getVertSource(version, this, false);
	}

	@Override
	protected String geometryShader(int version) {
		return null;
	}

	@Override
	protected String fragmentShader(int version) {
		return ShaderGeneratorStandard.getFragSource(version, this);
	}
    
}
