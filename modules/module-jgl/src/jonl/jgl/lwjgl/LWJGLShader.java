package jonl.jgl.lwjgl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import jonl.jgl.AbstractShader;
import jonl.jgl.GraphicsLibrary.ShaderType;
import jonl.jutils.misc.StringUtils;


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
            
            String[] lines = source.split("\n");
            int numLen = ("Line "+lines.length+"").length();
            for (int i=0; i<lines.length; i++) {
                String n = "Line "+(i+1);
                n = StringUtils.padBackMatch(n,' ',numLen);
                n = n+":";
                System.err.println(n+lines[i]);
            }
            System.err.println(log);
        }
    }
    
    
}
