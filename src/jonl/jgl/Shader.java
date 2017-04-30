package jonl.jgl;

/**
 * @author Jonathan Lacombe
 */
public interface Shader {

    public void compileSource(String source);
    
    public void compileSourceFromFile(String file);
    
}
