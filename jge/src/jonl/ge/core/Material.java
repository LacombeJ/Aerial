package jonl.ge.core;

import jonl.ge.base.BaseMaterial;
import jonl.ge.core.material.GeneratedMaterial;
import jonl.ge.core.material.GeneratedShader;
import jonl.ge.core.material.ShaderMaterial;
import jonl.ge.core.material.SolidMaterial;
import jonl.ge.core.material.StandardMaterial;
import jonl.ge.core.material.TextureMaterial;
import jonl.jutils.misc.TypeUtils;

public abstract class Material extends BaseMaterial {
    
    
    
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
