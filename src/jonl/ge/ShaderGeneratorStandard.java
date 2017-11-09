package jonl.ge;

class ShaderGeneratorStandard {
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************            FRAGMENT SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    static String getFragSource(int version, GeneratedMaterial material) {
        StringBuilder sb = new StringBuilder();
        version(sb,version);
        sb.append('\n');
        sb.append(fragGenerated(material));
        sb.append('\n');
        sb.append(fragStructLight());
        sb.append('\n');
        sb.append(fragIn(material));
        sb.append('\n');
        sb.append(fragUniforms(material));
        sb.append('\n');
        sb.append(fragFunctions(material));
        sb.append('\n');
        sb.append(fragFuncParallaxMapping(material));
        sb.append('\n');
        sb.append(fragMainStart());
        sb.append('\n');
        sb.append(fragMainBody(material));
        sb.append('\n');
        sb.append(fragMainEnd());
        sb.append('\n');
        return sb.toString();
    }
    
    private static void version(StringBuilder sb, int version)   {
        sb.append("#version "+version+"\n");
        if (version<330) {
        sb.append("#extension GL_ARB_explicit_attrib_location : enable \n");
        }
    }
    private static String fragMainStart()            { return "void main() {\n"; }
    private static String fragMainEnd()              { return "}"; }
    
    private static String fragGenerated(GeneratedMaterial material) {
        return
            "// Generated Fragment Shader\n" +
            "// Shader key: "+material.shaderKey()+"\n" +
            "";
    }
    
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
    
    private static String _in_mat3_mTBN(GeneratedMaterial material) {
        if (material.normal!=null || material.height!=null) {
            return "in mat3 mTBN;\n";
        }
        return "";
    }
    
    private static String fragIn(GeneratedMaterial material) {
        return
            "in vec3 vFragPos;\n" +
            "in vec3 vNormal;\n" +
            "in vec2 vTexCoord;\n" +
            "in vec3 vTangent;\n" +
            "in vec3 vBitangent;\n" +
            _in_mat3_mTBN(material) +
            "";
    }
    
    private static String getShaderFunctionUniforms(GeneratedMaterial material) {
        return material.slUniforms;
    }
    
    private static String fragUniforms(GeneratedMaterial material) {
        return
            "uniform vec3 eye;\n" +
            "uniform Light light[8];\n" +
            "uniform int numLights;\n" +
            //"uniform Material material;\n" +
            getShaderFunctionUniforms(material) +
            "";
    }
    
    private static String getShaderFunctions(GeneratedMaterial material) {
        return material.slFunctions;
    }
    
    private static String fragFunctions(GeneratedMaterial material) {
        return
            getShaderFunctions(material) +
            "";
    }
    
    // https://learnopengl.com/#!Advanced-Lighting/Parallax-Mapping
    private static String fragFuncParallaxMapping(GeneratedMaterial material) {
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
    
    private static String _____texCooord_E_parallaxTexCoord_vTexCoord_eyeDir_(GeneratedMaterial material) {
        if (material.height!=null) {
            return "    texCoord = parallaxTexCoord(fTexCoord,eyeDir);\n";
        }
        return "";
    }
    
    private static String _____vec3_materialDiffuse_E_X(GeneratedMaterial material) {
        if (material.diffuse!=null)
            return "    vec3 materialDiffuse = "+material.diffuse.getName()+";\n";
        return "    vec3 materialDiffuse = vec3(0,0,0);\n";
    }
    private static String _____vec3_materialSpecular_E_X(GeneratedMaterial material) {
        if (material.specular!=null)
            return "    vec3 materialSpecular = "+material.specular.getName()+";\n";
        return "    vec3 materialSpecular = vec3(0,0,0);\n";
    }
    private static String _____float_materialRoughness_E_X(GeneratedMaterial material) {
        if (material.roughness!=null)
            return "    float materialRoughness = "+material.roughness.getName()+";\n";
        return "    float materialRoughness = 0.8f;\n";
    }
    private static String _____float_materialFresnel_E_X(GeneratedMaterial material) {
        if (material.fresnel!=null)
            return "    float materialFresnel = "+material.fresnel.getName()+";\n";
        return "    float materialFresnel = 0.3f;\n";
    }
    
    private static String _____normal_E_texture2D_material_normal_vTexCoord_xyz(GeneratedMaterial material) {
        if (material.normal!=null) {
            return "    normal = mTBN * (("+material.normal+"-0.5) * 2);\n";
        }
        return "";
    }
    private static String _____getShaderFunctionString(GeneratedMaterial material) {
        return material.slStatements;
    }
    
    private static String fragMainBody(GeneratedMaterial material) {
        return
            "    vec3 fFragPos = vFragPos;\n" +
            "    vec3 fNormal = vNormal;\n" +
            "    vec2 fTexCoord = vTexCoord;\n" +
            "    vec3 fTangent = vTangent;\n" +
            "    vec3 fBitangent = vBitangent;\n" +
            "    \n" +
            _____getShaderFunctionString(material) +
            "    \n" +
            "    vec3 normal = normalize(fNormal);\n" +
            "    vec3 varEyeDir = eye - fFragPos;\n" +
            "    vec3 eyeDir = normalize(varEyeDir);\n" +
            "    vec2 texCoord = fTexCoord;\n" +
            _____texCooord_E_parallaxTexCoord_vTexCoord_eyeDir_(material) +
            "    \n" +
            _____vec3_materialDiffuse_E_X(material) +
            _____vec3_materialSpecular_E_X(material) +
            _____float_materialRoughness_E_X(material) +
            _____float_materialFresnel_E_X(material) +
            _____normal_E_texture2D_material_normal_vTexCoord_xyz(material) +
            "    \n" +
            "    vec3 lightSum = vec3(0,0,0);\n" +
            "    \n" +
            "    int i=0;\n" +
            "    for (i=0; i<numLights; i++) {\n" +
            "        \n" +
            "        float distFromLight = length(light[i].position - fFragPos);\n" +
            "        \n" +
            "        if (distFromLight > light[i].range)\n" +
            "            continue;\n" +
            "        \n" +
            "        vec3 lightDirection = normalize(light[i].position - fFragPos);\n" +
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
            "        float fresnel = pow(1.0 - VdotH, 3.0);\n" + //changed from 5 to 3, because of black artifacts, why?
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
            "    gl_FragColor = vec4(lightSum, 1.0);\n" +
            "";
    }
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************              VERTEX SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    static String getVertSource(int version, GeneratedMaterial material, boolean instanced) {
        StringBuilder sb = new StringBuilder();
        version(sb,version);
        sb.append('\n');
        sb.append(vertGenerated(material));
        sb.append('\n');
        sb.append(vertIn(version,instanced));
        sb.append('\n');
        sb.append(vertUniforms(instanced));
        sb.append('\n');
        sb.append(vertOut(material,instanced));
        sb.append('\n');
        sb.append(vertFuncUnitTransform(material));
        sb.append('\n');
        sb.append(vertMainStart());
        sb.append('\n');
        sb.append(vertBody(material,instanced));
        sb.append('\n');
        sb.append(vertMainEnd());
        sb.append('\n');
        return sb.toString();
    }
    
    private static String vertMainStart()            { return "void main() {\n"; }
    private static String vertMainEnd()              { return "}"; } 
    
    private static String vertGenerated(GeneratedMaterial material) {
        return
            "// Generated Vertex Shader\n" +
            "// Shader key: "+material.shaderKey()+"\n" +
            "";
    }
    
    static String _layout__location_e_5__in_mat4_MVP(boolean instanced) {
        if (instanced)
            return "layout (location = 5) in mat4 M;\n";
        return "";
    }
    
    private static String vertIn(int version, boolean instanced) {
        if (version>=300) {
            
        } else {
            
        }
        return
            "layout (location = 0) in vec4 vertex;\n" +
            "layout (location = 1) in vec3 normal;\n" +
            "layout (location = 2) in vec2 texCoord;\n" +
            "layout (location = 3) in vec3 tangent;\n" +
            "layout (location = 4) in vec3 bitangent;\n" +
            _layout__location_e_5__in_mat4_MVP(instanced) +
            "";
    }
    
    private static String vertUniforms(boolean instanced) {
        if (instanced) {
            return
                "uniform mat4 VP;\n" +
                "uniform mat4 V;\n" +
                "";
        } else {
            return
                "uniform mat4 MVP;\n" +
                "uniform mat4 M;\n" +
                "uniform mat4 MV;\n" +
                "";
        }
    }
    
    private static String _out_mat3_mTBN(GeneratedMaterial material) {
        if (material.normal!=null || material.height!=null) return "out mat3 mTBN;\n";
        return "";
    }
    
    private static String vertOut(GeneratedMaterial material, boolean instanced) {
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
    private static String vertFuncUnitTransform(GeneratedMaterial material) {
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
                "   float R = 1.0 / (1.0 + B.z);\n" +
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
    
    private static String _____mTBN_E_mat3_vTangent_vBitangent_vNormal_(GeneratedMaterial material) {
        if (material.normal!=null || material.height!=null) {
            return 
                "    mTBN = mat3(vTangent,vBitangent,vNormal);\n";
        }
        return "";
    }
    
    private static String _____MVP_instance(boolean instanced) {
        if (instanced) {
            return
                "    mat4 MVP = VP * M;\n" +
                "";
        } else {
            return
                "";
        }
    }
    
    private static String vertBody(GeneratedMaterial material, boolean instanced) {
        return
            _____MVP_instance(instanced) +
            "    gl_Position = MVP * vertex;\n" +
            "    vFragPos = vec3(M * vertex);\n" +
            "    vNormal = mat3(M) * normal;\n" +
            "    vTexCoord = texCoord;\n" +
            "    vTangent = mat3(M) * tangent;\n" +
            "    vBitangent = mat3(M) * bitangent;\n" +
            _____mTBN_E_mat3_vTangent_vBitangent_vNormal_(material) +
            "";
    }
    
}
