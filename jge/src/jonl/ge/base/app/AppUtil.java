package jonl.ge.base.app;

import jonl.jutils.io.Console;
import jonl.jutils.io.FileUtils;
import jonl.jgl.Program;
import jonl.jgl.Shader;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;
import jonl.ge.core.GameObject;
import jonl.ge.core.Material;
import jonl.ge.core.Mesh;
import jonl.ge.core.geometry.Geometry;
import jonl.ge.core.material.GeneratedMaterial;
import jonl.ge.core.material.GeneratedMaterialBuilder;
import jonl.ge.core.text.Font;
import jonl.ge.utils.Loader;
import jonl.ge.utils.PresetData;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.GraphicsLibrary.ShaderType;

public class AppUtil {
    
	public static final Font FONT_CONSOLAS = new Font("Consolas",Font.PLAIN,24,false);
    
    public static GameObject cube() {
        GameObject cube = new GameObject();
        cube.setName("Cube");
        Geometry geometry = Loader.loadMesh(PresetData.cubeMesh());
        Material material = new GeneratedMaterial();
        Mesh mesh = new Mesh(geometry,material);
        cube.addComponent(mesh);
        return cube;
    }
    
    public static GeneratedMaterial defaultMaterial() {
        GeneratedMaterialBuilder sl = new GeneratedMaterialBuilder();
        sl.diffuse = sl.vec3u("diffuse",0.5f,0.5f,0.5f);
        sl.specular = sl.vec3u("specular",0.5f,0.5f,0.5f);
        sl.roughness = sl.slFloatu("roughness",0.8f);
        sl.fresnel = sl.slFloatu("fresnel",0.3f);
        return sl.build();
    }
    
    public static Program createProgramFromSource(GraphicsLibrary gl, String vertSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(ShaderType.VERTEX_SHADER,vertSource);
        Shader fragShader = gl.glCreateShader(ShaderType.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    public static Program createProgramFromSource(GraphicsLibrary gl, String vertSource, String geomSource, String fragSource) {
        Program program = gl.glCreateProgram();
        Shader vertShader = gl.glCreateShader(ShaderType.VERTEX_SHADER,vertSource);
        Shader geomShader = gl.glCreateShader(ShaderType.GEOMETRY_SHADER,geomSource);
        Shader fragShader = gl.glCreateShader(ShaderType.FRAGMENT_SHADER,fragSource);
        program.attach(vertShader);
        program.attach(geomShader);
        program.attach(fragShader);
        program.link();
        return program;
    }
    
    public static Program createProgram(GraphicsLibrary gl, String vertFile, String fragFile) {
        return createProgramFromSource(gl,FileUtils.readFromFile(vertFile).toString(),FileUtils.readFromFile(fragFile).toString());
    }
    
    public static void setUniform(Program program, String name, Vector2 v) {
        program.setUniform(name,v.x,v.y);
    }
    
    public static void setUniform(Program program, String name, Vector3 v) {
        program.setUniform(name,v.x,v.y,v.z);
    }
    
    public static void setUniform(Program program, String name, Vector4 v) {
        program.setUniform(name,v.x,v.y,v.z,v.w);
    }
    
    public static void setUniform(Program program, String name, float[] v) {
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
