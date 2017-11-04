package jonl.ge;

import java.util.HashMap;

import jonl.ge.utils.PresetData;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;
import jonl.jutils.io.FileUtils;

class MaterialProgramMapper {
    
    private GraphicsLibrary gl;
    
    private HashMap<String,Program> map = new HashMap<>();
    
    private int version = 430;
    
    
    MaterialProgramMapper(GraphicsLibrary gl) {
        this.gl = gl;
    }
    
    void setGLSLVersion(int i) {
        this.version = i;
    }
    
    int getGLSLVersion() {
        return version;
    }
    
    Program getOrCreateProgram(Material material) {
        String string = material.shaderKey();
        Program glprogram = map.get(string);
        if (glprogram==null) {
            if (material instanceof GeneratedMaterial) {
                GeneratedMaterial m = (GeneratedMaterial)material;
                //TODO remove ShaderGeneratorBasic and simplify ShaderGenerator
                String fragSource = ShaderGeneratorStandard.getFragSource(version, m);
                String vertSource = ShaderGeneratorStandard.getVertSource(version, m, false); //TODO remove false instance parameter
                glprogram = AppUtil.createProgramFromSource(gl, vertSource, fragSource);
                ///*
                //TODO remove write to file debugging
                //FileUtils.writeToFile("debug_"+material.shaderKey()+".fs.c", fragSource);
                //*/
            } else if (material instanceof ShaderMaterial) {
                ShaderMaterial m = (ShaderMaterial)material;
                if (m.geometryShader!=null) {
                    glprogram = AppUtil.createProgramFromSource(gl,m.vertexShader,m.geometryShader,m.vertexShader);
                } else {
                    glprogram = AppUtil.createProgramFromSource(gl,m.vertexShader,m.fragmentShader);
                }
                m.vertexShader = null;
                m.geometryShader = null;
                m.fragmentShader = null;
            } else if (material instanceof SolidMaterial) {
                glprogram = AppUtil.createProgramFromSource(gl,PresetData.solidVSSource(version),PresetData.solidFSSource(version));
            } else if (material instanceof TextureMaterial) {
                glprogram = AppUtil.createProgramFromSource(gl,PresetData.basicVSSource(version),PresetData.basicFSSource(version));
            }
            map.put(string,glprogram);
        }
        
        return glprogram;
    }
    
}
