package jonl.jgl;

import jonl.jutils.io.FileUtils;

public abstract class AbstractShader implements Shader {
    
    @Override
    public void compileSourceFromFile(String file) {
        String source = FileUtils.readFromFile(file).toString();
        compileSource(source);
    }
    
}