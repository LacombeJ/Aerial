package jonl.aui.tea.graphics;

public class TShader {

    // Copied from Presets
    
    private static void version(CustomStringBuilder sb, int version) {
        sb.append("#version "+version);
        if (version<330) {
            sb.append("#extension GL_ARB_explicit_attrib_location : enable");
        }
    }
    
    public static String textureVSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("layout (location = 0) in vec4 vertex;");
        sb.append("layout (location = 1) in vec3 normal;");
        sb.append("layout (location = 2) in vec2 texCoord;");
        sb.append("out vec2 vTexCoord;");
        sb.append("uniform mat4 MVP;");
        sb.append("void main() {");
        sb.append("    gl_Position = MVP * vertex;");
        sb.append("    vTexCoord = texCoord;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String textureFSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("in vec2 vTexCoord;");
        sb.append("uniform sampler2D texture;");
        sb.append("uniform vec4 color;");
        sb.append("void main() {");
        sb.append("    gl_FragColor = texture2D(texture,vTexCoord) * color;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String fontVSSource(int version) {
        return textureVSSource(version);
    }
    
    public static String fontFSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("in vec2 vTexCoord;");
        sb.append("uniform sampler2D texture;");
        sb.append("uniform vec4 fontColor;");
        sb.append("void main() {");
        sb.append("    vec4 texColor = texture2D(texture,vTexCoord);");
        sb.append("    gl_FragColor = vec4(1,1,1,texColor.w) * fontColor;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String solidVSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("layout (location = 0) in vec4 vertex;");
        sb.append("uniform mat4 MVP;");
        sb.append("void main() {");
        sb.append("    gl_Position = MVP * vertex;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String solidFSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("uniform vec4 color;");
        sb.append("void main() {");
        sb.append("    gl_FragColor = color;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String gradientVSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("layout (location = 0) in vec4 vertex;");
        sb.append("layout (location = 1) in vec3 normal;");
        sb.append("layout (location = 2) in vec2 texCoord;");
        sb.append("out vec2 vTexCoord;");
        sb.append("uniform mat4 MVP;");
        sb.append("void main() {");
        sb.append("    gl_Position = MVP * vertex;");
        sb.append("    vTexCoord = texCoord;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String gradientFSSource(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("in vec2 vTexCoord;");
        sb.append("uniform vec4 color00;");
        sb.append("uniform vec4 color01;");
        sb.append("uniform vec4 color11;");
        sb.append("uniform vec4 color10;");
        sb.append("void main() {");
        sb.append("    vec4 sum = vec4(0,0,0,0);");
        sb.append("    float x = vTexCoord.x;");
        sb.append("    float y = vTexCoord.y;");
        // Switching orientation of y because shader orientation is upside down
        sb.append("    sum += color00 * (1-x) * (y  );");
        sb.append("    sum += color01 * (1-x) * (1-y);");
        sb.append("    sum += color11 * (x  ) * (1-y);");
        sb.append("    sum += color10 * (x  ) * (y  );");
        sb.append("    gl_FragColor = sum;");
        sb.append("}");
        return sb.toString();
    }
    
    
    public static String boxVertex(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        version(sb,version);
        sb.append("layout (location = 0) in vec4 vertex;");
        sb.append("layout (location = 1) in vec3 normal;");
        sb.append("layout (location = 2) in vec2 texCoord;");
        sb.append("out vec2 box;");
        sb.append("out vec2 vTexCoord;");
        sb.append("uniform mat4 MVP;");
        sb.append("uniform mat4 B;");
        sb.append("void main() {");
        sb.append("    gl_Position = MVP * vertex;");
        sb.append("    box = vec2(B * vertex);");
        sb.append("    vTexCoord = texCoord;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String boxFragment(int version) {
        CustomStringBuilder sb = new CustomStringBuilder();
        
        version(sb,version);
        
        sb.append("in vec2 box;");
        
        sb.append("uniform float width;");
        sb.append("uniform float height;");
        sb.append("uniform float border;");
        sb.append("uniform float radius;");
        
        sb.append("uniform vec4 c00;");
        sb.append("uniform vec4 c01;");
        sb.append("uniform vec4 c11;");
        sb.append("uniform vec4 c10;");
        
        sb.append("uniform vec4 bc00;");
        sb.append("uniform vec4 bc01;");
        sb.append("uniform vec4 bc11;");
        sb.append("uniform vec4 bc10;");
        
        sb.append("void main() {");
        
        sb.append("    float r = min(radius,min(width/2,height/2));");
        
        sb.append("    float a = -1;"); //radius distance
        sb.append("    float b = -1;"); //border distance
        
        sb.append("    if (distance(box,vec2(0,0)) < r) {");
        sb.append("        a = distance(box,vec2(r,r));");
        sb.append("    }");
        sb.append("    if (distance(box,vec2(width,0)) < r) {");
        sb.append("        a = distance(box,vec2(width-r,r));");
        sb.append("    }");
        sb.append("    if (distance(box,vec2(width,height)) < r) {");
        sb.append("        a = distance(box,vec2(width-r,height-r));");
        sb.append("    }");
        sb.append("    if (distance(box,vec2(0,height)) < r) {");
        sb.append("        a = distance(box,vec2(r,height-r));");
        sb.append("    }");
        
        sb.append("    int withinBorder = 0;");
        sb.append("    if (box.x<border || box.x>(width-border) || box.y<border || box.y>(height-border)) {");
        sb.append("        withinBorder = 1;");
        sb.append("    }");
        sb.append("    else {");
        
        sb.append("        if (distance(box,vec2(0,0)) < r) {");
        sb.append("            b = r - distance(box,vec2(r,r));");
        sb.append("        }");
        sb.append("        if (distance(box,vec2(width,0)) < r) {");
        sb.append("            b = r - distance(box,vec2(width-r,r));");
        sb.append("        }");
        sb.append("        if (distance(box,vec2(width,height)) < r) {");
        sb.append("            b = r - distance(box,vec2(width-r,height-r));");
        sb.append("        }");
        sb.append("        if (distance(box,vec2(0,height)) < r) {");
        sb.append("            b = r - distance(box,vec2(r,height-r));");
        sb.append("        }");
        
        sb.append("    }");
        
        sb.append("    float x = box.x / width;");
        sb.append("    float y = box.y / height;");
        
        sb.append("    if ((a>r && border>0) || (b>0 && b<border)) {");
        sb.append("        withinBorder = 1;");
        sb.append("    };");
        
        sb.append("    float amr = max(0,a - r);");
        
        sb.append("    vec4 color = vec4(0);");
        sb.append("    vec4 borderColor = vec4(0);");
        
        // Switching orientation of y because shader orientation is upside down
        sb.append("    {");
        sb.append("        vec4 sum = vec4(0,0,0,0);");
        sb.append("        sum += bc00 * (x  ) * (y  );");
        sb.append("        sum += bc01 * (1-x) * (y  );");
        sb.append("        sum += bc11 * (x  ) * (1-y);");
        sb.append("        sum += bc10 * (1-x) * (1-y);");
        sb.append("        borderColor = sum;");
        sb.append("        if (a>r) {");
        sb.append("            borderColor.a = 1-sqrt(amr);");
        sb.append("        }");
        sb.append("    }");
        sb.append("    {");
        sb.append("        vec4 sum = vec4(0,0,0,0);");
        sb.append("        sum += c00 * (x  ) * (y  );");
        sb.append("        sum += c01 * (1-x) * (y  );");
        sb.append("        sum += c11 * (x  ) * (1-y);");
        sb.append("        sum += c10 * (1-x) * (1-y);");
        sb.append("        color = sum;");
        sb.append("        if (a>r) {");
        sb.append("            color.a = 1-sqrt(amr);");
        sb.append("        }");
        sb.append("    }");
        
        sb.append("    if (withinBorder==1) {");
        sb.append("        gl_FragColor = borderColor;");
        sb.append("    }");
        sb.append("    else {");
        sb.append("        gl_FragColor = color;");
        sb.append("    }");
        
        sb.append("}");
        
        return sb.toString();
    }
    
    // Added this class so I don't have to add /n for every line to debug
    static class CustomStringBuilder {
        StringBuilder sb = new StringBuilder();
        void append(String string) {
            sb.append(string);
            sb.append("\n");
        }
        @Override
        public String toString() {
            return sb.toString();
        }
    }
    
}
