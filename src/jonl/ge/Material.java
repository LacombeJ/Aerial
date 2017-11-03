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
    
    public abstract void setUniform(String name, Object object);

    public GeneratedMaterial asGeneratedMaterial() {
        return (GeneratedMaterial) this;
    }
    
    public ShaderMaterial asShaderMaterial() {
        return (ShaderMaterial) this;
    }
    
    public SolidMaterial asSolidMaterial() {
        return (SolidMaterial) this;
    }
    
    public TextureMaterial asTextureMaterial() {
        return (TextureMaterial) this;
    }
    
}
