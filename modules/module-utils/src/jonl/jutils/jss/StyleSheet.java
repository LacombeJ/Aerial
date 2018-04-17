package jonl.jutils.jss;

import java.util.HashMap;

import jonl.jutils.io.FileUtils;

public class StyleSheet {

    HashMap<String,Style> selectors;
    
    public static StyleSheet fromFile(String file) {
        String jss = FileUtils.readFromFile(file);
        return fromString(jss);
    }
    
    public static StyleSheet fromString(String jss) {
        StyleSheetParser parser = new StyleSheetParser(jss);
        return parser.styleSheet();
    }
    
    StyleSheet() {
        selectors = new HashMap<>();
        
    }
    
    Style getOrCreateStyle(String name) {
        Style style = selectors.get(name);
        if (style==null) {
            style = new Style(name);
            selectors.put(name,style);
        }
        return style;
    }
    
    public Style selector(String id) {
        return selectors.get(id);
    }
    
}
