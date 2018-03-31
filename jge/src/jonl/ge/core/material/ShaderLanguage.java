package jonl.ge.core.material;

import java.util.ArrayList;
import java.util.HashMap;

import jonl.ge.core.Texture;
import jonl.ge.core.TextureUniform;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Function0D;
import jonl.jutils.func.Tuple2;
import jonl.jutils.misc.StringUtils;
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
    
    /** List of Uniforms */
    private final ArrayList<String> slUniformDeclareList = new ArrayList<>();
    private final ArrayList<SLUniform> slUniformList = new ArrayList<>();
    private int uniformNameId;
    private int textureCount;
    
    /** List of consts */
    private final ArrayList<String> slConstList = new ArrayList<>();
    private int constCount;
    
    /** List of structs */
    private final ArrayList<String> slStructList = new ArrayList<>();
    private int structCount;
    
    /** List of Statements to be executed in order */
    private final ArrayList<String> slStatementList = new ArrayList<>();
    private int variableCount = 0;
    
    private String version = "#version 130\n";
    
    private final ArrayList<String> slFunctionList = new ArrayList<>();
    private ArrayList<String> slFunctionBuilder = new ArrayList<>();
    private boolean inFunction = false;
    private int functionCount = 0;
    
    private final String varKey;
    
    //To id unique generated materials we use a static unique_gm for every GSLuilder
    //with another int everytime the GSL is changed and return _gm_[unique_gm_id]_[unique_gm_changed]
    //This is so that only one shader is generated for every unique GM
    int unique_gm_id = 0;
    int unique_gm_changed = 0;
    static int unique_gm_count = 0;
    
    /**
     * This key will be appended to uniform names to handle linking errors between shaders of the same program
     * @param key
     */
    public ShaderLanguage(String key) {
        unique_gm_id = unique_gm_count++;
        varKey = key;
    }
    
    public ShaderLanguage() {
        this("");
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
    
    protected ArrayList<String> getUniformStringList() {
        return slUniformDeclareList;
    }
    
    protected ArrayList<SLUniform> getUniformList() {
        return slUniformList;
    }
    
    protected ArrayList<String> getStatementList() {
        return slStatementList;
    }
    
    protected ArrayList<String> getFunctionList() {
        return slFunctionList;
    }
    
    String getUniformString(ArrayList<String> uniforms) {
        StringBuilder sb = new StringBuilder();
        for (String s : uniforms) {
            sb.append(s); //"uniform "+u.type+" "+u.name+";\n"
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
    
    String getFunctionString(ArrayList<String> functions) {
        StringBuilder sb = new StringBuilder();
        for (String s : functions) {
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
        
        for (String s : slStructList) {
        	sb.append(s);
        }
        
        for (String a : slAttributeList) {
            sb.append(a);
        }
        
        for (String u : slUniformDeclareList) {
            sb.append(u);
        }
        
        for (String c : slConstList) {
            sb.append(c);
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
    
    
    // Unique generated names
    
    private String genName() {
        String name = "_gmv_"+variableCount;
        variableCount++;
        return name;
    }
    
    private String genConstName() {
        String name = "_gmconst_"+constCount;
        constCount++;
        return name;
    }
    
    private String genUniformName() {
        String name = varKey+"_gmuni_"+uniformNameId;
        uniformNameId++;
        return name;
    }
    
    private String genStructName() {
        String name = "_gmstruct_"+structCount;
        structCount++;
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
    
    private <T extends SLVar> T putConstVariable(T v, String value) {
        unique_gm_changed++;
        v.sl = this;
        v.type = getType(v);
        v.name = genConstName();
        v.value = value;
        String declaration = "const "+v.type+" "+v.name+" = "+v.value+";\n";
        slConstList.add(declaration);
        return v;
    }
    
    private void putAttribute(String attribute) {
        unique_gm_changed++;
        slAttributeList.add(attribute+";\n");
    }
    
    private void putHardUniform(String uniform) {
        unique_gm_changed++;
        slUniformDeclareList.add(uniform+";\n");
    }
    
    private <T extends SLUniform> T putUniform(T u, String type, String name, String id, Object data, boolean declare) {
    	unique_gm_changed++;
        u.sl = this;
        u.type = type;
        u.name = name;
        u.id = id;
        u.data = data;
        if (declare) {
        	slUniformDeclareList.add("uniform "+u.type+" "+u.name+";\n");
        }
        slUniformList.add(u);
        return u;
    }
    
    private <T extends SLUniform> T putUniform(T u, String id, Object data) {
        return putUniform(u, getType(u), genUniformName(), id, data, true);
    }
    
    private <T extends SLDeclare<?>> T putStruct(T s) {
    	unique_gm_changed++;
    	s.name = genStructName();
    	slStructList.add("struct "+s.name+" {\n");
    	for (String v : s.declares) {
    		slStructList.add(v);
    	}
    	slStructList.add("};\n");
    	return s;
    }
    
    private <T extends SLData> SLFunc<T> putFunc(String body, SLFunc<T> f, Class<T> returnClass, Tuple2<String,String>[] arglist) {
        return putFunc(body,genFuncName(),f,returnClass,arglist);
    }
    
    private <T extends SLData> SLFunc<T> putFunc(String body, String fname, SLFunc<T> f, Class<T> returnClass, Tuple2<String,String>[] arglist) {
        unique_gm_changed++;
        f.sl = this;
        f.name = fname;
        f.ref = getData(returnClass);
        
        String[] argTypes = new String[arglist.length];
        String[] argNames = new String[arglist.length];
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
        return putBegin(f,genFuncName(),returnClass,argTypes);
    }
    
    private <T extends SLData> SLFunc<T> putBegin(SLFunc<T> f, String name, Class<T> returnClass, String... argTypes) {
        unique_gm_changed++;
        f.sl = this;
        f.name = name;
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
    
    public <T extends SLInclude> T include(T include) {
        include.include(this);
        return include;
    }
    
    @SuppressWarnings("unchecked")
	private <T extends SLData> T layout(String layoutType, Class<T> returnClass, int index, String name) {
    	SLVar v = (SLVar) getData(returnClass);
    	v.sl = this;
        v.type = getType(v);
        v.name = name;
        putAttribute(String.format("layout (location = %d) %s %s %s",layoutType,index,v.type,v.name));
        return (T) v;
    }
	public <T extends SLData> T layoutIn(Class<T> returnClass, int index, String name) {
    	return layout("in",returnClass,index,name);
    }
	public <T extends SLData> T layoutOut(Class<T> returnClass, int index, String name) {
    	return layout("out",returnClass,index,name);
    }
	public void layoutIn(int index, String a) { putAttribute(String.format("layout (location = %d) in %s",index,a)); }
    public void layoutOut(int index, String a) { putAttribute(String.format("layout (location = %d) out %s",index,a)); }
    
	
	
    @SuppressWarnings("unchecked")
	private <T extends SLData> T attribute(String attributeType, Class<T> returnClass, String name) {
    	SLVar v = (SLVar) getData(returnClass);
    	v.sl = this;
        v.type = getType(v);
        v.name = name;
        putAttribute(String.format("%s %s %s",attributeType,v.type,v.name));
        return (T) v;
    }
    /** puts: in [type] [name];\n */
    public <T extends SLData> T attributeIn(Class<T> returnClass, String name) {
    	return attribute("in",returnClass,name);
    }
    /** puts: out [type] [name];\n */
	public <T extends SLData> T attributeOut(Class<T> returnClass, String name) {
    	return attribute("out",returnClass,name);
    }
	/** puts: in [attrib];\n */
	public void attributeIn(String attrib) { putAttribute(String.format("in %s",attrib)); }
	/** puts: out [attrib];\n */
    public void attributeOut(String attrib) { putAttribute(String.format("out %s",attrib)); }
	
	
    
    public <T extends SLStruct> SLDeclare<T> declare(Function0D<T> create) {
    	return putStruct(new SLDeclare<T>(this, create));
    }
    
    
    
    @SuppressWarnings("unchecked")
	public <T extends SLData> T uniform(Class<T> returnClass, String id) {
    	return (T) putUniform(getUniform(returnClass),id,null);
	}
    
    public void uniform(String u) { putHardUniform(String.format("uniform %s",u)); }
    
    public <T extends SLStruct> T uniform(SLDeclare<T> declare, String name) {
    	T s = declare.create.f();
    	s.name = name;
    	SLStructBuilder sb = new SLStructBuilder(this);
    	sb.setStruct(s);
    	sb.setCallback((v) -> {
    		putUniform(getUniform(v), v.type, v.name, v.name, null, false);
    	});
    	s.init(sb);
    	sb.setCallback(null);
    	sb.setStruct(null);
    	putHardUniform(String.format("uniform %s %s",declare.name,name));
    	return s;
	}
    
    public <T extends SLStruct> SLArray<T> arrayu(SLDeclare<T> declare, String name, int length) {
    	T s = declare.create.f();
    	s.name = name;
    	SLStructBuilder sb = new SLStructBuilder(this);
    	sb.setStruct(s);
    	sb.setCallback((v) -> {
    		for (int i=0; i<length; i++) {
    			String vname = v.name.replace(".", "["+i+"].");
    			putUniform(getUniform(v), v.type, vname, vname, null, false);
    		}
    	});
    	s.init(sb);
    	sb.setCallback(null);
    	sb.setStruct(null);
    	putHardUniform(String.format("uniform %s %s[%d]",declare.name,name,length));
    	return new SLArray<T>(declare,name);
	}
    
    private <T extends SLStruct> T index(SLArray<T> array, String index) {
    	T s = array.declare.create.f();
    	s.name = array.name;
    	SLStructBuilder sb = new SLStructBuilder(this);
    	sb.setStruct(s);
    	sb.setIndex(index);
    	s.init(sb);
    	sb.setIndex(null);
    	sb.setStruct(null);
    	return s;
    }
    public <T extends SLStruct> T index(SLArray<T> array, int index) {
    	return index(array,index+"");
    }
    public <T extends SLStruct> T index(SLArray<T> array, SLInt index) {
    	return index(array,index+"");
    }
    
    /** Adds string to body (without semicolon and newline, use putStatement for that) */
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
    
    public <T extends SLData> SLFunc<T> slBegin(SLFunc<T> f, Class<T> returnClass, String... argTypes) {
        return putBegin(new SLFunc<T>(), f.name, returnClass, argTypes);
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
    private void putEndBracket()       	{ putString("}\n"); }
    
    public void slIf(SLBool bool)       { putIf(bool); }
    public void slElseIf(SLBool bool)   { putElseIf(bool); }
    public void slElse()                { putElse(); }
    public void slEndIf()               { putEndBracket(); }
    
    public SLInt slLoop(int start, int end, int increment) {
    	SLInt i = slInt(start);
    	putString(String.format("for (%s = %s; %s < %s; %s += %s) {\n", i,start, i,end, i,increment));
    	return i;
    }
    public void slEndLoop()            	{ putEndBracket(); }
    
    // Const Variables ----------------------------------------------------------------------------------
    
    public SLBool   slBoolc  (boolean b) 	{ return putConstVariable(new SLBoolV(),  b+""); }
    public SLInt    slIntc   (int i)     	{ return putConstVariable(new SLIntV(),   i+""); }
    public SLFloat  slFloatc (float f)   	{ return putConstVariable(new SLFloatV(), f+""); }
    
    private SLVec4 vec4pv(Object... params) {
        return putConstVariable(new SLVec4V(),funcBuild("vec4",params));
    }
    public SLVec4   vec4c  (float x, float y, float z, float w)		{ return vec4pv(x,y,z,w); }
    public SLVec4   vec4c  (float v)								{ return vec4pv(v,v,v,v); }
    
    private SLVec3 vec3pv(Object... params) {
        return putConstVariable(new SLVec3V(),funcBuild("vec3",params));
    }
    public SLVec3   vec3c  (float x, float y, float z)				{ return vec3pv(x,y,z); }
    public SLVec3   vec3c  (float v)								{ return vec3pv(v,v,v); }
    
    private SLVec2 vec2pv(Object... params) {
        return putConstVariable(new SLVec2V(),funcBuild("vec2",params));
    }
    public SLVec2   vec2c  (float x, float y)						{ return vec2pv(x,y); }
    public SLVec2   vec2c  (float v)								{ return vec2pv(v,v); }
    
    // Variables ----------------------------------------------------------------------------------------
    
    public SLBool   slBool(String var)  { return putVariable(new SLBoolV(),     var); }
    public SLInt    slInt(String var)   { return putVariable(new SLIntV(),      var); }
    public SLFloat  slFloat(String var) { return putVariable(new SLFloatV(),    var); }
    public SLVec4   vec4(String var)    { return putVariable(new SLVec4V(),     var); }
    public SLVec3   vec3(String var)    { return putVariable(new SLVec3V(),     var); }
    public SLVec2   vec2(String var)    { return putVariable(new SLVec2V(),     var); }
    
    public SLBool   slBool  (boolean b) { return putVariable(new SLBoolV(),  b+""); }
    public SLInt    slInt   (int i)     { return putVariable(new SLIntV(),   i+""); }
    public SLFloat  slFloat (float f)   { return putVariable(new SLFloatV(), f+""); }
    
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
    public SLVec3 vec3(SLFloat x, SLFloat y, float z)               { return vec3p(x,y,z); }
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
    
    /*
    public SLVec4 sample(SLTexU u) {
        return putVariable(new SLVec4V(),"texture2D("+u.name+",fTexCoord)");
    }*/
    public SLVec4 sample(SLTexU u, String texCoord) {
        return putVariable(new SLVec4V(),"texture2D("+u.name+","+texCoord+")");
    }
    public SLVec4 sample(SLTexU u, SLVec2 texCoord) {
        return putVariable(new SLVec4V(),"texture2D("+u.name+","+texCoord+")");
    }
    
    // --------------------------------------------------------------------------------------------------
    
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
    
    public SLVec3 add(SLVec3 u, SLFloat v) {
        return putVariable(new SLVec3V(),u+"+"+v);
    }
    
    public SLFloat add(float u, SLFloat v) {
        return putVariable(new SLFloatV(),u+"+"+v);
    }
    
    public SLFloat add(SLFloat u, float v) {
        return putVariable(new SLFloatV(),u+"+"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T sub(T u, T v) {
        return (T) putVariable(getVar(u),u+"-"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T sub(T u, float v) {
        return (T) putVariable(getVar(u),u+"-"+v);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T sub(float u, T v) {
        return (T) putVariable(getVar(v),u+"-"+v);
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
    public <T extends SLData> T mul(T x, SLFloat y, SLFloat z) {
        return (T) putVariable(getVar(x),x+"*"+y+"*"+z);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T mul(T x, SLFloat y, SLFloat z, SLFloat w) {
        return (T) putVariable(getVar(x),x+"*"+y+"*"+z+"*"+w);
    }
    
    @SuppressWarnings("unchecked") public <T extends SLData> T mul(T x, float y, SLFloat z, SLFloat w) { return (T) putVariable(getVar(x),x+"*"+y+"*"+z+"*"+w); }
    @SuppressWarnings("unchecked") public <T extends SLData> T mul(T x, SLFloat y, float z, SLFloat w) { return (T) putVariable(getVar(x),x+"*"+y+"*"+z+"*"+w); }
    @SuppressWarnings("unchecked") public <T extends SLData> T mul(T x, SLFloat y, SLFloat z, float w) { return (T) putVariable(getVar(x),x+"*"+y+"*"+z+"*"+w); }
    
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
    public <T extends SLData> T neg(T u) {
        return (T) putVariable(getVar(u),"-"+u);
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
    
    @SuppressWarnings("unchecked")
	public <T extends SLData> T normalize(T u) {
        return (T) putVariable(getVar(u),funcBuild("normalize",u));
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
        return (T) putVariable(getVar(u),funcBuild("sin",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T cos(T u) {
        return (T) putVariable(getVar(u),funcBuild("cos",u));
    }
    
    @SuppressWarnings("unchecked")
    public <T extends SLData> T tan(T u) {
        return (T) putVariable(getVar(u),funcBuild("tan",u));
    }
    
    
    
    public SLVec4 mul(SLMat4 m, SLVec4 v) {
        return (SLVec4) putVariable(getVar(v),m+"*"+v);
    }
    
    public SLVec3 mul(SLMat3 m, SLVec3 v) {
        return (SLVec3) putVariable(getVar(v),m+"*"+v);
    }
    
    public SLVec2 mul(SLMat2 m, SLVec2 v) {
        return (SLVec2) putVariable(getVar(v),m+"*"+v);
    }
    
    
    public SLVec4 mul(SLVec4 u, Vector4 v) {
        return (SLVec4) putVariable(getVar(u),u+"*"+j(v));
    }
    public SLVec3 mul(SLVec3 u, Vector3 v) {
        return (SLVec3) putVariable(getVar(u),u+"*"+j(v));
    }
    public SLVec2 mul(SLVec2 u, Vector2 v) {
        return (SLVec2) putVariable(getVar(u),u+"*"+j(v));
    }
    
    // Standard OpenGL Calls
    
    public void gl_PointSize(float f) {
        putStatement("gl_PointSize = "+f);
    }
    
    public void gl_PointSize(SLFloat f) {
        putStatement("gl_PointSize = "+f);
    }
    
    public void gl_FragColor(SLVec4 v) {
        putStatement("gl_FragColor = "+v);
    }
    
    public final SLVec4V gl_FragColor = gl(new SLVec4V(), "gl_FragColor");
    public final SLVec4V gl_FragCoord = gl(new SLVec4V(), "gl_FragCoord");
    public final SLVec2V gl_PointCoord = gl(new SLVec2V(), "gl_PointCoord");
    public final SLFloatV gl_PointSize = gl(new SLFloatV(), "gl_PointSize");
    
    private <T extends SLObject> T gl(T v, String name) {
        v.sl = this;
        v.type = getType(v);
        v.name = name;
        return v;
    }
    
    
    
    
    private String jvec4(float x, float y, float z, float w) {
    	return StringUtils.f("vec4(%,%,%,%)", x, y,z,w);
    }
    private String jvec3(float x, float y, float z) {
    	return StringUtils.f("vec3(%,%,%)", x, y,z);
    }
    private String jvec2(float x, float y) {
    	return StringUtils.f("vec2(%,%)", x, y);
    }
    
    private String j(Vector4 v) { return jvec4(v.x,v.y,v.z,v.w); }
    private String j(Vector3 v) { return jvec3(v.x,v.y,v.z); }
    private String j(Vector2 v) { return jvec2(v.x,v.y); }
    
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
    
    private static SLUniform getUniform(SLVar v) {
        if (v instanceof SLBoolV)    return new SLBoolU();
        if (v instanceof SLIntV)     return new SLIntU();
        if (v instanceof SLFloatV)   return new SLFloatU();
        if (v instanceof SLVec4V)    return new SLVec4U();
        if (v instanceof SLVec3V)    return new SLVec3U();
        if (v instanceof SLVec2V)    return new SLVec2U();
        if (v instanceof SLMat4V)    return new SLMat4U();
        if (v instanceof SLMat3V)    return new SLMat3U();
        if (v instanceof SLMat2V)    return new SLMat2U();
        return null;
    }
    
    private static SLUniform getUniform(Class<? extends SLData> c) {
        if (SLBool.class == c)    return new SLBoolU();
        if (SLInt.class == c)     return new SLIntU();
        if (SLFloat.class == c)   return new SLFloatU();
        if (SLVec4.class == c)    return new SLVec4U();
        if (SLVec3.class == c)    return new SLVec3U();
        if (SLVec2.class == c)    return new SLVec2U();
        if (SLMat4.class == c)    return new SLMat4U();
        if (SLMat3.class == c)    return new SLMat3U();
        if (SLMat2.class == c)    return new SLMat2U();
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
        SLVec4 mul(float f);
        SLVec4 mul(SLFloat f);
        SLVec4 mul(SLVec4 v);
        SLVec4 xyzw();
        SLVec3 xyz();
        SLVec2 xy();
        SLFloat x();
        SLFloat y();
        SLFloat z();
        SLFloat w();
    }
    public static interface SLVec3  extends SLData {
        SLVec3 mul(float f);
        SLVec3 mul(SLFloat f);
        SLVec3 mul(SLVec3 v);
        SLVec3 xyz();
        SLVec2 xy();
        SLFloat x();
        SLFloat y();
        SLFloat z();
    }
    public static interface SLVec2  extends SLData {
        SLVec2 mul(float f);
        SLVec2 mul(SLFloat f);
        SLVec2 mul(SLVec2 v);
        SLVec2 xy();
        SLFloat x();
        SLFloat y();
    }
    public static interface SLMat4  extends SLData {
        SLVec4 mul(SLVec4 v);
    }
    public static interface SLMat3  extends SLData {
        
    }
    public static interface SLMat2  extends SLData {
        
    }
    
    public abstract static class SLUniform extends SLObject {
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
        public SLVec4 mul(float f) { return sl.mul(this,f); }
        public SLVec4 mul(SLFloat f) { return sl.mulf(this,f); }
        public SLVec4 mul(SLVec4 v) { return sl.mul(this,v); }
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
        public SLVec3 mul(float f) { return sl.mul(this,f); }
        public SLVec3 mul(SLFloat f) { return sl.mulf(this,f); }
        public SLVec3 mul(SLVec3 v) { return sl.mul(this,v); }
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
        public SLVec2 mul(float f) { return sl.mul(this,f); }
        public SLVec2 mul(SLFloat f) { return sl.mulf(this,f); }
        public SLVec2 mul(SLVec2 v) { return sl.mul(this,v); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
    }
    public static class SLMat4U  extends SLUniform implements SLMat4 { 
        public Matrix4 get() { return (Matrix4) data; }
        public void set(Matrix4 m)  { data = m.get(); }
        public String getName() { return name; }
        
        //Override
        public SLVec4 mul(SLVec4 v) { return sl.mul(this, v); }
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
    public static class SLIntC extends SLIntV { }
    public static class SLFloatV extends SLVar implements SLFloat { 
        public String getName() { return name; }
    }
    public static class SLVec4V  extends SLVar implements SLVec4 { 
        public String getName() { return name; }
        
        //Override
        public SLVec4 mul(float f) { return sl.mul(this,f); }
        public SLVec4 mul(SLFloat f) { return sl.mulf(this,f); }
        public SLVec4 mul(SLVec4 v) { return sl.mul(this,v); }
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
        public SLVec3 mul(float f) { return sl.mul(this,f); }
        public SLVec3 mul(SLFloat f) { return sl.mulf(this,f); }
        public SLVec3 mul(SLVec3 v) { return sl.mul(this,v); }
        public SLVec3 xyz() { return sl.xyz(this); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
        public SLFloat z() { return sl.z(this); }
    }
    public static class SLVec2V  extends SLVar implements SLVec2 { 
        public String getName() { return name; }
        
        //Override
        public SLVec2 mul(float f) { return sl.mul(this,f); }
        public SLVec2 mul(SLFloat f) { return sl.mulf(this,f); }
        public SLVec2 mul(SLVec2 v) { return sl.mul(this,v); }
        public SLVec2 xy() { return sl.xy(this); }
        public SLFloat x() { return sl.x(this); }
        public SLFloat y() { return sl.y(this); }
    }
    public static class SLMat4V  extends SLVar implements SLMat4 { 
        public String getName() { return name; }
        
        //Override
        public SLVec4 mul(SLVec4 v) { return sl.mul(this, v); }
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
        
        @SuppressWarnings("unchecked")
		public <V extends SLData> SLFunc<V> append(String body, Class<V> returnClass, Tuple2<String,String>... args) {
            return sl.putFunc(body, this.name, new SLFunc<V>(), returnClass, args);
        }
        
        @Override
        public String toString() {
        	return name;
        }
        
    }
    
    public static class SLDeclare<T extends SLStruct> {
    	String name;
    	Function0D<T> create;
    	ArrayList<String> declares = new ArrayList<>();
    	SLDeclare(ShaderLanguage sl, Function0D<T> create) {
    		this.create = create;
    		T struct = this.create.f();
    		SLStructBuilder sb = new SLStructBuilder(sl);
    		sb.setDeclare(this);
    		struct.init(sb);
    		sb.setDeclare(null);
    	}
    }
    public static abstract class SLStruct {
    	String name;
    	public abstract void init(SLStructBuilder sb);
    }
    public static class SLStructBuilder {
    	private SLDeclare<?> sd;
    	private ShaderLanguage sl;
    	private SLStruct struct;
    	private Callback<SLVar> cbDeclare;
    	private String index = null; //can be an integer or a glsl int variable name
    	SLStructBuilder(ShaderLanguage sl) {
    		this.sl = sl;
    	}
    	/** This method should only be called by an SLDeclare itself to grab struct members before init is called */
    	void setDeclare(SLDeclare<?> sd) {
    		this.sd = sd;
    	}
    	/** This method should only be called when declaring a variable of type struct, before init is called */
    	void setStruct(SLStruct struct) {
    		this.struct = struct;
    	}
    	/** This method should only be called to set a callback before init is called */
    	void setCallback(Callback<SLVar> cbDeclare) {
    		this.cbDeclare = cbDeclare;
    	}
    	/** This method is called for array initialization before init is called */
    	void setIndex(String index) {
    		this.index = index;
    	}
    	@SuppressWarnings("unchecked")
		private <T extends SLData> T declare(Class<T> c, String name) {
    		SLVar v = (SLVar) getData(c);
    		v.sl = sl;
    		v.type = getType(v);
    		if (struct==null) {
    			v.name = name;
    		} else {
    			if (index==null) {
    				v.name = String.format("%s.%s", struct.name, name);
        		} else {
        			v.name = String.format("%s[%s].%s", struct.name, index, name);
        		}
    		}
    		if (sd != null) {
    			sd.declares.add(String.format("%s %s;\n", v.type, v.name));
    		}
    		if (cbDeclare != null) {
    			cbDeclare.f(v);
    		}
    		return (T) v;
    	}
    	public SLBool slBool(String name) {
    		return declare(SLBool.class, name);
    	}
    	public SLInt slInt(String name) {
    		return declare(SLInt.class, name);
    	}
    	public SLFloat slFloat(String name) {
    		return declare(SLFloat.class, name);
    	}
    	public SLVec4 vec4(String name) {
    		return declare(SLVec4.class, name);
    	}
    	public SLVec3 vec3(String name) {
    		return declare(SLVec3.class, name);
    	}
    	public SLVec2 vec2(String name) {
    		return declare(SLVec2.class, name);
    	}
    	public SLMat4 mat4(String name) {
    		return declare(SLMat4.class, name);
    	}
    	public SLMat3 mat3(String name) {
    		return declare(SLMat3.class, name);
    	}
    	public SLMat2 mat2(String name) {
    		return declare(SLMat2.class, name);
    	}
    	
    }
    
    
    public static class SLArray<T extends SLStruct> {
    	
    	String name;
    	SLDeclare<T> declare;
    	
    	SLArray(SLDeclare<T> declare, String name) {
    		this.declare = declare;
    		this.name = name;
    	}
    	
    }
    
    
    public interface SLInclude {
        void include(ShaderLanguage sl);
    }
    
    
}
