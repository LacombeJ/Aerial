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
    
    /**
     * Available uniform object primitives and classes:
     * boolean, int, float, Vector2, Vector3, Vector4, Matrix2, Matrix3, Matrix4, TextureUniform
     * @param name
     * @param object
     * 
     */
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
