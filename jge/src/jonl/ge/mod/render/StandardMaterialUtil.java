package jonl.ge.mod.render;

import jonl.ge.core.material.ShaderLanguage;
import jonl.ge.core.material.ShaderLanguage.SLArray;
import jonl.ge.core.material.ShaderLanguage.SLDeclare;
import jonl.ge.core.material.ShaderLanguage.SLFloat;
import jonl.ge.core.material.ShaderLanguage.SLInt;
import jonl.ge.core.material.ShaderLanguage.SLStruct;
import jonl.ge.core.material.ShaderLanguage.SLStructBuilder;
import jonl.ge.core.material.ShaderLanguage.SLVec3;
import jonl.ge.core.material.ShaderLanguage.SLVec4;
import jonl.ge.core.shaders.SLImports.Attenuation;
import jonl.ge.core.shaders.SLImports.DiffuseOrenNayer;
import jonl.ge.core.shaders.SLImports.GLSLGamma;
import jonl.ge.core.shaders.SLImports.SpecularPhong;

public class StandardMaterialUtil {

    public static SLVec3 light(ShaderLanguage sl, SLVec3 vPosition, SLVec3 eye, SLInt numLights, SLVec3 fDiffuse, SLVec3 fNormal, SLVec3 fSpecular) {
        // Declares
        // ================================================================================================== //
        
        DiffuseOrenNayer orenNayer = sl.include(new DiffuseOrenNayer());
        SpecularPhong phong = sl.include(new SpecularPhong());
        Attenuation attenuation = sl.include(new Attenuation());
        
        SLDeclare<SLLight> Light = sl.declare(() -> new SLLight());
        
        int MAX_NUM_LIGHTS = 5;
        float specularScale = 3f;
        float shininess = 20.0f;
        float roughness = 0.1f;
        float albedo = 0.95f;
        
        SLArray<SLLight> array = sl.arrayu(Light, "light", MAX_NUM_LIGHTS);
        
        // Main
        // ================================================================================================== //
        
        // Light-invariant variables
        SLVec3 eyeVector = sl.sub( eye, vPosition );
        SLVec3 finalColor = sl.vec3(0f);
        SLVec3 V = sl.normalize(eyeVector);
        
        // Multi-light rendering
        SLInt i = sl.slLoop(0,numLights,1); {
            
            SLLight light = sl.index(array, i);
            
            // Point light
            sl.slIf(light.type+"=="+1); {
                
                SLVec4 lightPosition = sl.vec4(light.position,1f);
                SLVec3 lightVector = sl.sub( lightPosition.xyz() , vPosition );
                
                SLFloat lightDistance = sl.length(lightVector);
                SLFloat falloff = sl.call(attenuation.attenuation, light.radius, light.falloff, lightDistance);
                
                SLVec3 L = sl.normalize(lightVector);
                
                SLVec3 specular = sl.mul(fSpecular, sl.call(phong.phong, L, V, fNormal, shininess), specularScale, falloff);
                SLVec3 diffuse = sl.mul(light.color, sl.call(orenNayer.orenNayer, L, V, fNormal, roughness, albedo), falloff);
                SLVec3 ambient = light.ambient;
                
                sl.set(finalColor, sl.add(finalColor, sl.add(fDiffuse.mul(sl.add(diffuse, ambient)), specular)));
                
            }
            // Spot light
            sl.slElseIf(light.type+"=="+2); {
                
            }
            // Directional light
            sl.slElseIf(light.type+"=="+3); {
                
                // Same as point light but direction is set by light and there is no falloff
                
                SLVec3 lightVector = sl.neg(light.direction);
                
                SLVec3 L = sl.normalize(lightVector);
                
                SLVec3 specular = sl.mul(fSpecular, sl.call(phong.phong, L, V, fNormal, shininess), specularScale);
                SLVec3 diffuse = sl.mul(light.color, sl.call(orenNayer.orenNayer, L, V, fNormal, roughness, albedo));
                SLVec3 ambient = light.ambient;
                
                sl.set(finalColor, sl.add(finalColor, sl.add(fDiffuse.mul(sl.add(diffuse, ambient)), specular)));
                
            }
            sl.slEndIf();
            
        }
        sl.slEndLoop();
        
        return finalColor;
    }
    
    public static void fragment(ShaderLanguage sl, SLVec3 vPosition, SLVec3 eye, SLInt numLights, SLVec3 fDiffuse, SLVec3 fNormal, SLVec3 fSpecular) {
        GLSLGamma glslGamma = sl.include(new GLSLGamma());
        
        SLVec3 finalColor = light(sl,vPosition,eye,numLights,fDiffuse,fNormal,fSpecular);
        
        finalColor = sl.call(glslGamma.toGammaVec3, finalColor);
        
        sl.gl_FragColor(sl.vec4(finalColor, 1f));
    }
    
    
    public static class SLLight extends SLStruct {
        SLInt type;
        SLVec3 position;
        SLVec3 color;
        SLVec3 ambient;
        SLFloat falloff;
        SLFloat radius;
        SLVec3 direction;
        @Override
        public void init(SLStructBuilder sb) {
            type = sb.slInt("type");
            position = sb.vec3("position");
            color = sb.vec3("color");
            ambient = sb.vec3("ambient");
            falloff = sb.slFloat("falloff");
            radius = sb.slFloat("radius");
            direction = sb.vec3("direction");
        }
    }
    
}
