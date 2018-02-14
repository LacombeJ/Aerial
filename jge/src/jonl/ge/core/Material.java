package jonl.ge.core;

import java.util.List;

import jonl.jutils.misc.TypeUtils;

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
    	return TypeUtils.cast(this, GeneratedMaterial.class);
    }
    
    public GeneratedShader asGeneratedShader() {
    	return TypeUtils.cast(this, GeneratedShader.class);
    }
    
    public StandardMaterial asStandardMaterial() {
    	return TypeUtils.cast(this, StandardMaterial.class);
    }
    
    public ShaderMaterial asShaderMaterial() {
    	return TypeUtils.cast(this, ShaderMaterial.class);
    }
    
    public SolidMaterial asSolidMaterial() {
    	return TypeUtils.cast(this, SolidMaterial.class);
    }
    
    public TextureMaterial asTextureMaterial() {
    	return TypeUtils.cast(this, TextureMaterial.class);
    }
    
}
