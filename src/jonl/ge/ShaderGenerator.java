package jonl.ge;

import java.util.HashMap;

import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Program;
import jonl.vmath.Matrix2;
import jonl.vmath.Matrix3;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

class ShaderGenerator {
    
    private HashMap<String,Program> programMap = new HashMap<>();
    
    private GraphicsLibrary gl;
    
    ShaderGenerator(GraphicsLibrary gl) {
        this.gl = gl;
    }
    
    /** Every type of material must have a unique string */
    private static String getProgramString(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append("mat"+material.id); //TODO dont create program for every material instance?
        return sb.toString();
    }
    
    Program getOrCreateProgram(Material material) {
        String string = getProgramString(material);
        Program glprogram = programMap.get(string);
        if (glprogram==null) {
            String fragSource = getFragSource(material);
            String vertSource = getVertSource(material);
            glprogram = AppUtil.createProgramFromSource(gl,vertSource,fragSource);
            programMap.put(string,glprogram);
        }
        return glprogram;
    }
    
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************            FRAGMENT SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    private static String getFragSource(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append(fragVersion());
        sb.append(fragStructLight());
        sb.append(fragIn(material));
        sb.append(fragUniforms(material));
        sb.append(fragFuncParallaxMapping(material));
        sb.append(fragMainStart());
        sb.append(fragMainBody(material));
        sb.append(fragMainEnd());
        return sb.toString();
    }
    
    private static String fragVersion()     { return "#version 430\n"; }
    private static String fragMainStart()   { return "void main() {\n"; }
    private static String fragMainEnd()     { return "}"; }
    
    private static String fragStructLight() {
        return 
            "struct Light {\n" +
            "    int type;\n" +
            "    vec3 position;\n" +
            "    float range;\n" +
            "    vec3 color;\n" +
            "    float intensity;\n" +
            "    float angle;\n" +
            "};\n" +
            "";
    }
    
    private static String _in_mat3_mTBN(Material material) {
        if (material.normal!=null || material.height!=null) {
            return "in mat3 mTBN;\n";
        }
        return "";
    }
    
    private static String fragIn(Material material) {
        return
            "in vec3 vFragPos;\n" +
            "in vec3 vNormal;\n" +
            "in vec2 vTexCoord;\n" +
            "in vec3 vTangent;\n" +
            "in vec3 vBitangent;\n" +
            _in_mat3_mTBN(material) +
            "";
    }
    
    private static String getShaderFunctionUniforms(Material material) {
        return material.mbUniforms;
    }
    
    private static String fragUniforms(Material material) {
        return
            "uniform vec3 eye;\n" +
            "uniform Light light[8];\n" +
            "uniform int numLights;\n" +
            //"uniform Material material;\n" +
            getShaderFunctionUniforms(material) +
            "";
    }
    
    // https://learnopengl.com/#!Advanced-Lighting/Parallax-Mapping
    private static String fragFuncParallaxMapping(Material material) {
        if (material.height!=null) {
            return
                "vec2 parallaxTexCoord(vec2 texCoords, vec3 eyeDir) {\n" +
                "    mat3 iTBN = inverse(mTBN);\n" +
                "    vec3 viewDir = iTBN * eyeDir;\n" +
                "    const float minLayers = 8;\n" +
                "    const float maxLayers = 32;\n" +
                "    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));\n" +
                "    float layerDepth = 1.0 / numLayers;\n" +
                "    float currentLayerDepth = 0.0;\n" +
                
                "    float height =  texture("+material.height+", texCoords).r;\n" +
                "    vec2 p = viewDir.xy * (0.5);\n" + //TODO replace 1 with height scale
                
                "    vec2 deltaTexCoords = p / numLayers;\n" +
                "    vec2 currentTexCoords = texCoords;\n" +
                "    float currentDepthMapValue = texture("+material.height+", currentTexCoords).r;\n" +
                "    while (currentLayerDepth < currentDepthMapValue) {\n" +
                "        currentTexCoords += deltaTexCoords;\n" +
                "        currentDepthMapValue -= texture("+material.height+", currentTexCoords).r;\n" +
                "        currentLayerDepth += layerDepth;\n" +
                "    }\n" +
                "    vec2 prevTexCoords = currentTexCoords + deltaTexCoords;\n" +
                "    float afterDepth = currentDepthMapValue - currentLayerDepth;\n" +
                "    float beforeDepth = texture("+material.height+",prevTexCoords).r - currentLayerDepth + layerDepth;\n" +
                "    float weight = afterDepth / (afterDepth - beforeDepth);\n" +
                "    vec2 finalTexCoords = prevTexCoords * weight + currentTexCoords * (1.0 - weight);\n" +
                "    return finalTexCoords;\n" +
                "}\n" +
                "";
            }
        return "";
    }
    
    private static String _____texCooord_E_parallaxTexCoord_vTexCoord_eyeDir_(Material material) {
        if (material.height!=null) {
            return "    texCoord = parallaxTexCoord(vTexCoord,eyeDir);\n";
        }
        return "";
    }
    
    private static String _____vec3_materialDiffuse_E_X(Material material) {
        if (material.diffuse!=null)
            return "    vec3 materialDiffuse = "+material.diffuse.getName()+";\n";
        return "    vec3 materialDiffuse = vec3(0,0,0);\n";
    }
    private static String _____vec3_materialSpecular_E_X(Material material) {
        if (material.specular!=null)
            return "    vec3 materialSpecular = "+material.specular.getName()+";\n";
        return "    vec3 materialSpecular = vec3(0,0,0);\n";
    }
    private static String _____float_materialRoughness_E_X(Material material) {
        if (material.roughness!=null)
            return "    float materialRoughness = "+material.roughness.getName()+";\n";
        return "    float materialRoughness = 0.8f;\n";
    }
    private static String _____float_materialFresnel_E_X(Material material) {
        if (material.fresnel!=null)
            return "    float materialFresnel = "+material.fresnel.getName()+";\n";
        return "    float materialFresnel = 0.3f;\n";
    }
    
    private static String _____normal_E_texture2D_material_normal_vTexCoord_xyz(Material material) {
        if (material.normal!=null) {
            return "    normal = mTBN * (("+material.normal+"-0.5) * 2);\n";
        }
        return "";
    }
    /*
    private static String tempMethod(Material material) {
        if (material.normal!=null) {
            return 
                "    vec3 normalx = mTBN * ((texture2D(material.normal,texCoord).xyz-0.5) * 2);\n" +
                "    gl_FragColor = vec4(normalx,1)*0.5 + 0.5;\n" +
                "";
            //return "    gl_FragColor = vec4(vNormal*0.5+0.5, 1.0);\n";
        }
        return "";
    }
    */
    private static String _____getShaderFunctionString(Material material) {
        return material.mbStatements;
    }
    
    private static String fragMainBody(Material material) {
        return
            "    vec3 normal = normalize(vNormal);\n" +
            "    vec3 varEyeDir = eye - vFragPos;\n" +
            "    vec3 eyeDir = normalize(varEyeDir);\n" +
            "    vec2 texCoord = vTexCoord;\n" +
            _____texCooord_E_parallaxTexCoord_vTexCoord_eyeDir_(material) +
            _____getShaderFunctionString(material) +
            _____vec3_materialDiffuse_E_X(material) +
            _____vec3_materialSpecular_E_X(material) +
            _____float_materialRoughness_E_X(material) +
            _____float_materialFresnel_E_X(material) +
            _____normal_E_texture2D_material_normal_vTexCoord_xyz(material) +
            "    vec3 lightSum = vec3(0,0,0);\n" +
            "    \n" +
            "    int i=0;\n" +
            "    for (i=0; i<numLights; i++) {\n" +
            "        \n" +
            "        float distFromLight = length(light[i].position - vFragPos);\n" +
            "        \n" +
            "        if (distFromLight > light[i].range)\n" +
            "            continue;\n" +
            "        \n" +
            "        vec3 lightDirection = normalize(light[i].position - vFragPos);\n" +
            "        float NdotL = max(dot(normal, lightDirection), 0.0);\n" +
            "        \n" +
            "        if (NdotL <= 0.0)\n" +
            "            continue;\n" +
            "        \n" +
            "        float ratio = max(1-distFromLight/light[i].range,0);\n" +
            "        \n" +
            "        // calculate intermediary values\n" +
            "        vec3 halfVector = normalize(lightDirection + eyeDir);\n" +
            "        float NdotH = max(dot(normal, halfVector), 0.0); \n" +
            "        float VdotH = max(dot(eyeDir, halfVector), 0.0);\n" +
            "        float mSquared = materialRoughness * materialRoughness;\n" +
            "        float NdotHSquared = NdotH * NdotH;\n" +
            "        \n" +
            "        // roughness (or: microfacet distribution function)\n" +
            "        // beckmann distribution function\n" +
            "        float r1 = 1.0 / ( 4.0 * mSquared * pow(NdotH, 4.0));\n" +
            "        float r2 = (NdotHSquared - 1.0) / (mSquared * NdotHSquared);\n" +
            "        float roughness = r1 * exp(r2);\n" +
            "        \n" +
            "        // Schlick approximation\n" +
            "        float fresnel = pow(1.0 - VdotH, 5.0);\n" +
            "        fresnel *= (1.0 - materialFresnel);\n" +
            "        fresnel += materialFresnel;\n" +
            "        \n" +
            "        float specular = fresnel * roughness;\n" +
            "        \n" +
            "        NdotL *= ratio;\n" +
            "        \n" +
            "        vec3 diffuse = NdotL * materialDiffuse;\n" +
            "        //float specularC = specular * materialSpecular;\n" +
            "        \n" +
            "        vec3 finalValue = NdotL*light[i].color * ( materialSpecular*specular + diffuse );\n" +
            "        lightSum += finalValue;\n" +
            "        \n" +
            "    }\n" +
            "    gl_FragColor = vec4(gl_FragCoord.x/1024,gl_FragCoord.y/576,gl_FragColor.z,1);\n" +
            "    gl_FragColor = vec4(lightSum, 1.0);\n" +
            //tempMethod(material) +
            "";
    }
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************              VERTEX SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    private static String getVertSource(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append(vertVersion());
        sb.append(vertIn());
        sb.append(vertUniforms());
        sb.append(vertOut(material));
        sb.append(vertFuncUnitTransform(material));
        sb.append(vertMainStart());
        sb.append(vertBody(material));
        sb.append(vertMainEnd());
        return sb.toString();
    }
    
    private static String vertVersion()     { return "#version 430\n"; }
    private static String vertMainStart()   { return "void main() {\n"; }
    private static String vertMainEnd()     { return "}"; } 
    
    private static String vertIn() {
        return
            "layout (location = 0) in vec4 vertex;\n" +
            "layout (location = 1) in vec3 normal;\n" +
            "layout (location = 2) in vec2 texCoord;\n" +
            "layout (location = 3) in vec3 tangent;\n" +
            "layout (location = 4) in vec3 bitangent;\n" +
            "";
    }
    
    private static String vertUniforms() {
        return
            "uniform mat4 MVP;\n" +
            "uniform mat4 M;\n" +
            "uniform mat4 MV;\n" +
            "";
    }
    
    private static String _out_mat3_mTBN(Material material) {
        if (material.normal!=null || material.height!=null) return "out mat3 mTBN;\n";
        return "";
    }
    
    private static String vertOut(Material material) {
        return
            "out vec3 vFragPos;\n" +
            "out vec3 vNormal;\n" +
            "out vec2 vTexCoord;\n" +
            "out vec3 vTangent;\n" +
            "out vec3 vBitangent;\n" +
            _out_mat3_mTBN(material) +
            "";
    }
    
    // http://math.stackexchange.com/questions/180418/calculate-rotation-matrix-to-align-vector-a-to-vector-b-in-3d
    // Gives rotation that transforms A(0,0,1) onto B (where B is a unit vector)
    // Hardcode A = 0,0,1 and optimize
    // - This shader function does not work for all test cases (where cross(a,B)==0),
    //   so I handle this by providing the correct matrices in the scenario where A = (0,0,1)
    //TODO replace this with another function (transforming vectors close to (0,0,-1) results in slightly
    //     incorrect values
    private static String vertFuncUnitTransform(Material material) {
        if (material.normal!=null || material.height!=null) {
            return
                "mat3 unitTransform(vec3 B) {\n" +
                "   vec3 V = vec3(-B.y,-B.x,0);\n" +
                "   if (V.x==0 && V.y==0 && V.z==0) {\n" +
                "       if (B.x==0 && B.y==0 && B.z==1) return mat3(1);\n" +
                "       return mat3(vec3(-1,0,0)," +
                "                   vec3(0,1,0)," +
                "                   vec3(0,0,-1));\n" +
                "   }\n" +
                "   float R = 1f / (1f + B.z);\n" +
                "   mat3 M;\n" +
                "   M[0] = vec3(  0,    0, V.y);\n" +
                "   M[1] = vec3(  0,    0,  V.x);\n" +
                "   M[2] = vec3(-V.y, -V.x,    0);\n" +
                "   return mat3(1) + M + M*M*R;\n" +
                "}\n" +
                "";
        }
        return "";
    }
    
    private static String _____mTBN_E_mat3_vTangent_vBitangent_vNormal_(Material material) {
        if (material.normal!=null || material.height!=null) {
            return 
                "    mTBN = mat3(vTangent,vBitangent,vNormal);\n";
        }
        return "";
    }
    
    private static String vertBody(Material material) {
        return
            "    gl_Position = MVP * vertex;\n" +
            "    vFragPos = vec3(M * vertex);\n" +
            "    vNormal = mat3(M) * normal;\n" +
            "    vTexCoord = texCoord;\n" +
            "    vTangent = mat3(M) * tangent;\n" +
            "    vBitangent = mat3(M) * bitangent;\n" +
            _____mTBN_E_mat3_vTangent_vBitangent_vNormal_(material) +
            "";
    }
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************            ShaderGenerator             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    
    private static HashMap<Class<?>,String> typeMap = new HashMap<>();
    static {
        typeMap.put(Texture.class,"sampler2D");
        typeMap.put(Matrix4.class,"mat4");
        typeMap.put(Matrix3.class,"mat3");
        typeMap.put(Matrix2.class,"mat2");
        typeMap.put(Vector4.class,"vec4");
        typeMap.put(Vector3.class,"vec3");
        typeMap.put(Vector2.class,"vec2");
    }
    
}
