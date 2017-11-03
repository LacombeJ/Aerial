package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jonl.ge.GeneratedMaterialBuilder.MBFloat;
import jonl.ge.GeneratedMaterialBuilder.MBFloatU;
import jonl.ge.GeneratedMaterialBuilder.MBShader;
import jonl.ge.GeneratedMaterialBuilder.MBTexU;
import jonl.ge.GeneratedMaterialBuilder.MBUniform;
import jonl.ge.GeneratedMaterialBuilder.MBVec3;
import jonl.ge.GeneratedMaterialBuilder.MBVec3U;
import jonl.vmath.Vector3;

public class GeneratedMaterial extends Material {
    //TODO rename GeneratedMaterial class
    ArrayList<MBUniform> mbUniformList;
    HashMap<String,MBUniform> mbUniformMap; //TODO different hashCode?s
    String mbUniforms;
    String mbStatements;
    
    // Used by shader generator
    MBShader shader      = null; //TODO remove this, unlit materials should be another class
    MBVec3   diffuse     = null;
    MBVec3   specular    = null;
    MBVec3   normal      = null;
    MBTexU   height      = null;
    MBFloat  roughness   = null;
    MBFloat  fresnel     = null;
    
    String id;
    
    static GeneratedMaterialBuilder gmbDSRF;
    
    {
        GeneratedMaterialBuilder mb = new GeneratedMaterialBuilder();
        mb.diffuse = mb.vec3u("diffuse",0.5f,0.5f,0.5f);
        mb.specular = mb.vec3u("specular",0.5f,0.5f,0.5f);
        mb.roughness = mb.mbFloatu("roughness",0.8f);
        mb.fresnel = mb.mbFloatu("fresnel",0.3f);
        
        gmbDSRF = mb;
    }
    
    /**
     * Constructs a generated material with diffuse, specular, roughness, and fresnel uniforms.
     * <p>
     * Example: getUniform("specular",MBVec3U.class) returns a valid value
     */
    public GeneratedMaterial(Vector3 diffuse, Vector3 specular, float roughness, float fresnel) {
        gmbDSRF.apply(this);
        getUniform("diffuse",MBVec3U.class).set(diffuse);
        getUniform("specular",MBVec3U.class).set(specular);
        getUniform("roughness",MBFloatU.class).set(roughness);
        getUniform("fresnel",MBFloatU.class).set(fresnel);
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
        String[] m = new String[mbUniformMap.size()];
        int i=0;
        for (MBUniform mbu : mbUniformMap.values()) {
            m[i++] = mbu.name;
        }
        return m;
    }
    
    public MBUniform getUniform(String id) {
        return mbUniformMap.get(id);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBUniform> T getUniform(String id, Class<T> c) {
        MBUniform mbu = mbUniformMap.get(id);
        if (c.isInstance(mbu)) {
            return (T) mbu;
        }
        return null;
    }
    
    String getUniformString(ArrayList<MBUniform> uniforms) {
        StringBuilder sb = new StringBuilder();
        for (MBUniform u : uniforms) {
            sb.append("uniform "+u.type+" "+u.name+";\n");
        }
        return sb.toString();
    }
    
    String getStatementString(ArrayList<String> statements) {
        StringBuilder sb = new StringBuilder();
        for (String s : statements) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    @Override
    public void setUniform(String name, Object object) {
        MBUniform mbu = mbUniformMap.get(name);
        mbu.data = object;
    }

    @Override
    List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        for (MBUniform mbu : mbUniformList) {
            uniforms.add(new Uniform(mbu.name,mbu.data));
        }
        return uniforms;
    }

    @Override
    String shaderKey() {
        return id;
    }
    
}
