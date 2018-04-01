package jonl.ge.mod.fx;

import jonl.ge.core.Texture;
import jonl.ge.core.material.GeneratedShader;
import jonl.ge.core.material.ShaderLanguage;
import jonl.ge.core.material.ShaderLanguage.SLFloatU;
import jonl.ge.core.material.ShaderLanguage.SLTexU;
import jonl.ge.core.material.ShaderLanguage.SLVec2;
import jonl.ge.core.material.ShaderLanguage.SLVec4;
import jonl.ge.core.material.ShaderLanguage.SLVec4U;
import jonl.ge.shaders.FogShader;
import jonl.ge.utils.SLImports;
import jonl.vmath.Vector4;
import jonl.vmath.Vector3;

// https://github.com/mrdoob/three.js/blob/dev/src/materials/PointsMaterial.js
// https://github.com/mrdoob/three.js/blob/dev/src/renderers/shaders/ShaderLib/points_vert.glsl
// https://github.com/mrdoob/three.js/blob/dev/src/renderers/shaders/ShaderLib/points_frag.glsl

/**
 * Material for rendering points
 * 
 * @author Jonathan
 *
 */
public class PointsMaterial extends GeneratedShader {
    
    private boolean hasTexture;
    
    public PointsMaterial(Texture texture, Vector4 color, float size, boolean useSizeAttenuation) {
        super(vertexShader(useSizeAttenuation,size), fragmentShader(texture,color));
        setColor(color);
        setSize(size);
        hasTexture = (texture!=null);
    }
    
    public PointsMaterial(Vector4 color, float size, boolean useSizeAttenuation) {
        this(null, color, size, useSizeAttenuation);
    }
    
    public Vector4 getColor() {
        return (Vector4) getUniformData("color");
    }
    
    public void setColor(Vector4 color) {
        setUniform("color", color);
    }
    
    public void setColor(Vector3 color) {
        setColor(new Vector4(color,1));
    }
    
    public float getSize() {
        return (float) getUniformData("size");
    }
    
    public void setSize(float size) {
        setUniform("size", size);
    }
    
    public boolean hasTexture() {
        return hasTexture;
    }

    @Override
    protected String shaderKey() {
        return "PointsMaterial"; // Since all shaders are the same
    }

    static ShaderLanguage vertexShader(boolean useSizeAttenuation, float sizef) {
        ShaderLanguage sl = new ShaderLanguage("vs");
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 MV");
        sl.uniform("float _height");
        
        SLFloatU size = sl.slFloatu("size", sizef);
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.putStatement("vec3 mvPosition = vec3(MV * vertex)");
        
        if (useSizeAttenuation) {
            sl.gl_PointSize(sl.slFloat(size+" * ((_height/2) / - mvPosition.z)"));
        } else {
            sl.gl_PointSize(size);
        }
        
        sl.include(FogShader.fogVertex("mvPosition"));
        
        return sl;
    }
    
    static ShaderLanguage fragmentShader(Texture tex, Vector4 col) {
        ShaderLanguage sl = new ShaderLanguage("fs");
        
        sl.version("330");
        
        SLImports.GLSLGamma gamma = sl.include(new SLImports.GLSLGamma());
        
        SLVec4U color = sl.vec4u("color", new Vector4(1,1,1,1));
        SLVec4 diffuseColor = sl.vec4(color);
        
        if (tex != null) {
            SLTexU texture = sl.texture("texture", tex);
            
            SLVec2 uv = sl.vec2("vec2(gl_PointCoord.x, 1.0 - gl_PointCoord.y)");
            
            SLVec4 mapTexel = sl.sample(texture, uv);
            
            sl.call(gamma.toLinearVec4, mapTexel);
            
            sl.set(diffuseColor, sl.mul(diffuseColor, mapTexel));
        }
        
        sl.gl_FragColor(diffuseColor);
        
        sl.include(FogShader.fogFragment());
        
        return sl;
    }
    
}
