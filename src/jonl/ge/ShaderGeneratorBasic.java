package jonl.ge;

class ShaderGeneratorBasic {    
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************            FRAGMENT SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    static String getFragSource(int version, GeneratedMaterial material) {
        StringBuilder sb = new StringBuilder();
        version(sb,version);
        sb.append(fragStructLight());
        sb.append(fragIn(material));
        sb.append(fragUniforms(material));
        sb.append(fragMainStart());
        sb.append(fragMainBody(material));
        sb.append(fragMainEnd());
        return sb.toString();
    }
    
    private static void version(StringBuilder sb, int version) {
        sb.append("#version "+version+"\n");
        if (version<330) {
        sb.append("#extension GL_ARB_explicit_attrib_location : enable \n");
        }
    }
    private static String fragMainStart()               { return "void main() {\n"; }
    private static String fragMainEnd()                 { return "}"; }
    
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
        return material.mbUniforms;
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
    
    private static String _____vec3_materialDiffuse_E_X(GeneratedMaterial material) {
        if (material.diffuse!=null)
            return "    vec3 materialDiffuse = "+material.diffuse.getName()+";\n";
        return "    vec3 materialDiffuse = vec3(0,0,0);\n";
    }
    
    private static String _____getShaderFunctionString(GeneratedMaterial material) {
        return material.mbStatements;
    }
    
    private static String fragMainBody(GeneratedMaterial material) {
        return
            "    vec3 normal = normalize(vNormal);\n" +
            "    vec3 varEyeDir = eye - vFragPos;\n" +
            "    vec3 eyeDir = normalize(varEyeDir);\n" +
            "    vec2 texCoord = vTexCoord;\n" +
            _____getShaderFunctionString(material) +
            _____vec3_materialDiffuse_E_X(material) +
            "    gl_FragColor = vec4(materialDiffuse, 1.0);\n" +
            "";
    }
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************              VERTEX SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    static String getVertSource(int version, GeneratedMaterial material, boolean instanced) {
        return ShaderGeneratorStandard.getVertSource(version, material, instanced);
    }
    
}
