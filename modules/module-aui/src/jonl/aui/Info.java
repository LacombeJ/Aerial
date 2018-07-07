package jonl.aui;

import jonl.jutils.structs.AttributeMap;
import jonl.vmath.Color;

public class Info extends AttributeMap {

    public Color getColor(String key) {
        return (Color) get(key);
    }
    
}
