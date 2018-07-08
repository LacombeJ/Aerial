package ax.engine.core.shaders;

import ax.engine.core.material.ShaderLanguage.SLInclude;
import ax.math.vector.Mathf;

public class LogDepth {

    public static SLInclude vertex_header() {
        return (sl) -> {
            sl.uniform("float _logDepthBufFC");
            
            sl.attributeOut("float vFragDepth");
        };
    }
    
    public static SLInclude vertex() {
        return (sl) -> {
            String EPSILON = Mathf.EPSILON+"";
            
            sl.putStatement("vFragDepth = 1.0 + gl_Position.w");
            
            sl.putStatement("gl_Position.z = log2(max( "+EPSILON+", gl_Position.w + 1.0 )) * _logDepthBufFC");
            sl.putStatement("gl_Position.z = (gl_Position.z - 1.0) * gl_Position.w");
            
        };
    }
    
    public static SLInclude fragment() {
        return (sl) -> {
            sl.enable("GL_EXT_frag_depth");
            
            sl.uniform("float _logDepthBufFC");
            sl.attributeIn("float vFragDepth");
            
            sl.putStatement("gl_FragDepthEXT = log2(vFragDepth) * _logDepthBufFC * 0.5");
        };
    }
    
}
