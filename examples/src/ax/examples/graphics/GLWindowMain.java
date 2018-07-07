package ax.examples.graphics;

import ax.graphics.GL;
import ax.graphics.Mesh;
import ax.graphics.Program;
import ax.graphics.Shader;
import ax.graphics.Window;
import ax.graphics.lwjgl.GLFWWindow;

public class GLWindowMain {

    public static void main(String[]args) {
        
        Window window = new GLFWWindow();
        GL gl = window.getGL();
        Mesh mesh = createMesh(gl);
        Program program = createProgram(gl);
        
        window.setRunner(()->{
            
            while (window.isRunning()) {
                
                gl.glClear(GL.COLOR_BUFFER_BIT);
                
                gl.glViewport(0,0,window.getWidth(),window.getHeight());
                
                gl.glUseProgram(program);
                gl.glRender(mesh, GL.TRIANGLES);
                gl.glUseProgram(null);
                
            }
        });
        
        window.start();
        
    }
    
    static Mesh createMesh(GL gl) {
        
        float[] vertices = {
            -1, -1,  0,
             1, -1,  0,
             1,  1,  0,
            -1,  1,  0
        };
        
        int[] indices = { 0, 1, 2, 0, 2, 3 };
        
        float[] colors = {
            1f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 1f,
            1f, 1f, 0f
        };
        
        Mesh mesh = gl.glGenMesh(vertices,indices);
        mesh.setCustomAttrib(1,colors,3);
        
        return mesh;
        
    }
    
    static Program createProgram(GL gl) {
        Program program = gl.glCreateProgram();
        Shader vs = gl.glCreateShader(GL.VERTEX_SHADER,vs());
        Shader fs = gl.glCreateShader(GL.FRAGMENT_SHADER,fs());
        program.attach(vs);
        program.attach(fs);
        program.link();
        return program;
    }
    
    static void ln(StringBuilder sb, String line) {
        sb.append(line);
        sb.append('\n');
    }
    
    static String vs() {
        StringBuilder sb = new StringBuilder();
        ln(sb,"#version 330");
        ln(sb,"in vec4 vPosition;");
        ln(sb,"layout (location = 1) in vec3 vColor;");
        ln(sb,"out vec3 fColor;");
        ln(sb,"void main() {");
        ln(sb,"     gl_Position = vPosition;");
        ln(sb,"     fColor = vColor;");
        ln(sb,"}");
        return sb.toString();
    }
    
    static String fs() {
        StringBuilder sb = new StringBuilder();
        ln(sb,"#version 330");
        ln(sb,"in vec3 fColor;");
        ln(sb,"void main() {");
        ln(sb,"     gl_FragColor = vec4(fColor,1);");
        ln(sb,"}");
        return sb.toString();
    }
    
}
