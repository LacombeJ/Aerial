package jonl.ge.core.material;

import jonl.ge.core.Texture;
import jonl.ge.core.material.ShaderLanguage.SLBoolU;
import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLFloatU;
import jonl.ge.core.material.ShaderLanguage.SLTexU;
import jonl.ge.core.material.ShaderLanguage.SLVec4U;
import jonl.ge.shaders.FogShader;
import jonl.ge.utils.SLImports;
import jonl.ge.utils.SLImports.DiffuseOrenNayerDeprecated;
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
    
    private Texture texture = null;
    
    public PointsMaterial(Texture texture, Vector4 color) {
        super(vertexShader(), fragmentShader(texture));
        setColor(color);
        this.texture = texture;
    }
    
    public PointsMaterial(Vector4 color) {
        this(null, color);
    }
    
    public PointsMaterial(Vector3 color) {
        this(new Vector4(color,1));
    }
    
    public PointsMaterial() {
        this(new Vector4(1,1,1,1));
    }
    
    public Vector4 getColor() {
        return (Vector4) getUniform("color", SLVec4U.class).data;
    }
    
    public void setColor(Vector4 color) {
        setUniform("color", color);
    }
    
    public void setColor(Vector3 color) {
        setColor(new Vector4(color,1));
    }
    
    public float getSize() {
        return (float) getUniform("size", SLFloatU.class).data;
    }
    
    public void setSize(float size) {
        setUniform("size", size);
    }

    @Override
    protected String shaderKey() {
        return "PointsMaterial"; // Since all shaders are the same
    }

    static ShaderLanguage vertexShader() {
        ShaderLanguage sl = new ShaderLanguage("vs");
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 MV");
        sl.uniform("float _height");
        
        SLFloatU size = sl.slFloatu("size", 1f);
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.putStatement("vec3 mvPosition = vec3(MV * vertex)");
        
        SLFloat f = sl.slFloat(size+" * ((_height/2) / - mvPosition.z)");
        
        sl.gl_PointSize(f);
        
        sl.call(FogShader.fogVertex("mvPosition"));
        
        return sl;
    }
    
    static ShaderLanguage fragmentShader(Texture tex) {
        ShaderLanguage sl = new ShaderLanguage("fs");
        
        DiffuseOrenNayerDeprecated oren = sl.include(SLImports.orenNayer());
        
        
        
        sl.call(oren.orenNayer(), 4f);
        
        //sl.call(SLUtils.mapTexelToLinear, mapTexel);
        
        sl.version("330");
        
        if (tex != null) {
            SLTexU texture = sl.texture("texture");
        }
        
        SLVec4U color = sl.vec4u("color", new Vector4(1,1,1,1));
        
        sl.gl_FragColor(color);
        
        sl.call(FogShader.fogFragment());
        
        return sl;
    }
    
}
