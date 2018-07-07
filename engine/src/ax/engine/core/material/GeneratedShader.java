package ax.engine.core.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ax.engine.core.material.ShaderLanguage.SLUniform;

public class GeneratedShader extends ShaderMaterial {
    
    ArrayList<SLUniform> slUniformList = new ArrayList<>();
    HashMap<String,SLUniform> slUniformMap = new HashMap<>(); //TODO different hashCode?s
    
    protected GeneratedShader() {
    	
    }
    
    public GeneratedShader(ShaderLanguage slVert, ShaderLanguage slFrag ) {
        super(slVert.shader(), slFrag.shader());
        add(slVert);
        add(slFrag);
    }
    
    public GeneratedShader(ShaderLanguage slVert, ShaderLanguage slGeom, ShaderLanguage slFrag) {
        super(slVert.shader(), slGeom.shader(), slFrag.shader());
        add(slVert);
        add(slGeom);
        add(slFrag);
    }
    
    protected void add(ShaderLanguage sl) {
        sl.addUniformListAndMap(slUniformList, slUniformMap);
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
    
    public Object getUniformData(String id) {
        return getUniform(id).data;
    }
    
    String getUniformString(ArrayList<SLUniform> uniforms) {
        StringBuilder sb = new StringBuilder();
        for (SLUniform u : uniforms) {
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
    
}
