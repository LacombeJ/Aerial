package jonl.ge;

import jonl.jutils.io.Console;
import jonl.jutils.io.FileUtils;
import jonl.jgl.Program;
import jonl.jgl.Shader;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.ShaderType;

class AppUtil {
    
    static final Font FONT_CONSOLAS = new Font("Consolas",Font.PLAIN,24,false);
    
    static GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        MeshRenderer cubeRenderer = new MeshRenderer();
        cubeRenderer.mesh = Loader.loadMesh("res/models/cube2.mesh"); //TODO hardcode mesh
        cube.addComponent(cubeRenderer);
        return cube;
    }
    
    static Material defaultMaterial() {
        MaterialBuilder mb = new MaterialBuilder();
        mb.diffuse = mb.vec3u("diffuse",0.5f,0.5f,0.5f);
        mb.specular = mb.vec3u("specular",0.5f,0.5f,0.5f);
        mb.roughness = mb.mbFloatu("roughness",0.8f);
        mb.fresnel = mb.mbFloatu("fresnel",0.3f);
        return mb.build();
    }
    
    static Program createProgramFromSource(GraphicsLibrary gl, String vertSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(ShaderType.VERTEX_SHADER,vertSource);
        Shader fragShader = gl.glCreateShader(ShaderType.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    static Program createProgram(GraphicsLibrary gl, String vertFile, String fragFile) {
        return createProgramFromSource(gl,FileUtils.readFromFile(vertFile).toString(),FileUtils.readFromFile(fragFile).toString());
    }
    
    static void setUniform(Program program, String name, Vector2 v) {
        program.setUniform(name,v.x,v.y);
    }
    
    static void setUniform(Program program, String name, Vector3 v) {
        program.setUniform(name,v.x,v.y,v.z);
    }
    
    static void setUniform(Program program, String name, Vector4 v) {
        program.setUniform(name,v.x,v.y,v.z,v.w);
    }
    
    static void setUniform(Program program, String name, float[] v) {
        switch (v.length) {
        case 1: program.setUniform(name,v[0]); break;
        case 2: program.setUniform(name,v[0],v[1]); break;
        case 3: program.setUniform(name,v[0],v[1],v[2]); break;
        case 4: program.setUniform(name,v[0],v[1],v[3]); break;
        default: throw new IndexOutOfBoundsException(); //TODO throw error
        }
    }
    
    public static void printFile(String file) {
        String[] str = FileUtils.linesFromFile(file);
        for (String s : str) {
            Console.print("\""+s+"\\n\" +");
        }
    }
    
    /*
    public static void main(String[]args) {
        printFile("res/shaders/standard_12_8.vert");
    }
    */
}
