package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.ge.MaterialBuilder.MBFloat;
import jonl.ge.MaterialBuilder.MBShader;
import jonl.ge.MaterialBuilder.MBTexU;
import jonl.ge.MaterialBuilder.MBUniform;
import jonl.ge.MaterialBuilder.MBVec3;

public class Material {

    ArrayList<MBUniform> mbUniformList;
    HashMap<String,MBUniform> mbUniformMap; //TODO different hashCode?s
    String mbUniforms;
    String mbStatements;
    
    MBShader shader      = null;
    MBVec3   diffuse     = null;
    MBVec3   specular    = null;
    MBVec3   normal      = null;
    MBTexU   height      = null;
    MBFloat  roughness   = null;
    MBFloat  fresnel     = null;
    
    int id;
    
    static int materialCount = 0;
    
    Material(
            MBShader shader,
            MBVec3 diffuse, MBVec3 specular, MBVec3 normal, MBTexU height,
            MBFloat roughness, MBFloat fresnel,
            ArrayList<String> statements, ArrayList<MBUniform> uniforms
    ) {
        id = materialCount++;
        this.shader = shader;
        this.diffuse = diffuse;
        this.specular = specular;
        this.normal = normal;
        this.height = height;
        this.roughness = roughness;
        this.fresnel = fresnel;
        mbStatements = getStatementString(statements);
        mbUniforms = getUniformString(uniforms);
        mbUniformList = new ArrayList<>();
        mbUniformMap = new HashMap<>();
        for (MBUniform mbu : uniforms) {
            MBUniform uniform = mbu.copy();
            this.mbUniformList.add(uniform);
            this.mbUniformMap.put(uniform.id,uniform);
        }
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
    
    private String getUniformString(ArrayList<MBUniform> uniforms) {
        StringBuilder sb = new StringBuilder();
        for (MBUniform u : uniforms) {
            sb.append("uniform "+u.type+" "+u.name+";\n");
        }
        return sb.toString();
    }
    
    private String getStatementString(ArrayList<String> statements) {
        StringBuilder sb = new StringBuilder();
        for (String s : statements) {
            sb.append(s);
        }
        return sb.toString();
    }
    
}
