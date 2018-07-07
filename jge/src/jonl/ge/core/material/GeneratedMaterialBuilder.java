package jonl.ge.core.material;

import java.util.ArrayList;
import java.util.HashMap;

//TODO calculate values before rendering for peformance?
/**
 * Shader Material Builder
 * @author Jonathan
 *
 */
public class GeneratedMaterialBuilder extends ShaderLanguage {
    
    public enum SLShader {
        
        /**
         * Standard lit material using diffuse, specular, normal, height,
         * roughness, fresnel
         */
        STANDARD,
        
        /**
         * Unlit material using only diffuse value to render a solid
         * color
         */
        BASIC;
        
    }
    
    
    
    /* ************************************************************** */
    /* *********************  Shader Variables   ******************** */
    /* ************************************************************** */
    
    /*
     * Note:
     * Adding a variable here means you should add it to calculating
     * the unique material id in getProgramString
     */
    
    public SLShader shader      = SLShader.STANDARD;
    
    public SLVec3   diffuse     = null;
    public SLVec3   specular    = null;
    public SLVec3   normal      = null;
    public SLTexU   height      = null;
    public SLFloat  roughness   = null;
    public SLFloat  fresnel     = null;
    
    /* ************************************************************** */
    /* ********************  End Shader Variables  ****************** */
    /* ************************************************************** */
    
    public GeneratedMaterialBuilder() {
        super();
    }
    
    
    public GeneratedMaterial build() {
        GeneratedMaterial mat = new GeneratedMaterial(0);
        apply(mat);
        return mat;
    }
    
    void apply(GeneratedMaterial gm) {
        String id = "_gm_build_"+unique_gm_id+"_"+unique_gm_changed+"_";
        
        gm.id = id;
        gm.shader = shader;
        
        gm.diffuse = copyData(diffuse);
        gm.specular = copyData(specular);
        gm.normal = copyData(normal);
        gm.height = copyData(height);
        gm.roughness = copyData(roughness);
        gm.fresnel = copyData(fresnel);
        
        gm.slStatements = getStatementString(getStatementList());
        gm.slFunctions = getFunctionString(getFunctionList());
        gm.slUniforms = getUniformString(getUniformStringList());
        gm.slUniformList = new ArrayList<>();
        gm.slUniformMap = new HashMap<>();
        addUniformListAndMap(gm.slUniformList, gm.slUniformMap);
        
    }
    
    
    public SLVec4 sample(SLTexU u) {
    	return this.sample(u, "fTexCoord");
    }
    
    
    
    
    
    
}
