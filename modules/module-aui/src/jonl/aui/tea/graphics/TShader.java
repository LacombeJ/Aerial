package jonl.aui.tea.graphics;


public class TShader {

    // Copied from Presets
    
    private static void version(StringBuilder sb, int version) {
        sb.append("#version "+version+"\n");
        if (version<330) {
            sb.append("#extension GL_ARB_explicit_attrib_location : enable \n");
        }
    }
    
    public static String textureVSSource(int version) {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
        version(sb,version);
        sb.append("layout (location = 0) in vec4 vertex;");
        sb.append("uniform mat4 MVP;");
        sb.append("void main() {");
        sb.append("    gl_Position = MVP * vertex;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String solidFSSource(int version) {
        StringBuilder sb = new StringBuilder();
        version(sb,version);
        sb.append("uniform vec4 color;");
        sb.append("void main() {");
        sb.append("    gl_FragColor = color;");
        sb.append("}");
        return sb.toString();
    }
    
    public static String gradientVSSource(int version) {
        StringBuilder sb = new StringBuilder();
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
        StringBuilder sb = new StringBuilder();
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
    
}
