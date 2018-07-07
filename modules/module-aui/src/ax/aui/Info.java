package ax.aui;

import ax.commons.structs.AttributeMap;
import ax.math.vector.Color;

public class Info extends AttributeMap {

    public Color getColor(String key) {
        return (Color) get(key);
    }
    
}
