package jonl.jutils.io;

public class Pattern {

    com.esotericsoftware.wildcard.Pattern pattern;
    
    public Pattern(String pattern, boolean ignoreCase) {
        this.pattern = new com.esotericsoftware.wildcard.Pattern(pattern,ignoreCase);
    }
    
    public boolean matches(String fileName) {
        return pattern.matches(fileName);
    }
    
}
