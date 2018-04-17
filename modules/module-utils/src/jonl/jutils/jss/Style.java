package jonl.jutils.jss;

import java.util.HashMap;

public class Style {

    Style parent = null;
    
    String name;
    
    HashMap<String,Style> children = new HashMap<>();
    
    HashMap<String,String> values = new HashMap<>();
    
    Style(String name) {
        this.name = name;
    }
    
    Style getOrCreateStyle(String name) {
        Style style = children.get(name);
        if (style==null) {
            style = new Style(name);
            style.parent = this;
            children.put(name,style);
        }
        return style;
    }
    
    void put(String property, String value) {
        values.put(property, value);
    }
    
    public Style parent() {
        return parent;
    }
    
    public Style style(String id) {
        return children.get(id);
    }
    
    public String value(String property) {
        String value = values.get(property);
        if (value==null && parent!=null) {
            return parent.value(property);
        }
        return value;
    }
    
    public String override(String property) {
        return null;
    }
    
}
