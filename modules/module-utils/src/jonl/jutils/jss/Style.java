package jonl.jutils.jss;

import java.util.HashMap;
import java.util.Map.Entry;

public class Style {

    Style parent = null;
    
    String name;
    
    HashMap<String,Style> children = new HashMap<>();
    
    HashMap<String,String> values = new HashMap<>();
    
    public Style(String name) {
        this.name = name;
    }
    
    Style getOrCreateStyle(String name) {
        Style style = children.get(name);
        if (style==null) {
            return addDirect(new Style(name));
            
        }
        return style;
    }
    
    Style addDirect(Style style) {
        style.parent = this;
        children.put(style.name,style);
        return style;
    }
    
    public void add(Style style) {
        if (children.containsKey(style.name)) {
            children.get(style.name).append(style.copy());
        } else {
            addDirect(style.copy());
        }
    }
    
    public void append(Style style) {
        for (Entry<String,Style> e : style.children.entrySet()) {
            if (children.containsKey(e.getKey())) {
                children.get(e.getKey()).add(e.getValue().copy());
            } else {
                addDirect(e.getValue().copy());
            }
        }
        for (Entry<String,String> e : style.values.entrySet()) {
            put(e.getKey(),e.getValue());
        }
    }
    
    public void put(String property, String value) {
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
        return values.get(property);
    }
    
    public Style copy() {
        Style style = new Style(name);
        for (Entry<String,Style> e : children.entrySet()) {
            style.addDirect(e.getValue().copy());
        }
        for (Entry<String,String> e : values.entrySet()) {
            style.put(e.getKey(),e.getValue());
        }
        return style;
    }
    
}
