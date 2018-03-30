package jonl.ge.core.material;

import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLVec4U;
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
    
    public PointsMaterial(Vector4 color) {
        super(vertexShader(), fragmentShader());
        setColor(color);
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

    @Override
    protected String shaderKey() {
        return "PointsMaterial"; // Since all shaders are the same
    }

    static ShaderLanguage vertexShader() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 MV");
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.putStatement("vec3 mvPosition = vec3(MV * vertex)");
        
        SLFloat f = sl.slFloat("30 * (288 / - mvPosition.z)");
        
        sl.gl_PointSize(f);
        
        return sl;
    }
    
    static ShaderLanguage fragmentShader() {
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        SLVec4U color = sl.vec4u("color", new Vector4(1,1,1,1));
        
        sl.gl_FragColor(color);
        
        return sl;
    }
    
}
