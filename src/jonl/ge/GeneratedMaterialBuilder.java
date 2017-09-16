package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.vmath.Matrix2;
import jonl.vmath.Matrix3;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

//TODO calculate values before rendering for peformance?
/**
 * Shader Material Builder
 * @author Jonathan Lacombe
 *
 */
public class GeneratedMaterialBuilder {
    
    public enum MBShader {
        
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
    
    public MBShader shader      = MBShader.STANDARD;
    
    public MBVec3   diffuse     = null;
    public MBVec3   specular    = null;
    public MBVec3   normal      = null;
    public MBTexU   height      = null;
    public MBFloat  roughness   = null;
    public MBFloat  fresnel     = null;
    
    /* ************************************************************** */
    /* ********************  End Shader Variables  ****************** */
    /* ************************************************************** */
    
    
    
    /** List of Uniforms */
    final ArrayList<MBUniform> mbUniformList = new ArrayList<>();
    private int textureCount;
    
    /** List of Statements to be executed in order */
    private final ArrayList<String> mbStatementList = new ArrayList<>();
    private int variableCount = 0;
    
    
    //To id unique generated materials we use a static unique_gm for every GMBuilder
    //with another int everytime the GMB is changed and return _gm_[unique_gm_id]_[unique_gm_changed]
    //This is so that only one shader is generated for every unique GM
    int unique_gm_id = 0;
    int unique_gm_changed = 0;
    static int unique_gm_count = 0;
    
    public GeneratedMaterialBuilder() {
        unique_gm_id = unique_gm_count++;
    }
    
    
    public GeneratedMaterial build() {
        GeneratedMaterial mat = new GeneratedMaterial(Construct.UNINITIALIZED);
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
        gm.fresnel = copyData(fresnel)
                ;
        gm.mbStatements = gm.getStatementString(mbStatementList);
        gm.mbUniforms = gm.getUniformString(mbUniformList);
        gm.mbUniformList = new ArrayList<>();
        gm.mbUniformMap = new HashMap<>();
        for (MBUniform mbu : mbUniformList) {
            MBUniform uniform = mbu.copy();
            gm.mbUniformList.add(uniform);
            gm.mbUniformMap.put(uniform.id,uniform);
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T extends MBData> T copyData(T md) {
        if (md==null) {
            return null;
        }
        if (md instanceof MBUniform) {
            MBUniform mbu = (MBUniform) md;
            return (T) mbu.copy();
        }
        return md;
    }
    
    
    /**
     * @return a unique name for a generated variable
     */
    private String genName() {
        String name = "_gmv_"+variableCount;
        variableCount++;
        return name;
    }
    
    // -----------------------------------------------------------------------------
    // SHADER MODIFICATION
    
    /** Sets this variable to the given value */
    private <T extends MBVar> T putVariable(T v, String value) {
        unique_gm_changed++;
        v.mb = this;
        v.type = getType(v);
        v.name = genName();
        v.value = value;
        String declaration = v.type+" "+v.name+" = "+v.value+";\n";
        mbStatementList.add(declaration);
        return v;
    }
    
    private <T extends MBUniform> T putUniform(T v, String id, Object data) {
        unique_gm_changed++;
        v.mb = this;
        v.type = getType(v);
        v.name = genName();
        v.id = id;
        v.data = data;
        mbUniformList.add(v);
        return v;
    }
    
    private void putString(String string) {
        unique_gm_changed++;
        mbStatementList.add(string);
    }
    
    // -----------------------------------------------------------------------------
    
    
    
    
    private void putIf(MBBool b)        { putString("if ("+b.getName()+") {\n"); }
    private void putElseIf(MBBool b)    { putString("else if ("+b.getName()+") {\n"); }
    private void putElse()              { putString("else {\n"); }
    private void putEndIf()             { putString("}\n"); }
    
    public void mbIf(MBBool bool)       { putIf(bool); }
    public void mbElseIf(MBBool bool)   { putElseIf(bool); }
    public void mbElse()                { putElse(); }
    public void mbEndIf()               { putEndIf(); }
    
    public MBBool   mbBool(String var)  { return putVariable(new MBBoolV(),     var); }
    public MBInt    mbInt(String var)   { return putVariable(new MBIntV(),      var); }
    public MBFloat  mbFloat(String var) { return putVariable(new MBFloatV(),    var); }
    public MBVec4   vec4(String var)    { return putVariable(new MBVec4V(),     var); }
    public MBVec3   vec3(String var)    { return putVariable(new MBVec3V(),     var); }
    public MBVec2   vec2(String var)    { return putVariable(new MBVec2V(),     var); }
    
    public MBBool   mbBool  (boolean b) { return putVariable(new MBBoolV(),  b+""); }
    public MBInt    mbInt   (int i)     { return putVariable(new MBIntV(),   i+""); }
    public MBFloat  mbFloat (float f)   { return putVariable(new MBFloatV(), f+""); }
    
    public MBVec4 sample(MBTexU u) {
        return putVariable(new MBVec4V(),"texture2D("+u.name+",texCoord)");
    }
    
    private MBVec4 vec4p(Object... params) {
        return putVariable(new MBVec4V(),funcBuild("vec4",params));
    }
    public MBVec4 vec4(MBVec3 u, float w)                           { return vec4p(u,w); }
    public MBVec4 vec4(MBVec2 u, float z, float w)                  { return vec4p(u,z,w); }
    public MBVec4 vec4(float x, float y, float z, float w)          { return vec4p(x,y,z,w); }
    public MBVec4 vec4(MBVec3 u, MBFloat w)                         { return vec4p(u,w); }
    public MBVec4 vec4(MBVec2 u, MBFloat z, MBFloat w)              { return vec4p(u,z,w); }
    public MBVec4 vec4(MBFloat x, MBFloat y, MBFloat z, MBFloat w)  { return vec4p(x,y,z,w); }
    public MBVec4 vec4(Vector4 v)                                   { return vec4p(v.x,v.y,v.z,v.w); }
    
    private MBVec3 vec3p(Object... params) {
        return putVariable(new MBVec3V(),funcBuild("vec3",params));
    }
    public MBVec3 vec3(MBVec2 u, float z)               { return vec3p(u,z); }
    public MBVec3 vec3(float x, float y, float z)       { return vec3p(x,y,z); }
    public MBVec3 vec3(MBVec2 u, MBFloat z)             { return vec3p(u,z); }
    public MBVec3 vec3(MBFloat x, MBFloat y, MBFloat z) { return vec3p(x,y,z); }
    public MBVec3 vec3(Vector3 v)                                   { return vec3p(v.x,v.y,v.z); }
    
    private MBVec2 vec2p(Object... params) {
        return putVariable(new MBVec2V(),funcBuild("vec2",params));
    }
    public MBVec2 vec2(float x, float y)        { return vec2p(x,y); }
    public MBVec2 vec2(MBFloat x, MBFloat y)    { return vec2p(x,y); }
    public MBVec2 vec2(Vector2 v)                                   { return vec2p(v.x,v.y); }
    
    
    public MBVec4 xyzw(MBVec4 v) {
        return putVariable(new MBVec4V(),v+".xyzw");
    }
    
    private MBVec3 xyzp(Object v) {
        return putVariable(new MBVec3V(),v+".xyz");
    }
    public MBVec3 xyz(MBVec4 v) { return xyzp(v); }
    public MBVec3 xyz(MBVec3 v) { return xyzp(v); }
    
    private MBVec2 xyp(Object v) {
        return putVariable(new MBVec2V(),v+".xy");
    }
    public MBVec2 xy(MBVec4 v) { return xyp(v); }
    public MBVec2 xy(MBVec3 v) { return xyp(v); }
    public MBVec2 xy(MBVec2 v) { return xyp(v); }
    
    private MBFloat xp(Object v) {
        return putVariable(new MBFloatV(),v+".x");
    }
    public MBFloat x(MBVec4 v) { return xp(v); }
    public MBFloat x(MBVec3 v) { return xp(v); }
    public MBFloat x(MBVec2 v) { return xp(v); }
    
    private MBFloat yp(Object v) {
        return putVariable(new MBFloatV(),v+".y");
    }
    public MBFloat y(MBVec4 v) { return yp(v); }
    public MBFloat y(MBVec3 v) { return yp(v); }
    public MBFloat y(MBVec2 v) { return yp(v); }
    
    private MBFloat zp(Object v) {
        return putVariable(new MBFloatV(),v+".z");
    }
    public MBFloat z(MBVec4 v) { return zp(v); }
    public MBFloat z(MBVec3 v) { return zp(v); }
    
    private MBFloat wp(Object v) {
        return putVariable(new MBFloatV(),v+".w");
    }
    public MBFloat w(MBVec4 v) { return wp(v); }

    
    
    
    public <T extends MBData> void set(T u, T v) {
        putString(u+" = "+v+";\n");
    }
    
    public <T extends MBData> MBBool and(MBBool u, MBBool v) {
        return (MBBool) putVariable(new MBBoolV(),u+"&&"+v);
    }
    
    public <T extends MBData> MBBool or(MBBool u, MBBool v) {
        return (MBBool) putVariable(new MBBoolV(),u+"||"+v);
    }
    
    public <T extends MBData> MBBool equals(T u, T v) {
        return (MBBool) putVariable(new MBBoolV(),u+"=="+v);
    }
    public <T extends MBData> MBBool less(T u, T v) {
        return (MBBool) putVariable(new MBBoolV(),u+"<"+v);
    }
    public <T extends MBData> MBBool greater(T u, T v) {
        return (MBBool) putVariable(new MBBoolV(),u+">"+v);
    }
    public <T extends MBData> MBBool leq(T u, T v) {
        return (MBBool) putVariable(new MBBoolV(),u+"<="+v);
    }
    public <T extends MBData> MBBool geq(T u, T v) {
        return (MBBool) putVariable(new MBBoolV(),u+">="+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T add(T u, T v) {
        return (T) putVariable(getVar(u),u+"+"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T sub(T u, T v) {
        return (T) putVariable(getVar(u),u+"-"+v);
    }
    
    public MBFloatV sub(float u, MBFloat v) {
        return putVariable(new MBFloatV(),u+"-"+v);
    }
    
    public MBFloatV sub(MBFloat u, float v) {
        return putVariable(new MBFloatV(),u+"-"+v);
    }
    
    public MBFloatV sub(MBFloat u, MBFloat v) {
        return putVariable(new MBFloatV(),u+"-"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mul(T u, T v) {
        return (T) putVariable(getVar(u),u+"*"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mul(T u, float f) {
        return (T) putVariable(getVar(u),u+"*"+f);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mul(float f, T u) {
        return (T) putVariable(getVar(u),f+"*"+u);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mul(T u, MBFloat f) {
        return (T) putVariable(getVar(u),u+"*"+f);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mul(MBFloat f, T u) {
        return (T) putVariable(getVar(u),f+"*"+u);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T div(T u, T v) {
        return (T) putVariable(getVar(u),u+"/"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T pow(T u, T v) {
        return (T) putVariable(getVar(u),funcBuild("pow",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T pow(T u, float y) {
        return (T) putVariable(getVar(u),funcBuild("pow",u,y));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T sqrt(T u) {
        return (T) putVariable(getVar(u),funcBuild("sqrt",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T round(T u) {
        return (T) putVariable(getVar(u),funcBuild("round",u));
    }
    
    public <T extends MBData> MBFloat length(T u) {
        return putVariable(new MBFloatV(),funcBuild("length",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T abs(T u) {
        return (T) putVariable(getVar(u),funcBuild("abs",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mix(T u, T v, T w) {
        return (T) putVariable(getVar(u),funcBuild("mix",u,v,w));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mix(T u, T v, MBFloat w) {
        return (T) putVariable(getVar(u),funcBuild("mix",u,v,w));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T mix(T u, T v, float w) {
        return (T) putVariable(getVar(u),funcBuild("mix",u,v,w));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T min(T u, T v) {
        return (T) putVariable(getVar(u),funcBuild("min",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T max(T u, T v) {
        return (T) putVariable(getVar(u),funcBuild("max",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T clamp(T u, T min, T max) {
        return (T) putVariable(getVar(u),funcBuild("clamp",u,min,max));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T clamp(T u, MBFloat min, MBFloat max) {
        return (T) putVariable(getVar(u),funcBuild("clamp",u,min,max));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T clamp(T u, float min, float max) {
        return (T) putVariable(getVar(u),funcBuild("clamp",u,min,max));
    }
    
    
    public MBBoolU  mbBoolu(String id, boolean b)  { return putUniform(new MBBoolU(),id, b); }
    public MBIntU   mbIntu(String id, int i)       { return putUniform(new MBIntU(),id,  i); }
    public MBFloatU mbFloatu(String id, float f)   { return putUniform(new MBFloatU(),id,f); }
    
    public MBVec4U vec4u(String id, Vector4 v) { return putUniform(new MBVec4U(),id,v.get()); }
    public MBVec4U vec4u(String id, float x, float y, float z, float w) { return putUniform(new MBVec4U(),id,new Vector4(x,y,z,w)); }
    public MBVec3U vec3u(String id, Vector3 v) { return putUniform(new MBVec3U(),id,v.get()); }
    public MBVec3U vec3u(String id, float x, float y, float z) { return putUniform(new MBVec3U(),id,new Vector3(x,y,z)); }
    public MBVec2U vec2u(String id, Vector2 v) { return putUniform(new MBVec2U(),id,v.get()); }
    public MBVec2U vec2u(String id, float x, float y) { return putUniform(new MBVec2U(),id,new Vector2(x,y)); }
    public MBMat4U mat4u(String id, Matrix4 m) { return putUniform(new MBMat4U(),id,m.get()); }
    public MBMat3U mat4u(String id, Matrix3 m) { return putUniform(new MBMat3U(),id,m.get()); }
    public MBMat2U mat4u(String id, Matrix2 m) { return putUniform(new MBMat2U(),id,m.get()); }
    public MBTexU texture(String id, Texture t) { return putUniform(new MBTexU(),id,new TextureUniform(t,textureCount++)); }
    
    private static String funcBuild(String name, Object... params) {
        String build = name + "(";
        for (int i=0; i<params.length; i++) {
            build += params[i];
            if (i!=params.length-1) {
                build += ",";
            }
        }
        build += ")";
        return build;
    } 
    
    
    
    
    private static String getType(MBObject v) {
        if (v instanceof MBTexU)    return "sampler2D";
        if (v instanceof MBBool)    return "bool";
        if (v instanceof MBInt)     return "int";
        if (v instanceof MBFloat)   return "float";
        if (v instanceof MBVec4)    return "vec4";
        if (v instanceof MBVec3)    return "vec3";
        if (v instanceof MBVec2)    return "vec2";
        if (v instanceof MBMat4)    return "mat4";
        if (v instanceof MBMat3)    return "mat3";
        if (v instanceof MBMat2)    return "mat2";
        return "???";
    }
    
    private static MBVar getVar(MBData v) {
        if (v instanceof MBBool)    return new MBBoolV();
        if (v instanceof MBInt)     return new MBIntV();
        if (v instanceof MBFloat)   return new MBFloatV();
        if (v instanceof MBVec4)    return new MBVec4V();
        if (v instanceof MBVec3)    return new MBVec3V();
        if (v instanceof MBVec2)    return new MBVec2V();
        if (v instanceof MBMat4)    return new MBMat4V();
        if (v instanceof MBMat3)    return new MBMat3V();
        if (v instanceof MBMat2)    return new MBMat2V();
        return null;
    }
    
    private static MBUniform getUniform(MBUniform v) {
        if (v instanceof MBTexU)     return new MBTexU();
        if (v instanceof MBBoolU)    return new MBBoolU();
        if (v instanceof MBIntU)     return new MBIntU();
        if (v instanceof MBFloatU)   return new MBFloatU();
        if (v instanceof MBVec4U)    return new MBVec4U();
        if (v instanceof MBVec3U)    return new MBVec3U();
        if (v instanceof MBVec2U)    return new MBVec2U();
        if (v instanceof MBMat4U)    return new MBMat4U();
        if (v instanceof MBMat3U)    return new MBMat3U();
        if (v instanceof MBMat2U)    return new MBMat2U();
        return null;
    }
    
    
    
    
    
    private interface MBData {
        String getName();
    }
    
    private abstract static class MBObject {
        GeneratedMaterialBuilder mb;
        String name;
        String type;
        @Override
        public String toString() {
            return name;
        }
    }
    
    public static interface MBBool  extends MBData {
        
    }
    public static interface MBInt   extends MBData {
        
    }
    public static interface MBFloat extends MBData {
        
    }
    public static interface MBVec4  extends MBData {
        MBVec4 mult(float f);
        MBVec4 mult(MBFloat f);
        MBVec4 mult(MBVec4 v);
        MBVec4 xyzw();
        MBVec3 xyz();
        MBVec2 xy();
        MBFloat x();
        MBFloat y();
        MBFloat z();
        MBFloat w();
    }
    public static interface MBVec3  extends MBData {
        MBVec3 mult(float f);
        MBVec3 mult(MBFloat f);
        MBVec3 mult(MBVec3 v);
        MBVec3 xyz();
        MBVec2 xy();
        MBFloat x();
        MBFloat y();
        MBFloat z();
    }
    public static interface MBVec2  extends MBData {
        
    }
    public static interface MBMat4  extends MBData {
        
    }
    public static interface MBMat3  extends MBData {
        
    }
    public static interface MBMat2  extends MBData {
        
    }
    
    abstract static class MBUniform extends MBObject {
        Object data;
        String id; //ID to request uniforms
        @Override
        public int hashCode() {
            return data.hashCode() + id.hashCode();
        }
        MBUniform copy() {
            MBUniform uniform = getUniform(this);
            uniform.mb = mb;
            uniform.name = name;
            uniform.type = type;
            uniform.data = this.data;
            uniform.id = this.id;
            return uniform;
        }
    }
    
    public static class MBTexU extends MBUniform implements MBData {
        public Texture get() { return (Texture) data; }
        public void set(Texture t)  { data = t; }
        public String getName() { return name; }
    }
    public static class MBBoolU extends MBUniform implements MBBool { 
        public boolean get() { return (boolean) data; }
        public void set(boolean b)  { data = b; }
        public String getName() { return name; }
    }
    public static class MBIntU   extends MBUniform implements MBInt { 
        public int get() { return (int) data; }
        public void set(int i)      { data = i; }
        public String getName() { return name; }
    }
    public static class MBFloatU extends MBUniform implements MBFloat { 
        public float get() { return (float) data; }
        public void set(float f)    { data = f; }
        public String getName() { return name; }
    }
    public static class MBVec4U  extends MBUniform implements MBVec4 { 
        public Vector4 get() { return (Vector4) data; }
        public void set(Vector4 v)  { data = v.get(); }
        public void set(float x, float y, float z, float w)  { data = new Vector4(x,y,z,w); }
        public String getName() { return name; }
        
        //Override
        public MBVec4 mult(float f) { return mb.mul(this,f); }
        public MBVec4 mult(MBFloat f) { return mb.mul(this,f); }
        public MBVec4 mult(MBVec4 v) { return mb.mul(this,v); }
        public MBVec4 xyzw() { return mb.xyzw(this); }
        public MBVec3 xyz() { return mb.xyz(this); }
        public MBVec2 xy() { return mb.xy(this); }
        public MBFloat x() { return mb.x(this); }
        public MBFloat y() { return mb.y(this); }
        public MBFloat z() { return mb.z(this); }
        public MBFloat w() { return mb.w(this); }
    }
    public static class MBVec3U  extends MBUniform implements MBVec3 { 
        public Vector3 get() { return (Vector3) data; }
        public void set(Vector3 v)  { data = v.get(); }
        public void set(float x, float y, float z)  { data = new Vector3(x,y,z); }
        public String getName() { return name; }
        
        //Override
        public MBVec3 mult(float f) { return mb.mul(this,f); }
        public MBVec3 mult(MBFloat f) { return mb.mul(this,f); }
        public MBVec3 mult(MBVec3 v) { return mb.mul(this,v); }
        public MBVec3 xyz() { return mb.xyz(this); }
        public MBVec2 xy() { return mb.xy(this); }
        public MBFloat x() { return mb.x(this); }
        public MBFloat y() { return mb.y(this); }
        public MBFloat z() { return mb.z(this); }
    }
    public static class MBVec2U  extends MBUniform implements MBVec2 { 
        public Vector2 get() { return (Vector2) data; }
        public void set(Vector2 v)  { data = v.get(); }
        public void set(float x, float y)  { data = new Vector2(x,y); }
        public String getName() { return name; }
        
        //Override
        public MBVec2 mult(float f) { return mb.mul(this,f); }
        public MBVec2 mult(MBFloat f) { return mb.mul(this,f); }
        public MBVec2 mult(MBVec2 v) { return mb.mul(this,v); }
        public MBVec2 xy() { return mb.xy(this); }
        public MBFloat x() { return mb.x(this); }
        public MBFloat y() { return mb.y(this); }
    }
    public static class MBMat4U  extends MBUniform implements MBMat4 { 
        public Matrix4 get() { return (Matrix4) data; }
        public void set(Matrix4 m)  { data = m.get(); }
        public String getName() { return name; }
    }
    public static class MBMat3U  extends MBUniform implements MBMat3 { 
        public Matrix3 get() { return (Matrix3) data; }
        public void set(Matrix3 m)  { data = m.get(); }
        public String getName() { return name; }
    }
    public static class MBMat2U  extends MBUniform implements MBMat2 { 
        public Matrix2 get() { return (Matrix2) data; }
        public void set(Matrix2 m)  { data = m.get(); }
        public String getName() { return name; }
    }
    
    private abstract static class MBVar extends MBObject {
        String value;
    }
    
    public static class MBBoolV extends MBVar implements MBBool { 
        public String getName() { return name; }
    }
    public static class MBIntV   extends MBVar implements MBInt { 
        public String getName() { return name; }
    }
    public static class MBFloatV extends MBVar implements MBFloat { 
        public String getName() { return name; }
    }
    public static class MBVec4V  extends MBVar implements MBVec4 { 
        public String getName() { return name; }
        
        //Override
        public MBVec4 mult(float f) { return mb.mul(this,f); }
        public MBVec4 mult(MBFloat f) { return mb.mul(this,f); }
        public MBVec4 mult(MBVec4 v) { return mb.mul(this,v); }
        public MBVec4 xyzw() { return mb.xyzw(this); }
        public MBVec3 xyz() { return mb.xyz(this); }
        public MBVec2 xy() { return mb.xy(this); }
        public MBFloat x() { return mb.x(this); }
        public MBFloat y() { return mb.y(this); }
        public MBFloat z() { return mb.z(this); }
        public MBFloat w() { return mb.w(this); }
    }
    public static class MBVec3V  extends MBVar implements MBVec3 { 
        public String getName() { return name; }
        
        //Override
        public MBVec3 mult(float f) { return mb.mul(this,f); }
        public MBVec3 mult(MBFloat f) { return mb.mul(this,f); }
        public MBVec3 mult(MBVec3 v) { return mb.mul(this,v); }
        public MBVec3 xyz() { return mb.xyz(this); }
        public MBVec2 xy() { return mb.xy(this); }
        public MBFloat x() { return mb.x(this); }
        public MBFloat y() { return mb.y(this); }
        public MBFloat z() { return mb.z(this); }
    }
    public static class MBVec2V  extends MBVar implements MBVec2 { 
        public String getName() { return name; }
        
        //Override
        public MBVec2 mult(float f) { return mb.mul(this,f); }
        public MBVec2 mult(MBFloat f) { return mb.mul(this,f); }
        public MBVec2 mult(MBVec2 v) { return mb.mul(this,v); }
        public MBVec2 xy() { return mb.xy(this); }
        public MBFloat x() { return mb.x(this); }
        public MBFloat y() { return mb.y(this); }
    }
    public static class MBMat4V  extends MBVar implements MBMat4 { 
        public String getName() { return name; }
    }
    public static class MBMat3V  extends MBVar implements MBMat3 { 
        public String getName() { return name; }
    }
    public static class MBMat2V  extends MBVar implements MBMat2 { 
        public String getName() { return name; }
    }
    
    
    
    
    
    
    
}
