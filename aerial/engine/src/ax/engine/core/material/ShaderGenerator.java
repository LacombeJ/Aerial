package ax.engine.core.material;

import java.util.HashMap;

import ax.engine.utils.GLUtils;
import ax.graphics.GL;
import ax.graphics.Program;

class ShaderGenerator {
    
    private HashMap<String,Program> programMap = new HashMap<>();
    
    private GL gl;
    
    private int version = 430;
    
    ShaderGenerator(GL gl) {
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
            glprogram = GLUtils.createProgramFromSource(gl,vertSource,fragSource);
            programMap.put(string,glprogram);
        }
        return glprogram;
    }

    
    
}
