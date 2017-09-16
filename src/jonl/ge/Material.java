package jonl.ge;

import java.util.List;

public abstract class Material {
    
    abstract List<Uniform> uniforms();
    
    abstract String shaderKey();
    
    class Uniform {
        String name;
        Object data;
        Uniform(String name, Object data) {
            this.name = name;
            this.data = data;
        }
    }
    
}
