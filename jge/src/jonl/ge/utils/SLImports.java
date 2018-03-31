package jonl.ge.utils;

import jonl.ge.core.material.ShaderLanguage;
import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLFunc;
import jonl.ge.core.material.ShaderLanguage.SLInclude;
import jonl.ge.core.material.ShaderLanguage.SLMat3;
import jonl.ge.core.material.ShaderLanguage.SLVec2;
import jonl.ge.core.material.ShaderLanguage.SLVec3;
import jonl.ge.core.material.ShaderLanguage.SLVec4;
import jonl.jutils.func.Tuple2;

public class SLImports {

	private static Tuple2<String,String> arg(String type, String name) {
        return new Tuple2<String,String>(type,name);
    }
	
	// GLSL Gamma
	// https://github.com/glslify/glsl-gamma
	public static class GLSLGamma implements SLInclude {

	    public SLFloat gamma;
	    public SLFunc<SLFloat> toLinear;
	    public SLFunc<SLVec2> toLinearVec2;
	    public SLFunc<SLVec3> toLinearVec3;
	    public SLFunc<SLVec4> toLinearVec4;
	    public SLFunc<SLFloat> toGamma;
	    public SLFunc<SLVec2> toGammaVec2;
	    public SLFunc<SLVec3> toGammaVec3;
	    public SLFunc<SLVec4> toGammaVec4;
	    
        @SuppressWarnings("unchecked")
        @Override
        public void include(ShaderLanguage sl) {
            SLFloat gamma = sl.slFloatc(2.2f);
            
            toLinear = sl.slFunc(
                "return pow(v, "+gamma+");",
                SLFloat.class, arg("float","v"));
            
            toLinearVec2 = sl.slFunc(
                "return pow(v, vec2("+gamma+"));",
                SLVec2.class, arg("vec2","v"));
            
            toLinearVec3 = sl.slFunc(
                "return pow(v, vec3("+gamma+"));",
                SLVec3.class, arg("vec3","v"));
            
            toLinearVec4 = sl.slFunc(
                "return pow(v, vec4("+gamma+"));",
                SLVec4.class, arg("vec4","v"));
            
            
            toGamma = sl.slFunc(
                "return pow(v, 1.0 / "+gamma+");",
                SLFloat.class, arg("float","v"));
            
            toGammaVec2 = sl.slFunc(
                "return pow(v, vec2(1.0 / "+gamma+"));",
                SLVec2.class, arg("vec2","v"));
            
            toGammaVec3 = sl.slFunc(
                "return pow(v, vec3(1.0 / "+gamma+"));",
                SLVec3.class, arg("vec3","v"));
            
            toGammaVec4 = sl.slFunc(
                "return pow(v, vec4(1.0 / "+gamma+"));",
                SLVec4.class, arg("vec4","v"));
        }
	    
	}
	
    
    // Diffuse Oren-Nayer
    // https://github.com/glslify/glsl-diffuse-oren-nayar
    public static class DiffuseOrenNayer implements SLInclude {

        public SLFunc<SLFloat> orenNayer;
        
        @SuppressWarnings("unchecked")
        @Override
        public void include(ShaderLanguage sl) {
            String PI = "3.14159265";
            
            String body =
                    "  float LdotV = dot(lightDirection, viewDirection);\n" + 
                    "  float NdotL = dot(lightDirection, surfaceNormal);\n" + 
                    "  float NdotV = dot(surfaceNormal, viewDirection);\n" + 
                    
                    "  float s = LdotV - NdotL * NdotV;\n" + 
                    "  float t = mix(1.0, max(NdotL, NdotV), step(0.0, s));\n" + 
                    
                    "  float sigma2 = roughness * roughness;\n" + 
                    "  float A = 1.0 + sigma2 * (albedo / (sigma2 + 0.13) + 0.5 / (sigma2 + 0.33));\n" + 
                    "  float B = 0.45 * sigma2 / (sigma2 + 0.09);\n" + 
                    
                    "  return albedo * max(0.0, NdotL) * (A + B * s / t) / "+PI+";";
            
            orenNayer = sl.slFunc(body, SLFloat.class,
                arg("vec3","lightDirection"),
                arg("vec3","viewDirection"),
                arg("vec3","surfaceNormal"),
                arg("float","roughness"),
                arg("float","albedo")
            );
        }
        
    }
	
    // Specular phong
    // https://github.com/glslify/glsl-specular-phong/blob/master/index.glsl
    public static class SpecularPhong implements SLInclude {

        public SLFunc<SLFloat> phong;
        
        @SuppressWarnings("unchecked")
        @Override
        public void include(ShaderLanguage sl) {
            String body =
                    "  vec3 R = -reflect(lightDirection, surfaceNormal);\n" + 
                    "  return pow(max(0.0, dot(viewDirection, R)), shininess);";
            
            phong = sl.slFunc(body, SLFloat.class,
                arg("vec3","lightDirection"),
                arg("vec3","viewDirection"),
                arg("vec3","surfaceNormal"),
                arg("float","shininess")
            );
        }
    }

	// Perturb-normal
    // https://github.com/glslify/glsl-perturb-normal/blob/master/index.glsl
    public static class PerturbNormal implements SLInclude {

        public SLFunc<SLVec3> perturb;
        public SLFunc<SLMat3> cotangent;
        
        @SuppressWarnings("unchecked")
        @Override
        public void include(ShaderLanguage sl) {
         // Cotangent
            // http://www.thetenthplanet.de/archives/1180
            
            String cotangentBody =
                    // get edge vectors of the pixel triangle
                    "  vec3 dp1 = dFdx(p);\n" + 
                    "  vec3 dp2 = dFdy(p);\n" + 
                    "  vec2 duv1 = dFdx(uv);\n" + 
                    "  vec2 duv2 = dFdy(uv);\n" + 
                    // solve the linear system
                    "  vec3 dp2perp = cross(dp2, N);\n" + 
                    "  vec3 dp1perp = cross(N, dp1);\n" + 
                    "  vec3 T = dp2perp * duv1.x + dp1perp * duv2.x;\n" + 
                    "  vec3 B = dp2perp * duv1.y + dp1perp * duv2.y;\n" + 
                    
                    // construct a scale-invariant frame
                    "  float invmax = 1.0 / sqrt(max(dot(T,T), dot(B,B)));\n" + 
                    "  return mat3(T * invmax, B * invmax, N);";
            
            cotangent = sl.slFunc(cotangentBody, SLMat3.class,
                arg("vec3","N"),
                arg("vec3","p"),
                arg("vec2","uv")
            );
            
            
            // Perturb
            
            String perturbBody =
                    "  mat3 TBN = "+cotangent+"(N, -V, texcoord);\n" + 
                    "  return normalize(TBN * map);";
            
            perturb = sl.slFunc(perturbBody, SLVec3.class,
                arg("vec3","map"),
                arg("vec3","N"),
                arg("vec3","V"),
                arg("vec2","texcoord")
            );
        }
    }
    
	// Attenuation-normal
    // https://github.com/glslify/glsl-Attenuation-normal/blob/master/index.glsl
    public static class Attenuation implements SLInclude {

        public SLFunc<SLFloat> attenuation;
        
        @SuppressWarnings("unchecked")
        @Override
        public void include(ShaderLanguage sl) {
            // Attenuation
            // https://github.com/stackgl/glsl-lighting-walkthrough/blob/master/lib/shaders/madams-attenuation.glsl
            
            String body =
                    "  float denom = d / r + 1.0;\n" + 
                    "  float attenuation = 1.0 / (denom*denom);\n" + 
                    "  float t = (attenuation - f) / (1.0 - f);\n" + 
                    "  return max(t, 0.0);";
            
            attenuation = sl.slFunc(body, SLFloat.class,
                arg("float","r"),
                arg("float","f"),
                arg("float","d")
            );
        }
    }
    
}
