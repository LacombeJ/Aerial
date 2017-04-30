package jonl.ge;

import java.util.ArrayList;

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
public class MaterialBuilder {
    
    public MBVec3   diffuse     = null;
    public MBVec3   specular    = null;
    public MBVec3   normal      = null;
    public MBTexU   height      = null;
    public MBFloat  roughness   = null;
    public MBFloat  fresnel     = null;
    
    /** List of Uniforms */
    final ArrayList<MBUniform> mbUniformList = new ArrayList<>();
    private int textureCount;
    
    /** List of Statements to be executed in order */
    private final ArrayList<String> mbStatementList = new ArrayList<>();
    private int variableCount = 0;
    
    public MaterialBuilder() {
        
    }
    
    public Material build() {
        Material mat = new Material(
                diffuse,specular,normal,height,
                roughness,fresnel,
                mbStatementList,mbUniformList
        );
        return mat;
    }
    
    private String genName() {
        String name = "mb_"+variableCount;
        variableCount++;
        return name;
    }
    
    /** Sets this variable to the given value */
    private <T extends MBVar> T putVariable(T v, String value) {
        v.mb = this;
        v.type = getType(v);
        v.name = genName();
        v.value = value;
        String declaration = v.type+" "+v.name+" = "+v.value+";\n";
        mbStatementList.add(declaration);
        return v;
    }
    
    private <T extends MBUniform> T putUniform(T v, String id, Object data) {
        v.mb = this;
        v.type = getType(v);
        v.name = genName();
        v.id = id;
        v.data = data;
        mbUniformList.add(v);
        return v;
    }
    
    
    
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

    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T add(T u, T v) {
        return (T) putVariable(getVar(u),u+"+"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends MBData> T sub(T u, T v) {
        return (T) putVariable(getVar(u),u+"-"+v);
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
    public MBTexU texture(String id, Texture t) {
        MBTexU u = new MBTexU();
        u.textureID = textureCount++;
        return putUniform(u,id,t);
    }
    
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
    /*
    private static String vec4s(Vector4 v) {
        return funcBuild("vec4",v.x,v.y,v.z,v.w);
    }
    
    private static String vec3s(Vector3 v) {
        return funcBuild("vec3",v.x,v.y,v.z);
    }
    
    private static String vec2s(Vector2 v) {
        return funcBuild("vec2",v.x,v.y);
    }
    
    private static String mat4s(Matrix4 m) {
        return funcBuild("mat4",vec4s(m.getCol(0)),vec4s(m.getCol(1)),vec4s(m.getCol(2)),vec4s(m.getCol(3)));
    }
    
    private static String mat3s(Matrix3 m) {
        return funcBuild("mat3",vec3s(m.getCol(0)),vec3s(m.getCol(1)),vec3s(m.getCol(2)));
    }
    
    private static String mat2s(Matrix2 m) {
        return funcBuild("mat2",vec2s(m.getCol(0)),vec2s(m.getCol(1)));
    }
    */
    
    
    private static String getType(MBObject v) {
        if (v instanceof MBTexU)    return "sampler2D";
        if (v instanceof MBBool)    return "int";
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
        MaterialBuilder mb;
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
        int textureID = -1;
        @Override
        public int hashCode() {
            return data.hashCode() + id.hashCode() + textureID;
        }
        MBUniform copy() {
            MBUniform uniform = getUniform(this);
            uniform.mb = mb;
            uniform.name = name;
            uniform.type = type;
            uniform.data = this.data;
            uniform.id = this.id;
            uniform.textureID = this.textureID;
            return uniform;
        }
    }
    
    public static class MBTexU extends MBUniform {
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
    }
    public static class MBVec2U  extends MBUniform implements MBVec2 { 
        public Vector2 get() { return (Vector2) data; }
        public void set(Vector2 v)  { data = v.get(); }
        public void set(float x, float y)  { data = new Vector2(x,y); }
        public String getName() { return name; }
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
    }
    public static class MBVec2V  extends MBVar implements MBVec2 { 
        public String getName() { return name; }
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
