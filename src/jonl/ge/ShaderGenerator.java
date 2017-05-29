package jonl.ge;

import java.util.HashMap;

import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;
import jonl.jutils.io.FileUtils;

class ShaderGenerator {
    
    private HashMap<String,Program> programMap = new HashMap<>();
    
    private GraphicsLibrary gl;
    
    ShaderGenerator(GraphicsLibrary gl) {
        this.gl = gl;
    }
    
    /** Every type of material must have a unique string 
     * @param instanced */
    private static String getProgramString(Material material, boolean instanced) {
        StringBuilder sb = new StringBuilder();
        sb.append("mat"+material.id);
        sb.append(instanced ? "i1" : "i0");
        return sb.toString();
    }
    
    Program getOrCreateProgram(Material material, boolean instanced) {
        String string = getProgramString(material, instanced);
        Program glprogram = programMap.get(string);
        if (glprogram==null) {
            String fragSource = null;
            String vertSource = null;
            switch (material.shader) {
            case STANDARD:
                fragSource = ShaderGeneratorStandard.getFragSource(material);
                vertSource = ShaderGeneratorStandard.getVertSource(material, instanced);
                FileUtils.writeToFile("test_fs"+material.id+".txt", fragSource); //TODO remove DEBUG
                FileUtils.writeToFile("test_vs"+material.id+".txt", vertSource); //TODO remove DEBUG
                break;
            case BASIC:
                fragSource = ShaderGeneratorBasic.getFragSource(material);
                vertSource = ShaderGeneratorBasic.getVertSource(material, instanced);
                break;
            }
            glprogram = AppUtil.createProgramFromSource(gl,vertSource,fragSource);
            programMap.put(string,glprogram);
        }
        return glprogram;
    }
    
}
