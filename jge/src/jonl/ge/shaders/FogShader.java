package jonl.ge.shaders;

import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLInclude;
import jonl.ge.core.material.ShaderLanguage.SLVec3;
import jonl.ge.core.material.ShaderLanguage.SLVec3U;
import jonl.vmath.Vector3;

public class FogShader {

    public static SLInclude fogVertex(String mvPosition) {
        return (sl) -> {
            SLFloat fogDepth = sl.attributeOut(SLFloat.class, "fogDepth");
            sl.putStatement(fogDepth+" = -"+mvPosition+".z");
        };
    }
    
    public static SLInclude fogFragment() {
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
