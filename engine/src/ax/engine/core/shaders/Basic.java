package ax.engine.core.shaders;

import ax.engine.core.material.ShaderLanguage.SLInclude;
import ax.engine.core.material.ShaderLanguage.SLVec4U;
import ax.math.vector.Vector4;

public class Basic {

    public static SLInclude vertex() {
        return (sl) -> {
            sl.version("330");
            
            sl.layoutIn(0,"vec4 vertex");
            sl.layoutIn(1,"vec3 normal");
            sl.layoutIn(2,"vec2 texCoord");
            
            sl.uniform("mat4 MVP");
            sl.uniform("mat4 M");
            
            sl.attributeOut("vec3 vPosition");
            sl.attributeOut("vec3 vNormal");
            sl.attributeOut("vec2 vTexCoord");
            
            sl.putStatement("gl_Position = MVP * vertex");
            
            sl.putStatement("vPosition = vec3(M * vertex)");
            sl.putStatement("vNormal = normalize ( mat3(M) * normal )");
            sl.putStatement("vTexCoord = texCoord");
        };
    }
    
    public static SLInclude fragment() {
        return (sl) -> {
            
            SLVec4U color = sl.vec4u("color",new Vector4(1,1,1,1));
            
            sl.putStatement("gl_FragColor = "+color);
            
        };
    }
    
}
