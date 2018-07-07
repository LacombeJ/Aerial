package jonl.jutils.jss;

import jonl.jutils.io.FileUtils;

public class StyleSheet extends Style {
    
    public static StyleSheet fromFile(String file) {
        String jss = FileUtils.readFromFile(file);
        return fromString(jss);
    }
    
    public static StyleSheet fromString(String jss) {
        StyleSheetParser parser = new StyleSheetParser(jss);
        return parser.styleSheet();
    }
    
    StyleSheet(String name) {
        super(name);
        
    }
    
}
