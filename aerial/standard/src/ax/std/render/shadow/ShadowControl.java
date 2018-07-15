package ax.std.render.shadow;

import ax.engine.core.Service;
import ax.engine.core.material.ShaderLanguage;
import ax.graphics.GL;
import ax.graphics.Program;
import ax.graphics.Shader;

public class ShadowControl {

    GL gl;
    Program program;

    ShadowControl() {

    }

    void create(Service service) {
        gl = service.getGL();
        program = createProgram();
    }

    private Program createProgram() {

        Program program = gl.glCreateProgram();
        Shader vs = gl.glCreateShader(GL.ShaderType.VERTEX_SHADER, vs());
        Shader fs = gl.glCreateShader(GL.ShaderType.FRAGMENT_SHADER, fs());
        program.attach(vs);
        program.attach(fs);
        program.link();

        return program;

    }

    public String vs() {
        ShaderLanguage sl = new ShaderLanguage();
        sl.version("330");
        sl.layoutIn(0,"vec4 vertex");
        sl.uniform("mat4 MVP");
        sl.putStatement("gl_Position = MVP * vertex");
        return sl.shader();

    }

    public String fs() {
        ShaderLanguage sl = new ShaderLanguage();
        sl.version("330");
        sl.putStatement("gl_FragColor = vec4(gl_FragCoord.z);");
        return sl.shader();

    }




}
