package jonl.ge.utils;

import jonl.ge.ShaderLanguage;
import jonl.ge.ShaderLanguage.SLFloat;
import jonl.ge.ShaderLanguage.SLFunc;
import jonl.ge.ShaderLanguage.SLInclude;
import jonl.ge.ShaderLanguage.SLIncludeLibrary;
import jonl.ge.ShaderLanguage.SLMat3;
import jonl.ge.ShaderLanguage.SLVec2;
import jonl.ge.ShaderLanguage.SLVec3;
import jonl.ge.ShaderLanguage.SLVec4;
import jonl.jutils.func.Tuple2;

public class SLImports {

	private static Tuple2<String,String> arg(String type, String name) {
        return new Tuple2<String,String>(type,name);
    }
	
	// GLSL Gamma
	// https://github.com/glslify/glsl-gamma
	
	public static class GLSLGamma implements SLIncludeLibrary {
		
		GLSLGamma(
				SLFunc<SLFloat> toLinear,
				SLFunc<SLVec2> toLinearVec2,
				SLFunc<SLVec3> toLinearVec3,
				SLFunc<SLVec4> toLinearVec4,
				SLFunc<SLFloat> toGamma,
				SLFunc<SLVec2> toGammaVec2,
				SLFunc<SLVec3> toGammaVec3,
				SLFunc<SLVec4> toGammaVec4
		) {
			this.toLinear = toLinear;
			this.toLinearVec2 = toLinearVec2;
			this.toLinearVec3 = toLinearVec3;
			this.toLinearVec4 = toLinearVec4;
			
			this.toGamma = toGamma;
			this.toGammaVec2 = toGammaVec2;
			this.toGammaVec3 = toGammaVec3;
			this.toGammaVec4 = toGammaVec4;
		}
		
		private SLFunc<SLFloat> toLinear;
		private SLFunc<SLVec2> toLinearVec2;
		private SLFunc<SLVec3> toLinearVec3;
		private SLFunc<SLVec4> toLinearVec4;
		
		private SLFunc<SLFloat> toGamma;
		private SLFunc<SLVec2> toGammaVec2;
		private SLFunc<SLVec3> toGammaVec3;
		private SLFunc<SLVec4> toGammaVec4;
		
		public SLFunc<SLFloat> toLinear() { return toLinear; }
		public SLFunc<SLVec2> toLinearVec2() { return toLinearVec2; }
		public SLFunc<SLVec3> toLinearVec3() { return toLinearVec3; }
		public SLFunc<SLVec4> toLinearVec4() { return toLinearVec4; }
		
		public SLFunc<SLFloat> toGamma() { return toGamma; }
		public SLFunc<SLVec2> toGammaVec2() { return toGammaVec2; }
		public SLFunc<SLVec3> toGammaVec3() { return toGammaVec3; }
		public SLFunc<SLVec4> toGammaVec4() { return toGammaVec4; }
		
    }
	private static class GLSLGammaInclude implements SLInclude<GLSLGamma> {
		@SuppressWarnings("unchecked")
		@Override
		public GLSLGamma include(ShaderLanguage sl) {

			SLFloat gamma = sl.slFloatc(2.2f);
	    	
	    	SLFunc<SLFloat> toLinear = sl.slFunc(
    			"return pow(v, "+gamma+");",
    			SLFloat.class, arg("float","v"));
	    	
	    	SLFunc<SLVec2> toLinearVec2 = sl.slFunc(
    			"return pow(v, vec2("+gamma+"));",
    			SLVec2.class, arg("vec2","v"));
	    	
	    	SLFunc<SLVec3> toLinearVec3 = sl.slFunc(
    			"return pow(v, vec3("+gamma+"));",
    			SLVec3.class, arg("vec3","v"));
	    	
	    	SLFunc<SLVec4> toLinearVec4 = sl.slFunc(
    			"return pow(v, vec4("+gamma+"));",
    			SLVec4.class, arg("vec4","v"));
	    	
	    	
	    	SLFunc<SLFloat> toGamma = sl.slFunc(
    			"return pow(v, 1.0 / "+gamma+");",
    			SLFloat.class, arg("float","v"));
	    	
	    	SLFunc<SLVec2> toGammaVec2 = sl.slFunc(
    			"return pow(v, vec2(1.0 / "+gamma+"));",
    			SLVec2.class, arg("vec2","v"));
	    	
	    	SLFunc<SLVec3> toGammaVec3 = sl.slFunc(
    			"return pow(v, vec3(1.0 / "+gamma+"));",
    			SLVec3.class, arg("vec3","v"));
	    	
	    	SLFunc<SLVec4> toGammaVec4 = sl.slFunc(
    			"return pow(v, vec4(1.0 / "+gamma+"));",
    			SLVec4.class, arg("vec4","v"));
	    	
	    	return new GLSLGamma(
    			toLinear,
    			toLinearVec2,
    			toLinearVec3,
    			toLinearVec4,
    			toGamma,
    			toGammaVec2,
    			toGammaVec3,
    			toGammaVec4
			);
		}
	}
	private static GLSLGammaInclude glslGamma = new GLSLGammaInclude();
    public static SLInclude<GLSLGamma> glslGamma() { return glslGamma; }
    
    
    
    
    // Diffuse Oren-Nayer
    // https://github.com/glslify/glsl-diffuse-oren-nayar
	public static class DiffuseOrenNayer implements SLIncludeLibrary {
		DiffuseOrenNayer(SLFunc<SLFloat> orenNayer) {
			this.orenNayer = orenNayer;
		}
		private SLFunc<SLFloat> orenNayer;
		public SLFunc<SLFloat> orenNayer() { return orenNayer; }
	 }
 	private static class DiffuseOrenNayerInclude implements SLInclude<DiffuseOrenNayer> {
 		@SuppressWarnings("unchecked")
 		@Override
 		public DiffuseOrenNayer include(ShaderLanguage sl) {
 			
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
 	        
 	        SLFunc<SLFloat> orenNayer = sl.slFunc(body, SLFloat.class,
 	        		arg("vec3","lightDirection"),
 	        		arg("vec3","viewDirection"),
 	        		arg("vec3","surfaceNormal"),
 	        		arg("float","roughness"),
 	        		arg("float","albedo")
 			);
 	    	
 	    	return new DiffuseOrenNayer(orenNayer);
 		}
 	}
	private static DiffuseOrenNayerInclude orenNayer = new DiffuseOrenNayerInclude();
	public static SLInclude<DiffuseOrenNayer> orenNayer() { return orenNayer; }
	
	
	
	// Specular phong
	// https://github.com/glslify/glsl-specular-phong/blob/master/index.glsl
	public static class SpecularPhong implements SLIncludeLibrary {
		SpecularPhong(SLFunc<SLFloat> phong) {
			this.phong = phong;
		}
		private SLFunc<SLFloat> phong;
		public SLFunc<SLFloat> phong() { return phong; }
	 }
 	private static class SpecularPhongInclude implements SLInclude<SpecularPhong> {
 		@SuppressWarnings("unchecked")
 		@Override
 		public SpecularPhong include(ShaderLanguage sl) {
 			
 			String body =
 					"  vec3 R = -reflect(lightDirection, surfaceNormal);\n" + 
 					"  return pow(max(0.0, dot(viewDirection, R)), shininess);";
 	        
 	        SLFunc<SLFloat> phong = sl.slFunc(body, SLFloat.class,
 	        		arg("vec3","lightDirection"),
 	        		arg("vec3","viewDirection"),
 	        		arg("vec3","surfaceNormal"),
 	        		arg("float","shininess")
 			);
 	    	
 	    	return new SpecularPhong(phong);
 		}
 	}
	private static SpecularPhongInclude phong = new SpecularPhongInclude();
	public static SLInclude<SpecularPhong> phong() { return phong; }
	
	
	
	// Perturb-normal
    // https://github.com/glslify/glsl-perturb-normal/blob/master/index.glsl
	public static class PerturbNormal implements SLIncludeLibrary {
		PerturbNormal(SLFunc<SLVec3> perturb, SLFunc<SLMat3> cotangent) {
			this.perturb = perturb;
			this.cotangent = cotangent;
		}
		private SLFunc<SLVec3> perturb;
		private SLFunc<SLMat3> cotangent;
		public SLFunc<SLVec3> pertub() { return perturb; }
		public SLFunc<SLMat3> cotangent() { return cotangent; }
	 }
 	private static class PerturbNormalInclude implements SLInclude<PerturbNormal> {
 		@SuppressWarnings("unchecked")
 		@Override
 		public PerturbNormal include(ShaderLanguage sl) {
 			
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
 			
 			SLFunc<SLMat3> cotangent = sl.slFunc(cotangentBody, SLMat3.class,
 					arg("vec3","N"),
 					arg("vec3","p"),
 					arg("vec2","uv")
			);
 			
 			
 			// Perturb
 			
 			String perturbBody =
 					"  mat3 TBN = "+cotangent+"(N, -V, texcoord);\n" + 
 					"  return normalize(TBN * map);";
 	        
 	        SLFunc<SLVec3> perturb = sl.slFunc(perturbBody, SLVec3.class,
 	        		arg("vec3","map"),
 	        		arg("vec3","N"),
 	        		arg("vec3","V"),
 	        		arg("vec2","texcoord")
 			);
 	        
 	    	
 	    	return new PerturbNormal(perturb, cotangent);
 		}
 	}
	private static PerturbNormalInclude perturbNormal = new PerturbNormalInclude();
	public static SLInclude<PerturbNormal> perturbNormal() { return perturbNormal; }
	
	
	
	// Attenuation-normal
    // https://github.com/glslify/glsl-Attenuation-normal/blob/master/index.glsl
	
	public static class Attenuation implements SLIncludeLibrary {
		Attenuation(SLFunc<SLFloat> attenuation) {
			this.attenuation = attenuation;
		}
		private SLFunc<SLFloat> attenuation;
		public SLFunc<SLFloat> attenuation() { return attenuation; }
	 }
 	private static class AttenuationInclude implements SLInclude<Attenuation> {
 		@SuppressWarnings("unchecked")
 		@Override
 		public Attenuation include(ShaderLanguage sl) {
 			
 			// Attenuation
 			// https://github.com/stackgl/glsl-lighting-walkthrough/blob/master/lib/shaders/madams-attenuation.glsl
 			
 			String body =
 					"  float denom = d / r + 1.0;\n" + 
 					"  float attenuation = 1.0 / (denom*denom);\n" + 
 					"  float t = (attenuation - f) / (1.0 - f);\n" + 
 					"  return max(t, 0.0);";
 	        
 	        SLFunc<SLFloat> attenuation = sl.slFunc(body, SLFloat.class,
 	        		arg("float","r"),
 	        		arg("float","f"),
 	        		arg("float","d")
 			);
 	    	
 	    	return new Attenuation(attenuation);
 		}
 	}
	private static AttenuationInclude attenuation = new AttenuationInclude();
	public static SLInclude<Attenuation> attenuation() { return attenuation; }
	
	
	
	
}