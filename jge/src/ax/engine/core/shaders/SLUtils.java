package ax.engine.core.shaders;

import ax.commons.func.Tuple2;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.ShaderLanguage.SLBool;
import ax.engine.core.material.ShaderLanguage.SLFloat;
import ax.engine.core.material.ShaderLanguage.SLFunc;
import ax.engine.core.material.ShaderLanguage.SLVec2;
import ax.engine.core.material.ShaderLanguage.SLVec4;

public class SLUtils {

	/**
	 * Returns a basic vertex shader
	 * <p>
	 * This vs outputs attributes: vec3 vNormal, vec2 vTexCoord
	 * @return
	 */
    public static ShaderLanguage basicVert() {
        
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        sl.layoutIn(1,"vec3 normal");
        sl.layoutIn(2,"vec2 texCoord");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 M");
        
        sl.attributeOut("vec3 vPosition");
        sl.attributeOut("vec3 vNormal");
        sl.attributeOut("vec2 vTexCoord");
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.putStatement("vPosition = vec3(M * vertex)");
        sl.putStatement("vNormal = normalize ( mat3(M) * normal )");
        sl.putStatement("vTexCoord = texCoord");
        
        return sl;
        
    }
    
    public static ShaderLanguage normalVert() {
        
        ShaderLanguage sl = new ShaderLanguage();
        
        sl.version("330");
        
        sl.layoutIn(0,"vec4 vertex");
        sl.layoutIn(1,"vec3 normal");
        sl.layoutIn(2,"vec2 texCoord");
        sl.layoutIn(3,"vec3 tangent");
        sl.layoutIn(4,"vec3 bitangent");
        
        sl.uniform("mat4 MVP");
        sl.uniform("mat4 M");
        
        sl.attributeOut("vec3 vPosition");
        sl.attributeOut("vec3 vNormal");
        sl.attributeOut("vec2 vTexCoord");
        sl.attributeOut("vec3 vTangent");
        sl.attributeOut("vec3 vBitangent");
        sl.attributeOut("mat3 mTBN");
        
        sl.putStatement("gl_Position = MVP * vertex");
        
        sl.putStatement("mat3 mNormal = transpose(inverse(mat3(M)))");
        
        sl.putStatement("vPosition = vec3(M * vertex)");
        sl.putStatement("vNormal = normalize ( mNormal * normal )");
        sl.putStatement("vTexCoord = texCoord");
        
        // TODO find out whether tangent and bitangent vectors should be multipled by M or T(I(M))
        sl.putStatement("vTangent = normalize ( mat3(mNormal) * tangent )");
        sl.putStatement("vBitangent = normalize ( mat3(mNormal) * bitangent )");
        
        sl.putStatement("mTBN = mat3(vTangent,vBitangent,vNormal)");
        
        return sl;
        
    }
    
    // TODO move SLFunc functions to SLImports
    
    
    
    
    
    
    /*
    public static SLFunc<SLFloat> dLine(ShaderLanguage sl) {
        SLFunc<SLFloat> func = sl.slBegin(SLFloat.class, "vec2", "float", "float"); {
            
            sl.putStatement("vec2 p = "+sl.arg(0));
            sl.putStatement("float m = "+sl.arg(1));
            sl.putStatement("float b = "+sl.arg(2));
            
            sl.putString(
                "// If slope is 0, get y distance to horizontal line\n" + 
                "    if (m==0.0) {\n" + 
                "        return abs(p.y - b);\n" + 
                "    }\n" + 
                "\n" + 
                "    // Find right triangle intersection points\n" + 
                "    vec2 u = vec2(p.x, (m*p.x + b)); // (y adj leg) ( y = mx + b )\n" + 
                "    vec2 v = vec2(((p.y - b)/m), p.y); // (x adj leg) ( x = (y-b)/m )\n" + 
                "\n" + 
                "    // Find length of sides of right triangle \n" + 
                "    float y = distance(p,u);\n" + 
                "    float x = distance(p,v);\n" + 
                "    float h = distance(u,v);\n" + 
                "\n" + 
                "    // Calculate shortest distance from p to line (height of triangle)\n" + 
                "    float d = x*y / h;\n" + 
                "    if (h < 0.00001) return 0;\n" +
                "    \n" + 
                "    return d;"
            );
            
        }
        sl.slEnd();
        
        return func;
    }
    */
    
    private static Tuple2<String,String> arg(String type, String name) {
        return new Tuple2<String,String>(type,name);
    }
    
    /**
     * Based on {@link ax.engine.core.Geometry#scaleTexCoords(float,float,float,float) Geometry.scaleTexCoords(float,float,float,float)}
     * @param sl
     * @return
     */
    @SuppressWarnings("unchecked")
    public static SLFunc<SLVec2> scaleTexCoord(ShaderLanguage sl) {
        
        String body = 
        "    vec2 dir = uv - vec2(0.5,0.5);\n" +
        "    dir *= scale;\n" +
        
        "    return vec2(0.5,0.5) + dir;";
        
        SLFunc<SLVec2> func = sl.slFunc(body, SLVec2.class, arg("vec2","uv"), arg("vec2","scale"));
        
        return func;
    }
    
    @SuppressWarnings("unchecked")
    public static SLFunc<SLFloat> dLine2(ShaderLanguage sl) {
        
        String body = 
             // If slope is 0, get y distance to horizontal line
        "    if (m==0.0) {\n" + 
        "        return abs(p.y - b);\n" + 
        "    }\n" + 
        
             // Find right triangle intersection points
        "    vec2 u = vec2(p.x, (m*p.x + b));\n" +   // (y adj leg) ( y = mx + b )
        "    vec2 v = vec2(((p.y - b)/m), p.y);\n" +  // (x adj leg) ( x = (y-b)/m )
        
             //Find length of sides of right triangle
        "    float y = distance(p,u);\n" + 
        "    float x = distance(p,v);\n" + 
        "    float h = distance(u,v);\n" + 
        
             // Calculate shortest distance from p to line (height of triangle)
        "    float d = x*y / h;\n" + 
        "    if (h < 0.00001) return 0;\n" +
        
        "    return d;";
        
        SLFunc<SLFloat> func = sl.slFunc(body, SLFloat.class, arg("vec2","p"), arg("float","m"), arg("float","b"));
        
        return func;
    }
    
    // Distance from line
    // Find closest distance of point p to line y=mx + c
    // See: distance from line method
    public static SLFunc<SLFloat> dLine(ShaderLanguage sl) {
        
        SLFunc<SLFloat> func = sl.slBegin(SLFloat.class, "vec2", "float", "float"); {
            
            SLVec2 p = sl.vec2(sl.arg(0)); // point
            SLFloat m = sl.slFloat(sl.arg(1)); // slope
            SLFloat b = sl.slFloat(sl.arg(2)); // y-intercept
            
            // If slope is 0, get y distance to horizontal line
            SLBool slopeEqualsZero = sl.equals(m, sl.slFloat(0f));
            sl.slIf(slopeEqualsZero); {
                func.ret( sl.abs( sl.sub(p.y(),b) ) );
            }
            sl.slEndIf();
            
            // Find right triangle intersection points
            SLVec2 u = sl.vec2(p.x(),  sl.add(sl.mul(m, p.x()), b));
            SLVec2 v = sl.vec2(sl.div(sl.sub(p.y(), b), m),  p.y());
            
            // Find length of sides of right triangle 
            SLFloat y = sl.distance(p,u);
            SLFloat x = sl.distance(p,v);
            SLFloat h = sl.distance(u,v);
            
            // If h equals 0, return 0
            SLBool hEqualsZero = sl.equals(h, sl.slFloat(0f));
            sl.slIf(hEqualsZero); {
                func.ret( sl.slFloat(0.0f) );
            }
            sl.slEndIf();
            
            // Calculate shortest distance from p to line (height of triangle)
            SLFloat d = sl.div( sl.mul(x, y) , h );
            
            //sl.slReturn(d);
            func.ret(d);
            
        }
        sl.slEnd();
        
        return func;
    }
    
    
    // Distance from line segment
    public static SLFunc<SLFloat> dSegment(ShaderLanguage sl) {
        
        SLFunc<SLFloat> func = sl.slBegin(SLFloat.class, "vec2", "vec2", "vec2"); {
            
            SLVec2 p = sl.vec2(sl.arg(0)); // point
            SLVec2 A = sl.vec2(sl.arg(1)); // slope
            SLVec2 B = sl.vec2(sl.arg(2)); // y-intercept
            
            // If slope is inf, get x distance to vertical line
            //SLBool slopeEqualsInf = sl.equals(A.x(), B.x());
            SLBool slopeEqualsInf = sl.less(sl.abs(sl.sub(A.x(), B.x())), sl.slFloat(0.005f));
            sl.slIf(slopeEqualsInf); {
                
                SLBool aG = sl.greater(p.y(), A.y());
                SLBool bG = sl.greater(p.y(), B.y());
                SLBool within = sl.xor(aG, bG); //y>ay xor y>by
                
                sl.slIf(within); {
                    func.ret( sl.abs( sl.sub(p.x(), A.x()) ) );
                }
                sl.slEndIf();
                
            }
            sl.slEndIf();
            
            // Find line equation given A and B
            SLFloat m = sl.div( sl.sub(B.y(), A.y()), sl.sub(B.x(), A.x()) );
            SLFloat b = sl.sub(A.y(), sl.mul(m, A.x()));
            
            // If slope is 0, get y distance to horizontal line
            //SLBool slopeEqualsZero = sl.equals(m, sl.slFloat(0f));
            SLBool slopeEqualsZero = sl.less(sl.abs(m), sl.slFloat(0.005f));
            sl.slIf(slopeEqualsZero); {
                
                SLBool aG = sl.greater(p.x(), A.x());
                SLBool bG = sl.greater(p.x(), B.x());
                SLBool within = sl.xor(aG, bG); //y>ay xor y>by
                
                sl.slIf(within); {
                    func.ret( sl.abs( sl.sub(p.y(),b) ) );
                }
                sl.slEndIf();
            }
            sl.slEndIf();
            
            // Find tangent line passing through p
            SLFloat _m = sl.div(-1.0f, m);
            SLFloat _b = sl.sub(p.y(), sl.mul(_m, p.x()));
            
            // Find where these lines intersect (mx+b = _mx+_b)
            SLFloat x = sl.div( sl.sub(b, _b) , sl.sub(_m, m) );
            SLFloat y = sl.add(sl.mul(m, x), b);
            SLVec2 i = sl.vec2(x,y);
            
            // Check if intersection point lies on segment
            // We can check if the y values are within range of end points since
            // we already checked for horizontal line case
            SLBool aG = sl.greater(i.y(), A.y());
            SLBool bG = sl.greater(i.y(), B.y());
            SLBool within = sl.xor(aG, bG); //y>ay xor y>by
            
            SLFloat d = sl.slFloat("1.0 / 0.0"); // glsl infinity
            sl.slIf(within); {
                sl.set(d, sl.distance(p, i));
            }
            sl.slEndIf();
            
            func.ret(d);
            
        }
        sl.slEnd();
        
        return func;
    }
    
    
    // Returns if a is within epsilon of b
    // (a,b,epsilon)
    public static SLFunc<SLBool> e(ShaderLanguage sl) {
        SLFunc<SLBool> func = sl.slBegin(SLBool.class, "float", "float", "float"); {
            sl.putStatement(String.format("return abs(%s-%s) < %s", sl.arg(0), sl.arg(1), sl.arg(2)));
        }
        sl.slEnd();
        
        return func;
    }
    
    // Distance from circle edge
    // Find closest distance from p to circle centered at c with radius r
    // (p,c,r)
    public static SLFunc<SLFloat> dCircle(ShaderLanguage sl) {
        SLFunc<SLFloat> func = sl.slBegin(SLFloat.class, "vec2", "vec2", "float"); {
            sl.putStatement(String.format("return abs(distance(%s,%s) - %s)", sl.arg(0), sl.arg(1), sl.arg(2)));
        }
        sl.slEnd();
        return func;
    }
    
    // Distance from circle filled
    // Find closest distance from p to circle centered at c with radius r
    // (p,c,r)
    public static SLFunc<SLFloat> fdCircle(ShaderLanguage sl) {
        SLFunc<SLFloat> func = sl.slBegin(SLFloat.class, "vec2", "vec2", "float"); {
            sl.putStatement(String.format("return max(0.0, distance(%s,%s) - %s)", sl.arg(0), sl.arg(1), sl.arg(2)));
        }
        sl.slEnd();
        return func;
    }
    
    // Operation union
    // (d1, d2)
    public static SLFunc<SLFloat> opU(ShaderLanguage sl) {
        SLFunc<SLFloat> func = sl.slBegin(SLFloat.class, "float", "float"); {
            sl.putStatement(String.format("return (%s < %s) ? %s : %s", sl.arg(0), sl.arg(1), sl.arg(0), sl.arg(1)));
        }
        sl.slEnd();
        return func;
    }

    
    /**
     * Returns floor(gl_FragCoord.xy)
     * @return
     */
    @SuppressWarnings("unchecked")
    public static SLFunc<SLVec2> getPixelLoc(ShaderLanguage sl) {
        
        String body = 
       "    return floor(gl_FragCoord.xy);\n";
       
       SLFunc<SLVec2> func = sl.slFunc(body, SLVec2.class);
       
       return func;
           
    }
    
    /**
     * @return unit vector(0,1) to normal vector (-1,1)
     */
    public static SLVec4 unitToNorm(ShaderLanguage sl, SLVec4 vec) {
        return sl.vec4(vec+"*2 - 1");
    }
    
    /**
     * @return normal vector (-1,1) to unit vector(0,1)
     */
    public static SLVec4 normToUnit(ShaderLanguage sl, SLVec4 vec) {
        return sl.vec4("("+vec+"+1) / 2");
    }
    
    public static SLVec2 unitToNorm(ShaderLanguage sl, SLVec2 vec) {
        return sl.vec2(vec+"*2 - 1");
    }
    
    public static SLVec2 normToUnit(ShaderLanguage sl, SLVec2 vec) {
        return sl.vec2("("+vec+"+1) / 2");
    }

    public static SLVec2 flip(ShaderLanguage sl, SLVec2 vec) {
        return sl.vec2(vec.x(), sl.sub(1,vec.y()));
    }
    
    
}
