package ax.commons.jss;

import java.util.ArrayList;
import java.util.HashMap;

class StyleSheetParser {
    
    static final char[] KEY_CHAR = {
        '{',
        ':',
        '}',
        ','
    };
    
    static final char[] ROOT_IGNORES = {
        ' ',
        '\t',
        '\n',
        '\r'
    };
    
    StyleSheet ss;
    String collection = "";
    char[] contents;
    int index;
    String property = "";
    String value = "";
    
    String atProperty = "";
    String atValue = "";
    
    ArrayList<String> styleTitles = new ArrayList<>();
    ArrayList<Style> styles = new ArrayList<>();
    boolean root = false;
    boolean define = false;
    
    HashMap<String,String> definitions = new HashMap<>();
    
    StyleSheetParser(String jss) {

        contents = jss.toCharArray();
        index = 0;
        
        ss = new StyleSheet("");
        
        root();
        
    }
    
    StyleSheet styleSheet() {
        return ss;
    }
    
    // ------------------------------------------------------------------------
    
    boolean hasNext() {
        return index < contents.length;
    }
    
    char next() {
        return contents[index++];
    }
    
    char current() {
        return contents[index];
    }
    
    char back() {
        index--;
        return contents[index];
    }
    
    void error(String string) {
        throw new IllegalStateException("Error parsing stylesheet: "+string);
    }
    
    boolean isMember(char c, char[] set) {
        for (char s : set) {
            if (c==s) return true;
        }
        return false;
    }
    
    // ------------------------------------------------------------------------
    
    void root() {
        while (hasNext()) {
            char c = next();
            if (isMember(c,ROOT_IGNORES)) {
                continue;
            }
            if (isMember(c,KEY_CHAR)) {
                error("Syntax Error.");
            }
            if (isSingleLineComment()) {
                continue;
            }
            if (isMultiLineComment()) {
                continue;
            }
            back();
            styles();
        }
    }
    
    boolean isMultiLineComment() {
        back();
        while (hasNext()) {
            char c = next();
            if (c=='/') {
                char a = next();
                if (a=='*') {
                    while (hasNext()) {
                        char o = next();
                        if (o=='*') {
                            char e = next();
                            if (e=='/') {
                                return true;
                            } else {
                                back();
                            }
                        }
                    }
                    return true;
                } else {
                    back();
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
    
    boolean isSingleLineComment() {
        back();
        while (hasNext()) {
            char c = next();
            if (c=='/') {
                char a = next();
                if (a=='/') {
                    while (hasNext()) {
                        char o = next();
                        if (o=='\n') {
                            return true;
                        }
                    }
                    return true;
                } else {
                    back();
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }
    
    void styles() {
        while (hasNext()) {
            char c = next();
            if (c==':') {
                styleTitles.add(collection);
                collection="";
                continue;
            }
            if (c=='{' || c==',') {
                if (collection.equals(".root") && styleTitles.isEmpty()) {
                    root = true;
                } else if (collection.equals(".define") && styleTitles.isEmpty()) {
                    define = true;
                } else {
                    styleTitles.add(collection);
                }
                collection="";
                
                if (styleTitles.size()>0) {
                    Style selector = ss.getOrCreateStyle(styleTitles.get(0));
                    Style style = selector;
                    for (int i=1; i<styleTitles.size(); i++) {
                        Style sub = style.getOrCreateStyle(styleTitles.get(i));
                        style = sub;
                    }
                    styles.add(style);
                    styleTitles.clear();
                }
            }
            if (c=='{') {
                values();
                root = false;
                styles.clear();
                return;
            }
            if (c==',') {
                continue;
            }
            if (isMember(c,ROOT_IGNORES)) {
                continue;
            }
            collection += c;
        }
    }
    
    void values() {
        while (hasNext()) {
            char c = next();
            if (isMember(c,ROOT_IGNORES)) {
                continue;
            }
            if (c=='}') {
                return;
            }
            if (isSingleLineComment()) {
                continue;
            }
            if (isMultiLineComment()) {
                continue;
            }
            back();
            line();
        }
    }
    
    void line() {
        while (hasNext()) {
            char c = next();
            if (isMember(c,ROOT_IGNORES)) {
                continue;
            }
            if (c=='}') {
                back();
                return;
            }
            if (c=='@') {
                atProperty();
                atValue();
                continue;
            }
            back();
            property();
            value();
        }
    }
    
    void atProperty() {
        atProperty = "";
        while (hasNext()) {
            char c = next();
            if (c==' ') {
                return;
            }
            atProperty += c;
        }
    }
    
    void atValue() {
        atValue = "";
        boolean found = false;
        while (hasNext()) {
            char c = next();
            if (!found && isMember(c,ROOT_IGNORES)) {
                continue;
            }
            if (c==';') {
                if (atProperty.equals("extend")) {
                    for (Style style : styles) {
                        Style extend = style(ss,atValue);
                        if (extend!=null) {
                            style.append(extend);
                        }
                    }
                }
                return;
            }
            found = true;
            atValue += c;
        }
    }
    
    void property() {
        property = "";
        while (hasNext()) {
            char c = next();
            if (c==':') {
                return;
            }
            property += c;
        }
    }
    
    void value() {
        value = "";
        boolean found = false;
        while (hasNext()) {
            char c = next();
            if (!found && isMember(c,ROOT_IGNORES)) {
                continue;
            }
            if (c==';') {
                for (Style style : styles) {
                    put(style, property, value);
                }
                if (root) {
                    put(ss, property, value);
                }
                if (define) {
                    definitions.put(property,value);
                }
                return;
            }
            found = true;
            value += c;
        }
    }
    
    void put(Style style, String property, String value) {
        String definition = definitions.get(value);
        if (definition!=null) {
            style.put(property, definition);
        } else {
            style.put(property, value);
        }
    }
    
    Style style(Style style, String name) {
        String[] styles = name.split(":");
        Style s = style.style(styles[0]);
        for (int i=1; i<styles.length; i++) {
            if (s==null) {
                throw new Error("Null style: "+""+"from "+name);
            }
            s = s.style(styles[i]);
        }
        return s;
    }
    
}
