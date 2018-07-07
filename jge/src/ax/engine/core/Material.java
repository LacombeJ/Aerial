package ax.engine.core;

import java.util.List;

import ax.commons.misc.TypeUtils;
import ax.engine.core.material.GeneratedMaterial;
import ax.engine.core.material.GeneratedShader;
import ax.engine.core.material.ShaderMaterial;
import ax.engine.core.material.SolidMaterial;
import ax.engine.core.material.TextureMaterial;

public abstract class Material {

    protected abstract String vertexShader(int version);
    
    protected abstract String geometryShader(int version);
    
    protected abstract String fragmentShader(int version);

    protected abstract List<Uniform> uniforms();
    
    protected abstract String shaderKey();
    
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
    
    public ShaderMaterial asShaderMaterial() {
    	return TypeUtils.cast(this, ShaderMaterial.class);
    }
    
    public SolidMaterial asSolidMaterial() {
    	return TypeUtils.cast(this, SolidMaterial.class);
    }
    
    public TextureMaterial asTextureMaterial() {
    	return TypeUtils.cast(this, TextureMaterial.class);
    }
    
    protected class Uniform {
        String name;
        Object data;
        public Uniform(String name, Object data) {
            this.name = name;
            this.data = data;
        }
    }
    
}
