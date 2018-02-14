package jonl.ge.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Shader extends Material {

    HashMap<String,Object> uniformMap = new HashMap<>();
    
    //Shaders are deleted by MaterialProgramMapper on glProgram creation
    String vertexShader;
    String geometryShader;
    String fragmentShader;
    
    private int id;
    private static int materialCount = 0;
    
    Shader() {
        id = materialCount++;
    }
    
    public Shader(String vert, String frag) {
        this();
        vertexShader = vert;
        fragmentShader = frag;
    }
    
    public Shader(String vert, String geom, String frag) {
        this();
        vertexShader = vert;
        geometryShader = geom;
        fragmentShader = frag;
    }

    public void setUniform(String name, Object uniform) {
        uniformMap.put(name, uniform);
    }
    
    public Object getUniform(String name) {
        return uniformMap.get(name);
    }

    @Override
    List<Uniform> uniforms() {
        List<Uniform> uniforms = new ArrayList<>();
        for (Entry<String,Object> e : uniformMap.entrySet()) {
            uniforms.add(new Uniform(e.getKey(), e.getValue()));
        }
        return uniforms;
    }

    @Override
    String shaderKey() {
        return "_shader_"+id;
    }
    
    
    
}
