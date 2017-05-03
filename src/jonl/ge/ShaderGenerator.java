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
    
    /** Every type of material must have a unique string */
    private static String getProgramString(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append("mat"+material.id); //TODO dont create program for every material instance?
        return sb.toString();
    }
    
    Program getOrCreateProgram(Material material) {
        String string = getProgramString(material);
        Program glprogram = programMap.get(string);
        if (glprogram==null) {
            String fragSource = null;
            String vertSource = null;
            switch (material.shader) {
            case STANDARD:
                fragSource = ShaderGeneratorStandard.getFragSource(material);
                vertSource = ShaderGeneratorStandard.getVertSource(material);
                break;
            case BASIC:
                fragSource = ShaderGeneratorBasic.getFragSource(material);
                vertSource = ShaderGeneratorBasic.getVertSource(material);
                FileUtils.writeToFile("test"+material.id+".txt", fragSource); //TODO remove DEBUG
                break;
            }
            glprogram = AppUtil.createProgramFromSource(gl,vertSource,fragSource);
            programMap.put(string,glprogram);
        }
        return glprogram;
    }
    
}
