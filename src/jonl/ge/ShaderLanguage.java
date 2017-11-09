package jonl.ge;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.jutils.func.Tuple2;
import jonl.vmath.Matrix2;
import jonl.vmath.Matrix3;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

//TODO calculate values before rendering for peformance?
/**
 * Shader Material Builder
 * @author Jonathan Lacosle
 *
 */
public class ShaderLanguage {
    
    /** List of attributes as strings */
    private final ArrayList<String> slAttributeList = new ArrayList<>();
    
    /** List of hard-coded uniforms */
    private final ArrayList<String> slHardUniformList = new ArrayList<>();
    
    /** List of Uniforms */
    private final ArrayList<SLUniform> slUniformList = new ArrayList<>();
    private int textureCount;
    
    /** List of Statements to be executed in order */
    private final ArrayList<String> slStatementList = new ArrayList<>();
    private int variableCount = 0;
    
    private String version = null;
    
    private final ArrayList<String> slFunctionList = new ArrayList<>();
    private ArrayList<String> slFunctionBuilder = new ArrayList<>();
    private boolean inFunction = false;
    private int functionCount = 0;
    
    
    
    //To id unique generated materials we use a static unique_gm for every GSLuilder
    //with another int everytime the GSL is changed and return _gm_[unique_gm_id]_[unique_gm_changed]
    //This is so that only one shader is generated for every unique GM
    int unique_gm_id = 0;
    int unique_gm_changed = 0;
    static int unique_gm_count = 0;
    
    public ShaderLanguage() {
        unique_gm_id = unique_gm_count++;
    }
    
    
    
    @SuppressWarnings("unchecked")
    protected <T extends SLData> T copyData(T md) {
        if (md==null) {
            return null;
        }
        if (md instanceof SLUniform) {
            SLUniform slu = (SLUniform) md;
            return (T) slu.copy();
        }
        return md;
    }
    
    protected ArrayList<SLUniform> getUniformList() {
        return slUniformList;
    }
    
    protected ArrayList<String> getStatementList() {
        return slStatementList;
    }
    
    String getUniformString(ArrayList<SLUniform> uniforms) {
        StringBuilder sb = new StringBuilder();
        for (SLUniform u : uniforms) {
            sb.append("uniform "+u.type+" "+u.name+";\n");
        }
        return sb.toString();
    }
    
    String getStatementString(ArrayList<String> statements) {
        StringBuilder sb = new StringBuilder();
        for (String s : statements) {
            sb.append(s);
        }
        return sb.toString();
    }
    
    void addUniformListAndMap(ArrayList<SLUniform> list, HashMap<String,SLUniform> map) {
        for (SLUniform slu : slUniformList) {
            SLUniform uniform = slu.copy();
            list.add(uniform);
            map.put(uniform.id,uniform);
        }
    }
    
    public String shader() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(version);
        
        for (String a : slAttributeList) {
            sb.append(a);
        }
        
        for (String u : slHardUniformList) {
            sb.append(u);
        }
        
        for (SLUniform u : slUniformList) {
            sb.append("uniform "+u.type+" "+u.name+";\n");
        }
        
        for (String s : slFunctionList) {
            sb.append(s);
        }
        
        sb.append("void main() {\n");
        for (String s : slStatementList) {
            sb.append(s);
        }
        sb.append("}\n");
        
        return sb.toString();
    }
    
    
    /**
     * @return a unique name for a generated variable
     */
    private String genName() {
        String name = "_gmv_"+variableCount;
        variableCount++;
        return name;
    }
    
    private String genFuncName() {
        String name = "_gmfunc_"+functionCount;
        functionCount++;
        return name;
    }
    
    // -----------------------------------------------------------------------------
    // SHADER MODIFICATION
    
    /** Sets this variable to the given value */
    private <T extends SLVar> T putVariable(T v, String value) {
        unique_gm_changed++;
        v.sl = this;
        v.type = getType(v);
        v.name = genName();
        v.value = value;
        String declaration = v.type+" "+v.name+" = "+v.value+";\n";
        if (inFunction) {
            slFunctionBuilder.add(declaration);
        } else {
            slStatementList.add(declaration);
        }
        return v;
    }
    
    private void putAttribute(String attribute) {
        unique_gm_changed++;
        slAttributeList.add(attribute+";\n");
    }
    
    private void putHardUniform(String uniform) {
        unique_gm_changed++;
        slHardUniformList.add(uniform+";\n");
    }
    
    private <T extends SLUniform> T putUniform(T v, String id, Object data) {
        unique_gm_changed++;
        v.sl = this;
        v.type = getType(v);
        v.name = genName();
        v.id = id;
        v.data = data;
        slUniformList.add(v);
        return v;
    }
    
    private <T extends SLData> SLFunc<T> putFunc(String body, SLFunc<T> f, Class<T> returnClass, Tuple2<String,String>[] arglist) {
        unique_gm_changed++;
        f.sl = this;
        f.name = genFuncName();
        f.ref = getData(returnClass);
        
        String[] argTypes = new String[0];
        String[] argNames = new String[0];
        Tuple2.rip(arglist,argTypes,argNames);
        
        Object[] args = new Object[argTypes.length];
        for (int i=0; i<argTypes.length; i++) {
            args[i] = argTypes[i] + ' ' + argNames[i];
        }
        String funcPart = funcBuild(f.name, args);
        String declaration = getType(getVar(f.ref)) + " " + funcPart;
        
        slFunctionList.add(declaration + "{\n");
        slFunctionList.add(body);
        slFunctionList.add("}\n");
        
        return f;
    }
    
    private <T extends SLData> SLFunc<T> putBegin(SLFunc<T> f, Class<T> returnClass, String... argTypes) {
        unique_gm_changed++;
        f.sl = this;
        f.name = genFuncName();
        f.ref = getData(returnClass);
        
        Object[] args = new Object[argTypes.length];
        for (int i=0; i<argTypes.length; i++) {
            args[i] = argTypes[i] + ' ' + arg(i);
        }
        String funcPart = funcBuild(f.name, args);
        String declaration = getType(getVar(f.ref)) + " " + funcPart;
        
        slFunctionBuilder.add(declaration + "{\n");
        
        inFunction = true;
        
        return f;
    }
    
    private void putEnd() {
        unique_gm_changed++;
        slFunctionBuilder.add("\n}\n");
        
        inFunction = false;
        
        for (String s : slFunctionBuilder) {
            slFunctionList.add(s);
        }
        
        slFunctionBuilder = new ArrayList<>();
        
    }
    
    private void put(String string) {
        unique_gm_changed++;
        if (inFunction) {
            slFunctionBuilder.add(string);
        } else {
            slStatementList.add(string);
        }
    }
    
    // -----------------------------------------------------------------------------
    
    public void version(String v) { version = "#version "+v+"\n"; }
    
    public void layoutIn(int index, String a) { putAttribute(String.format("layout (location = %d) in %s",index,a)); }
    public void layoutOut(int index, String a) { putAttribute(String.format("layout (location = %d) out %s",index,a)); }
    
    public void attributeIn(String a) { putAttribute(String.format("in %s",a)); }
    public void attributeOut(String a) { putAttribute(String.format("out %s",a)); }
    
    public void uniform(String u) { putHardUniform(String.format("uniform %s",u)); }
    
    /** Adds string to body */
    public void putString(String s)     { put(s); }
    
    /** Add string, semicolon, and new line to body */
    public void putStatement(String s)  { putString(s+";\n"); }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> SLFunc<T> slFunc(String body, Class<T> returnClass, Tuple2<String,String>... args) {
        return putFunc(body, new SLFunc<T>(), returnClass, args);
    }
    
    public <T extends SLData> SLFunc<T> slBegin(Class<T> returnClass, String... argTypes) {
        return putBegin(new SLFunc<T>(), returnClass, argTypes);
    }
    
    public String arg(int i) {
        return "_arg"+i;
    }
    
    private void returnStatement(Object o) {
        putStatement("return "+o);
    }
    
    public void slEnd() {
        putEnd();
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T call(SLFunc<T> f, Object... objects) {
        return (T) putVariable(getVar(f.ref),funcBuild(f.name,objects));
        
    }
    
    private void putIf(SLBool b)        { putString("if ("+b.getName()+") {\n"); }
    private void putElseIf(SLBool b)    { putString("} else if ("+b.getName()+") {\n"); }
    private void putElse()              { putString("} else {\n"); }
    private void putEndIf()             { putString("}\n"); }
    
    public void slIf(SLBool bool)       { putIf(bool); }
    public void slElseIf(SLBool bool)   { putElseIf(bool); }
    public void slElse()                { putElse(); }
    public void slEndIf()               { putEndIf(); }
    
    public SLBool   slBool(String var)  { return putVariable(new SLBoolV(),     var); }
    public SLInt    slInt(String var)   { return putVariable(new SLIntV(),      var); }
    public SLFloat  slFloat(String var) { return putVariable(new SLFloatV(),    var); }
    public SLVec4   vec4(String var)    { return putVariable(new SLVec4V(),     var); }
    public SLVec3   vec3(String var)    { return putVariable(new SLVec3V(),     var); }
    public SLVec2   vec2(String var)    { return putVariable(new SLVec2V(),     var); }
    
    public SLBool   slBool  (boolean b) { return putVariable(new SLBoolV(),  b+""); }
    public SLInt    slInt   (int i)     { return putVariable(new SLIntV(),   i+""); }
    public SLFloat  slFloat (float f)   { return putVariable(new SLFloatV(), f+""); }
    
    public SLVec4 sample(SLTexU u) {
        return putVariable(new SLVec4V(),"texture2D("+u.name+",fTexCoord)");
    }
    public SLVec4 sample(SLTexU u, String texCoord) {
        return putVariable(new SLVec4V(),"texture2D("+u.name+","+texCoord+")");
    }
    public SLVec4 sample(SLTexU u, SLVec2 texCoord) {
        return putVariable(new SLVec4V(),"texture2D("+u.name+","+texCoord+")");
    }
    
    private SLVec4 vec4p(Object... params) {
        return putVariable(new SLVec4V(),funcBuild("vec4",params));
    }
    public SLVec4 vec4(SLVec4 u)                                    { return vec4p(u); }
    public SLVec4 vec4(SLVec3 u, float w)                           { return vec4p(u,w); }
    public SLVec4 vec4(SLVec2 u, float z, float w)                  { return vec4p(u,z,w); }
    public SLVec4 vec4(SLVec3 u, SLFloat w)                         { return vec4p(u,w); }
    public SLVec4 vec4(SLVec2 u, SLFloat z, SLFloat w)              { return vec4p(u,z,w); }
    public SLVec4 vec4(Vector4 v)                                   { return vec4p(v.x,v.y,v.z,v.w); }
    public SLVec4 vec4(float v)                                     { return vec4p(v,v,v,v); }
    public SLVec4 vec4(SLFloat v)                                   { return vec4p(v,v,v,v); }
    
    public SLVec4 vec4(float x, float y, float z, float w)          { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, float y, float z, SLFloat w)        { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, float y, SLFloat z, float w)        { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, float y, SLFloat z, SLFloat w)      { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, SLFloat y, float z, float w)        { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, SLFloat y, float z, SLFloat w)      { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, SLFloat y, SLFloat z, float w)      { return vec4p(x,y,z,w); }
    public SLVec4 vec4(float x, SLFloat y, SLFloat z, SLFloat w)    { return vec4p(x,y,z,w); }
    
    public SLVec4 vec4(SLFloat x, float y, float z, float w)        { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, float y, float z, SLFloat w)      { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, float y, SLFloat z, float w)      { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, float y, SLFloat z, SLFloat w)    { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, SLFloat y, float z, float w)      { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, SLFloat y, float z, SLFloat w)    { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, SLFloat y, SLFloat z, float w)    { return vec4p(x,y,z,w); }
    public SLVec4 vec4(SLFloat x, SLFloat y, SLFloat z, SLFloat w)  { return vec4p(x,y,z,w); }

    
    
    private SLVec3 vec3p(Object... params) {
        return putVariable(new SLVec3V(),funcBuild("vec3",params));
    }
    public SLVec3 vec3(SLVec3 u)                                    { return vec3p(u); }
    public SLVec3 vec3(SLVec2 u, float z)                           { return vec3p(u,z); }
    public SLVec3 vec3(float x, float y, float z)                   { return vec3p(x,y,z); }
    public SLVec3 vec3(SLVec2 u, SLFloat z)                         { return vec3p(u,z); }
    public SLVec3 vec3(SLFloat x, SLFloat y, SLFloat z)             { return vec3p(x,y,z); }
    public SLVec3 vec3(Vector3 v)                                   { return vec3p(v.x,v.y,v.z); }
    public SLVec3 vec3(float v)                                     { return vec3p(v,v,v); }
    public SLVec3 vec3(SLFloat v)                                   { return vec3p(v,v,v); }
    
    private SLVec2 vec2p(Object... params) {
        return putVariable(new SLVec2V(),funcBuild("vec2",params));
    }
    public SLVec2 vec2(SLVec2 u)                { return vec2p(u); }
    public SLVec2 vec2(float x, float y)        { return vec2p(x,y); }
    public SLVec2 vec2(SLFloat x, SLFloat y)    { return vec2p(x,y); }
    public SLVec2 vec2(Vector2 v)               { return vec2p(v.x,v.y); }
    public SLVec2 vec2(float v)                 { return vec2p(v,v); }
    public SLVec2 vec2(SLFloat v)               { return vec2p(v,v); }
    
    
    public SLVec4 xyzw(SLVec4 v) {
        return putVariable(new SLVec4V(),v+".xyzw");
    }
    
    private SLVec3 xyzp(Object v) {
        return putVariable(new SLVec3V(),v+".xyz");
    }
    public SLVec3 xyz(SLVec4 v) { return xyzp(v); }
    public SLVec3 xyz(SLVec3 v) { return xyzp(v); }
    
    private SLVec2 xyp(Object v) {
        return putVariable(new SLVec2V(),v+".xy");
    }
    public SLVec2 xy(SLVec4 v) { return xyp(v); }
    public SLVec2 xy(SLVec3 v) { return xyp(v); }
    public SLVec2 xy(SLVec2 v) { return xyp(v); }
    
    private SLFloat xp(Object v) {
        return putVariable(new SLFloatV(),v+".x");
    }
    public SLFloat x(SLVec4 v) { return xp(v); }
    public SLFloat x(SLVec3 v) { return xp(v); }
    public SLFloat x(SLVec2 v) { return xp(v); }
    
    private SLFloat yp(Object v) {
        return putVariable(new SLFloatV(),v+".y");
    }
    public SLFloat y(SLVec4 v) { return yp(v); }
    public SLFloat y(SLVec3 v) { return yp(v); }
    public SLFloat y(SLVec2 v) { return yp(v); }
    
    private SLFloat zp(Object v) {
        return putVariable(new SLFloatV(),v+".z");
    }
    public SLFloat z(SLVec4 v) { return zp(v); }
    public SLFloat z(SLVec3 v) { return zp(v); }
    
    private SLFloat wp(Object v) {
        return putVariable(new SLFloatV(),v+".w");
    }
    public SLFloat w(SLVec4 v) { return wp(v); }

    
    public void discard() {
        putString("discard;\n");
    }
    
    
    
    
    public <T extends SLData> void set(T u, T v) {
        putString(u+" = "+v+";\n");
    }
    
    public <T extends SLData> SLBool not(SLBool u) {
        return (SLBool) putVariable(new SLBoolV(),"!"+u);
    }
    
    public <T extends SLData> SLBool and(SLBool u, SLBool v) {
        return (SLBool) putVariable(new SLBoolV(),u+"&&"+v);
    }
    
    public <T extends SLData> SLBool or(SLBool u, SLBool v) {
        return (SLBool) putVariable(new SLBoolV(),u+"||"+v);
    }
    
    public <T extends SLData> SLBool xor(SLBool u, SLBool v) {
        return (SLBool) putVariable(new SLBoolV(),u+"^^"+v);
    }
    
    public <T extends SLData> SLBool equals(T u, T v) {
        return (SLBool) putVariable(new SLBoolV(),u+"=="+v);
    }
    public <T extends SLData> SLBool neq(T u, T v) {
        return (SLBool) putVariable(new SLBoolV(),u+"!="+v);
    }
    public <T extends SLData> SLBool less(T u, T v) {
        return (SLBool) putVariable(new SLBoolV(),u+"<"+v);
    }
    public <T extends SLData> SLBool greater(T u, T v) {
        return (SLBool) putVariable(new SLBoolV(),u+">"+v);
    }
    public <T extends SLData> SLBool greater(T u, float v) {
        return (SLBool) putVariable(new SLBoolV(),u+">"+v);
    }
    public <T extends SLData> SLBool leq(T u, T v) {
        return (SLBool) putVariable(new SLBoolV(),u+"<="+v);
    }
    public <T extends SLData> SLBool geq(T u, T v) {
        return (SLBool) putVariable(new SLBoolV(),u+">="+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T add(T u, T v) {
        return (T) putVariable(getVar(u),u+"+"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T add(float u, SLFloat v) {
        return (T) putVariable(new SLFloatV(),u+"+"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T add(SLFloat u, float v) {
        return (T) putVariable(getVar(u),u+"+"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T sub(T u, T v) {
        return (T) putVariable(getVar(u),u+"-"+v);
    }
    
    public SLFloatV sub(float u, SLFloat v) {
        return putVariable(new SLFloatV(),u+"-"+v);
    }
    
    public SLFloatV sub(SLFloat u, float v) {
        return putVariable(new SLFloatV(),u+"-"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mul(T u, T v) {
        return (T) putVariable(getVar(u),u+"*"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mul(T u, float f) {
        return (T) putVariable(getVar(u),u+"*"+f);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mul(float f, T u) {
        return (T) putVariable(getVar(u),f+"*"+u);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mulf(T u, SLFloat f) {
        return (T) putVariable(getVar(u),u+"*"+f);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mulf(SLFloat f, T u) {
        return (T) putVariable(getVar(u),f+"*"+u);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T div(T u, T v) {
        return (T) putVariable(getVar(u),u+"/"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T div(T u, float f) {
        return (T) putVariable(getVar(u),u+"/"+f);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T div(float f, T u) {
        return (T) putVariable(getVar(u),f+"/"+u);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T pow(T u, T v) {
        return (T) putVariable(getVar(u),funcBuild("pow",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T pow(T u, float y) {
        return (T) putVariable(getVar(u),funcBuild("pow",u,y));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T sqrt(T u) {
        return (T) putVariable(getVar(u),funcBuild("sqrt",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T round(T u) {
        return (T) putVariable(getVar(u),funcBuild("round",u));
    }
    
    public <T extends SLData> SLFloat length(T u) {
        return putVariable(new SLFloatV(),funcBuild("length",u));
    }
    
    public <T extends SLData> SLFloat distance(T u, T v) {
        return putVariable(new SLFloatV(),funcBuild("distance",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T abs(T u) {
        return (T) putVariable(getVar(u),funcBuild("abs",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mix(T u, T v, T w) {
        return (T) putVariable(getVar(u),funcBuild("mix",u,v,w));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mix(T u, T v, SLFloat w) {
        return (T) putVariable(getVar(u),funcBuild("mix",u,v,w));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mix(T u, T v, float w) {
        return (T) putVariable(getVar(u),funcBuild("mix",u,v,w));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T min(T u, T v) {
        return (T) putVariable(getVar(u),funcBuild("min",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T max(T u, T v) {
        return (T) putVariable(getVar(u),funcBuild("max",u,v));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T clamp(T u, T min, T max) {
        return (T) putVariable(getVar(u),funcBuild("clamp",u,min,max));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T clamp(T u, SLFloat min, SLFloat max) {
        return (T) putVariable(getVar(u),funcBuild("clamp",u,min,max));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T clamp(T u, float min, float max) {
        return (T) putVariable(getVar(u),funcBuild("clamp",u,min,max));
    }
    
    public SLFloatV smoothstep(float edge0, float edge1, float x) {
        return putVariable(new SLFloatV(),funcBuild("smoothstep",edge0,edge1,x));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T smoothstep(float edge0, float edge1, T u) {
        return (T) putVariable(getVar(u),funcBuild("smoothstep",edge0,edge1,u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T smoothstep(T edge0, T edge1, T u) {
        return (T) putVariable(getVar(u),funcBuild("smoothstep",edge0,edge1,u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T sin(T u) {
        return (T) putVariable(getVar(u),funcBuild("cos",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T cos(T u) {
        return (T) putVariable(getVar(u),funcBuild("cos",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T tan(T u) {
        return (T) putVariable(getVar(u),funcBuild("cos",u));
    }
    
    
    
    
    
    public SLBool castBool(Object u) {
        return putVariable(new SLBoolV(),funcBuild("bool",u));
    }
    
    
    
    
    public SLBoolU  slBoolu(String id, boolean b)   { return putUniform(new SLBoolU(),id, b); }
    public SLBoolU  slBoolu(String id)              { return putUniform(new SLBoolU(),id, false); }
    public SLIntU   slIntu(String id, int i)        { return putUniform(new SLIntU(),id,  i); }
    public SLIntU   slIntu(String id)               { return putUniform(new SLIntU(),id,  0); }
    public SLFloatU slFloatu(String id, float f)    { return putUniform(new SLFloatU(),id,f); }
    public SLFloatU slFloatu(String id)             { return putUniform(new SLFloatU(),id,0f); }
    
    public SLVec4U vec4u(String id, Vector4 v) { return putUniform(new SLVec4U(),id,v.get()); }
    public SLVec4U vec4u(String id, float x, float y, float z, float w) { return putUniform(new SLVec4U(),id,new Vector4(x,y,z,w)); }
    public SLVec4U vec4u(String id) { return vec4u(id, new Vector4()); }
    
    public SLVec3U vec3u(String id, Vector3 v) { return putUniform(new SLVec3U(),id,v.get()); }
    public SLVec3U vec3u(String id, float x, float y, float z) { return putUniform(new SLVec3U(),id,new Vector3(x,y,z)); }
    public SLVec3U vec3u(String id) { return vec3u(id, new Vector3()); }
    
    public SLVec2U vec2u(String id, Vector2 v) { return putUniform(new SLVec2U(),id,v.get()); }
    public SLVec2U vec2u(String id, float x, float y) { return putUniform(new SLVec2U(),id,new Vector2(x,y)); }
    public SLVec2U vec2u(String id) { return vec2u(id, new Vector2()); }
    
    public SLMat4U mat4u(String id, Matrix4 m) { return putUniform(new SLMat4U(),id,m.get()); }
    public SLMat4U mat4u(String id) { return putUniform(new SLMat4U(),id,new Matrix4()); }
    
    public SLMat3U mat3u(String id, Matrix3 m) { return putUniform(new SLMat3U(),id,m.get()); }
    public SLMat3U mat3u(String id) { return putUniform(new SLMat3U(),id,new Matrix3()); }
    
    public SLMat2U mat2u(String id, Matrix2 m) { return putUniform(new SLMat2U(),id,m.get()); }
    public SLMat2U mat2u(String id) { return putUniform(new SLMat2U(),id,new Matrix2()); }
    
    public SLTexU texture(String id, Texture t) { return putUniform(new SLTexU(),id,new TextureUniform(t,textureCount++)); }
    public SLTexU texture(String id) { return putUniform(new SLTexU(),id,new TextureUniform(null,textureCount++)); }
    
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
    
    
    
    
    private static String getType(SLObject v) {
        if (v instanceof SLTexU)    return "sampler2D";
        if (v instanceof SLBool)    return "bool";
        if (v instanceof SLInt)     return "int";
        if (v instanceof SLFloat)   return "float";
        if (v instanceof SLVec4)    return "vec4";
        if (v instanceof SLVec3)    return "vec3";
        if (v instanceof SLVec2)    return "vec2";
        if (v instanceof SLMat4)    return "mat4";
        if (v instanceof SLMat3)    return "mat3";
        if (v instanceof SLMat2)    return "mat2";
        return "???";
    }
    
    private static SLVar getVar(SLData v) {
        if (v instanceof SLBool)    return new SLBoolV();
        if (v instanceof SLInt)     return new SLIntV();
        if (v instanceof SLFloat)   return new SLFloatV();
        if (v instanceof SLVec4)    return new SLVec4V();
        if (v instanceof SLVec3)    return new SLVec3V();
        if (v instanceof SLVec2)    return new SLVec2V();
        if (v instanceof SLMat4)    return new SLMat4V();
        if (v instanceof SLMat3)    return new SLMat3V();
        if (v instanceof SLMat2)    return new SLMat2V();
        return null;
    }
    
    private static SLData getData(Class<? extends SLData> c) {
        if (SLBool.class == c)    return new SLBoolV();
        if (SLInt.class == c)     return new SLIntV();
        if (SLFloat.class == c)   return new SLFloatV();
        if (SLVec4.class == c)    return new SLVec4V();
        if (SLVec3.class == c)    return new SLVec3V();
        if (SLVec2.class == c)    return new SLVec2V();
        if (SLMat4.class == c)    return new SLMat4V();
        if (SLMat3.class == c)    return new SLMat3V();
        if (SLMat2.class == c)    return new SLMat2V();
        return null;
    }
    
    private static SLUniform getUniform(SLUniform v) {
        if (v instanceof SLTexU)     return new SLTexU();
        if (v instanceof SLBoolU)    return new SLBoolU();
        if (v instanceof SLIntU)     return new SLIntU();
        if (v instanceof SLFloatU)   return new SLFloatU();
        if (v instanceof SLVec4U)    return new SLVec4U();
        if (v instanceof SLVec3U)    return new SLVec3U();
        if (v instanceof SLVec2U)    return new SLVec2U();
        if (v instanceof SLMat4U)    return new SLMat4U();
        if (v instanceof SLMat3U)    return new SLMat3U();
        if (v instanceof SLMat2U)    return new SLMat2U();
        return null;
    }
    
    
    
    
    
    private interface SLData {
        String getName();
    }
    
    private abstract static class SLObject {
        ShaderLanguage sl;
        String name;
        String type;
        @Override
        public String toString() {
            return name;
        }
    }
    
    public static interface SLBool  extends SLData {
        
    }
    public static interface SLInt   extends SLData {
        
    }
    public static interface SLFloat extends SLData {
        
    }
    public static interface SLVec4  extends SLData {
        SLVec4 mult(float f);
        SLVec4 mult(SLFloat f);
        SLVec4 mult(SLVec4 v);
        SLVec4 xyzw();
        SLVec3 xyz();
        SLVec2 xy();
        SLFloat x();
        SLFloat y();
        SLFloat z();
        SLFloat w();
    }
    public static interface SLVec3  extends SLData {
        SLVec3 mult(float f);
        SLVec3 mult(SLFloat f);
        SLVec3 mult(SLVec3 v);
        SLVec3 xyz();
        SLVec2 xy();
        SLFloat x();
        SLFloat y();
        SLFloat z();
    }
    public static interface SLVec2  extends SLData {
        SLVec2 mult(float f);
        SLVec2 mult(SLFloat f);
        SLVec2 mult(SLVec2 v);
        SLVec2 xy();
        SLFloat x();
        SLFloat y();
    }
    public static interface SLMat4  extends SLData {
        
    }
    public static interface SLMat3  extends SLData {
        
    }
    public static interface SLMat2  extends SLData {
        
    }
    
    abstract static class SLUniform extends SLObject {
        Object data;
        String id; //ID to request uniforms
        @Override
        public int hashCode() {
            return data.hashCode() + id.hashCode();
        }
        SLUniform copy() {
            SLUniform uniform = getUniform(this);
            uniform.sl = sl;
            uniform.name = name;
            uniform.type = type;
            uniform.data = this.data;
            uniform.id = this.id;
            return uniform;
        }
    }
    
    public static class SLTexU extends SLUniform implements SLData {
        public Texture get() { return (Texture) data; }
        public void set(Texture t)  { data = t; }
        public String getName() { return name; }
    }
    public static class SLBoolU extends SLUniform implements SLBool { 
        public boolean get() { return (boolean) data; }
        public void set(boolean b)  { data = b; }
        public String getName() { return name; }
    }
    public static class SLIntU   extends SLUniform implements SLInt { 
        public int get() { return (int) data; }
        public void set(int i)      { data = i; }
        public String getName() { return name; }
    }
    public static class SLFloatU extends SLUniform implements SLFloat { 
        public float get() { return (float) data; }
        public void set(float f)    { data = f; }
        public String getName() { return name; }
    }
    public static class SLVec4U  extends SLUniform implements SLVec4 { 
        public Vector4 get() { return (Vector4) data; }
        public void set(Vector4 v)  { data = v.get(); }
        public void set(float x, float y, float z, float w)  { data = new Vector4(x,y,z,w); }
        public String getName() { return name; }
        
        //Override
        public SLVec4 mult(float f) { return sl.mul(this,f); }
        public SLVec4 mult(SLFloat f) { return sl.mulf(this,f); }
        public SLVec4 mult(SLVec4 v) { return sl.mul(this,v); }
        public SLVec4 xyzw() { return sl.xyzw(this); }
        public SLVec3 xyz() { return sl.xyz(this); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
        public SLFloat z() { return sl.z(this); }
        public SLFloat w() { return sl.w(this); }
    }
    public static class SLVec3U  extends SLUniform implements SLVec3 { 
        public Vector3 get() { return (Vector3) data; }
        public void set(Vector3 v)  { data = v.get(); }
        public void set(float x, float y, float z)  { data = new Vector3(x,y,z); }
        public String getName() { return name; }
        
        //Override
        public SLVec3 mult(float f) { return sl.mul(this,f); }
        public SLVec3 mult(SLFloat f) { return sl.mulf(this,f); }
        public SLVec3 mult(SLVec3 v) { return sl.mul(this,v); }
        public SLVec3 xyz() { return sl.xyz(this); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
        public SLFloat z() { return sl.z(this); }
    }
    public static class SLVec2U  extends SLUniform implements SLVec2 { 
        public Vector2 get() { return (Vector2) data; }
        public void set(Vector2 v)  { data = v.get(); }
        public void set(float x, float y)  { data = new Vector2(x,y); }
        public String getName() { return name; }
        
        //Override
        public SLVec2 mult(float f) { return sl.mul(this,f); }
        public SLVec2 mult(SLFloat f) { return sl.mulf(this,f); }
        public SLVec2 mult(SLVec2 v) { return sl.mul(this,v); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
    }
    public static class SLMat4U  extends SLUniform implements SLMat4 { 
        public Matrix4 get() { return (Matrix4) data; }
        public void set(Matrix4 m)  { data = m.get(); }
        public String getName() { return name; }
    }
    public static class SLMat3U  extends SLUniform implements SLMat3 { 
        public Matrix3 get() { return (Matrix3) data; }
        public void set(Matrix3 m)  { data = m.get(); }
        public String getName() { return name; }
    }
    public static class SLMat2U  extends SLUniform implements SLMat2 { 
        public Matrix2 get() { return (Matrix2) data; }
        public void set(Matrix2 m)  { data = m.get(); }
        public String getName() { return name; }
    }
    
    private abstract static class SLVar extends SLObject {
        String value;
    }
    
    public static class SLBoolV extends SLVar implements SLBool { 
        public String getName() { return name; }
    }
    public static class SLIntV   extends SLVar implements SLInt { 
        public String getName() { return name; }
    }
    public static class SLFloatV extends SLVar implements SLFloat { 
        public String getName() { return name; }
    }
    public static class SLVec4V  extends SLVar implements SLVec4 { 
        public String getName() { return name; }
        
        //Override
        public SLVec4 mult(float f) { return sl.mul(this,f); }
        public SLVec4 mult(SLFloat f) { return sl.mulf(this,f); }
        public SLVec4 mult(SLVec4 v) { return sl.mul(this,v); }
        public SLVec4 xyzw() { return sl.xyzw(this); }
        public SLVec3 xyz() { return sl.xyz(this); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
        public SLFloat z() { return sl.z(this); }
        public SLFloat w() { return sl.w(this); }
    }
    public static class SLVec3V  extends SLVar implements SLVec3 { 
        public String getName() { return name; }
        
        //Override
        public SLVec3 mult(float f) { return sl.mul(this,f); }
        public SLVec3 mult(SLFloat f) { return sl.mulf(this,f); }
        public SLVec3 mult(SLVec3 v) { return sl.mul(this,v); }
        public SLVec3 xyz() { return sl.xyz(this); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
        public SLFloat z() { return sl.z(this); }
    }
    public static class SLVec2V  extends SLVar implements SLVec2 { 
        public String getName() { return name; }
        
        //Override
        public SLVec2 mult(float f) { return sl.mul(this,f); }
        public SLVec2 mult(SLFloat f) { return sl.mulf(this,f); }
        public SLVec2 mult(SLVec2 v) { return sl.mul(this,v); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
    }
    public static class SLMat4V  extends SLVar implements SLMat4 { 
        public String getName() { return name; }
    }
    public static class SLMat3V  extends SLVar implements SLMat3 { 
        public String getName() { return name; }
    }
    public static class SLMat2V  extends SLVar implements SLMat2 { 
        public String getName() { return name; }
    }
    
    
    
    public static class SLFunc<T extends SLData> {
        
        ShaderLanguage sl;
        String name;
        SLData ref;
        ArrayList<String> args = new ArrayList<>();
        public void ret(T r) {
            sl.returnStatement(r);
        }
        
    }
    
    
    
    
    
    
    
    
}
