package jonl.ge;

class ShaderGeneratorBasic {    
    
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************            FRAGMENT SHADER             ********************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    
    static String getFragSource(Material material) {
        StringBuilder sb = new StringBuilder();
        sb.append(fragVersion());
        sb.append(fragStructLight());
        sb.append(fragIn(material));
        sb.append(fragUniforms(material));
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
    
    private static String _____vec3_materialDiffuse_E_X(Material material) {
        if (material.diffuse!=null)
            return "    vec3 materialDiffuse = "+material.diffuse.getName()+";\n";
        return "    vec3 materialDiffuse = vec3(0,0,0);\n";
    }
    
    private static String _____getShaderFunctionString(Material material) {
        return material.mbStatements;
    }
    
    private static String fragMainBody(Material material) {
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
    
    static String getVertSource(Material material) {
        return ShaderGeneratorStandard.getVertSource(material);
    }
    
}
