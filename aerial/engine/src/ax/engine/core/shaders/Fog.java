package ax.engine.core.shaders;

import ax.engine.core.material.ShaderLanguage.SLFloat;
import ax.engine.core.material.ShaderLanguage.SLInclude;
import ax.engine.core.material.ShaderLanguage.SLVec3;
import ax.engine.core.material.ShaderLanguage.SLVec3U;
import ax.math.vector.Vector3;

public class Fog {

    public static SLInclude vertex(String mvPosition) {
        return (sl) -> {
            SLFloat fogDepth = sl.attributeOut(SLFloat.class, "fogDepth");
            sl.putStatement(fogDepth+" = -"+mvPosition+".z");
        };
    }
    
    public static SLInclude fragment() {
        return (sl) -> {
            SLFloat fogDepth = sl.attributeIn(SLFloat.class, "fogDepth");
            
            SLVec3U fogColor = sl.vec3u("fogColor", new Vector3(0,0,0));
            sl.uniform("float _near");
            sl.uniform("float _far");
            
            SLFloat fogFactor = sl.smoothstep(sl.slFloat("_near"), sl.slFloat("_far"), fogDepth);
            
            SLVec3 color = sl.mix(sl.vec3("gl_FragColor.rgb"), fogColor, fogFactor);
            
            sl.putStatement("gl_FragColor.rgb = "+color);
        };
    }
    
}
