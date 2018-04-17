package jonl.jutils.jss;

import java.util.ArrayList;

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
    
    ArrayList<String> styleTitles = new ArrayList<>();
    ArrayList<Style> styles = new ArrayList<>();
    
    StyleSheetParser(String jss) {

        contents = jss.toCharArray();
        index = 0;
        
        ss = new StyleSheet();
        
        root();
        
    }
    
    StyleSheet styleSheet() {
        return ss;
    }
    
    // ------------------------------------------------------------------------
    
    boolean hasNext() {
        return index < contents.length - 1;
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
            back();
            styles();
        }
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
                styleTitles.add(collection);
                collection="";
                
                Style selector = ss.getOrCreateStyle(styleTitles.get(0));
                Style style = selector;
                for (int i=1; i<styleTitles.size(); i++) {
                    Style sub = style.getOrCreateStyle(styleTitles.get(i));
                    style = sub;
                }
                styles.add(style);
                styleTitles.clear();
            }
            if (c=='{') {
                values();
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
        styles.clear();
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
            back();
            property();
            value();
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
                    style.put(property, value);
                }
                return;
            }
            found = true;
            value += c;
        }
    }
    
    
    
}
