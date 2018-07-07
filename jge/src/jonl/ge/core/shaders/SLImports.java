package jonl.ge.core.shaders;

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
    
    
    // Perlin Noise
    // https://gist.github.com/patriciogonzalezvivo/670c22f3966e662d2f83
    public static class PerlinNoise implements SLInclude {

        public SLFunc<SLFloat> fade;
        public SLFunc<SLFloat> lerp;
        public SLFunc<SLFloat> grad;
        public SLFunc<SLFloat> noise;
        
        @SuppressWarnings("unchecked")
        @Override
        public void include(ShaderLanguage sl) {
            
            sl.putConst("int _p_[512] = int[]( 151,160,137,91,90,15,\n" + 
                    "       131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,\n" + 
                    "       190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,\n" + 
                    "       88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,\n" + 
                    "       77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,\n" + 
                    "       102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,\n" + 
                    "       135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,\n" + 
                    "       5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,\n" + 
                    "       223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,\n" + 
                    "       129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,\n" + 
                    "       251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,\n" + 
                    "       49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,\n" + 
                    "       138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180,\n" +
                    "       151,160,137,91,90,15,\n" + 
                    "       131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,\n" + 
                    "       190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,\n" + 
                    "       88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,\n" + 
                    "       77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,\n" + 
                    "       102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,\n" + 
                    "       135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,\n" + 
                    "       5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,\n" + 
                    "       223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,\n" + 
                    "       129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,\n" + 
                    "       251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,\n" + 
                    "       49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,\n" + 
                    "       138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180 )");
            
            fade = sl.slFunc(
                "return t * t * t * (t * (t * 6 - 15) + 10);",
                SLFloat.class, arg("float","t"));
            
            lerp = sl.slFunc(
                "return a + t * (b - a);",
                SLFloat.class, arg("float","t"), arg("float","a"), arg("float","b"));
            
            grad = sl.slFunc(
                "int h = hash & 15;\n" + // CONVERT LO 4 BITS OF HASH CODE
                "float u = h<8 ? x : y,\n" + // INTO 12 GRADIENT DIRECTIONS.
                "    v = h<4 ? y : h==12||h==14 ? x : z;\n" + 
                "return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);",
                SLFloat.class, arg("int","hash"), arg("float","x"), arg("float","y"), arg("float","z"));
            
            noise = sl.slFunc(
                    "int X = int(floor(x)) & 255,                  // FIND UNIT CUBE THAT\n" + 
                    "Y = int(floor(y)) & 255,                  // CONTAINS POINT.\n" + 
                    "Z = int(floor(z)) & 255;\n" + 
                    "x -= floor(x);                                // FIND RELATIVE X,Y,Z\n" + 
                    "y -= floor(y);                                // OF POINT IN CUBE.\n" + 
                    "z -= floor(z);\n" + 
                    "float u = "+fade+"(x),                                // COMPUTE FADE CURVES\n" + 
                    "v = "+fade+"(y),                                // FOR EACH OF X,Y,Z.\n" + 
                    "w = "+fade+"(z);\n" + 
                    "int A = _p_[X  ]+Y, AA = _p_[A]+Z, AB = _p_[A+1]+Z,      // HASH COORDINATES OF\n" + 
                    "B = _p_[X+1]+Y, BA = _p_[B]+Z, BB = _p_[B+1]+Z;      // THE 8 CUBE CORNERS,\n" + 
                    "\n" + 
                    "          return "+lerp+"(w, "+lerp+"(v, "+lerp+"(u, "+grad+"(_p_[AA  ], x  , y  , z   ),  // AND ADD\n" + 
                    "                                         "+grad+"(_p_[BA  ], x-1, y  , z   )), // BLENDED\n" + 
                    "                                 "+lerp+"(u, "+grad+"(_p_[AB  ], x  , y-1, z   ),  // RESULTS\n" + 
                    "                                         "+grad+"(_p_[BB  ], x-1, y-1, z   ))),// FROM  8\n" + 
                    "                         "+lerp+"(v, "+lerp+"(u, "+grad+"(_p_[AA+1], x  , y  , z-1 ),  // CORNERS\n" + 
                    "                                         "+grad+"(_p_[BA+1], x-1, y  , z-1 )), // OF CUBE\n" + 
                    "                                 "+lerp+"(u, "+grad+"(_p_[AB+1], x  , y-1, z-1 ),\n" + 
                    "                                         "+grad+"(_p_[BB+1], x-1, y-1, z-1 ))));\n",
                    SLFloat.class, arg("float","x"), arg("float","y"), arg("float","z"));
            
        }
        
    }
    
    
}
