package jonl.jgl.lwjgl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import jonl.jgl.AbstractShader;
import jonl.jgl.GraphicsLibrary.ShaderType;


/**
 * @author Jonathan Lacombe
 *
 */
class LWJGLShader extends AbstractShader {
    
    private final int id;
    
    LWJGLShader(ShaderType type) {
        id = GL20.glCreateShader( LWJGL.getShaderType(type) );
    }
    
    LWJGLShader(ShaderType type, String source) {
        this(type);
        compileSource(source);
    }
    
    int getID() {
        return id;
    }
    
    @Override
    public void compileSource(String source) {
        GL20.glShaderSource(id, source);
        
        GL20.glCompileShader(id);
        
        ShaderType type = LWJGL.getShaderType( GL20.glGetShaderi(id,GL20.GL_SHADER_TYPE) );
        
        if (GL20.glGetShaderi(id,GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(type+" Shader failed to compile");
            String log = GL20.glGetShaderInfoLog(id);
            System.err.println(log);
        }
    }
    
    
}
