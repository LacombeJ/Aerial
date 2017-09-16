package jonl.ge;

import java.util.HashMap;

import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;

class ShaderGenerator {
    
    private HashMap<String,Program> programMap = new HashMap<>();
    
    private GraphicsLibrary gl;
    
    private int version = 430;
    
    ShaderGenerator(GraphicsLibrary gl) {
        this.gl = gl;
    }
    
    void setGLSLVersion(int i) {
        this.version = i;
    }
    
    int getGLSLVersion() {
        return version;
    }
    
    /** Every type of material must have a unique string 
     * @param instanced */
    private static String getProgramString(GeneratedMaterial material, boolean instanced) {
        StringBuilder sb = new StringBuilder();
        sb.append(material.shaderKey());
        sb.append(instanced ? "i1" : "i0");
        return sb.toString();
    }
    
    Program getOrCreateProgram(GeneratedMaterial material, boolean instanced) {
        String string = getProgramString(material, instanced);
        Program glprogram = programMap.get(string);
        if (glprogram==null) {
            String fragSource = null;
            String vertSource = null;
            switch (material.shader) {
            case STANDARD:
                fragSource = ShaderGeneratorStandard.getFragSource(version, material);
                vertSource = ShaderGeneratorStandard.getVertSource(version, material, instanced);
                break;
            case BASIC:
                fragSource = ShaderGeneratorBasic.getFragSource(version, material);
                vertSource = ShaderGeneratorBasic.getVertSource(version, material, instanced);
                break;
            }
            glprogram = AppUtil.createProgramFromSource(gl,vertSource,fragSource);
            programMap.put(string,glprogram);
        }
        return glprogram;
    }

    
    
}
