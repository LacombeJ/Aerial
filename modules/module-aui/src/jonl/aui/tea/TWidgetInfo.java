package jonl.aui.tea;

import jonl.jutils.structs.AttributeMap;
import jonl.vmath.Color;

public class TWidgetInfo extends AttributeMap {

    public Color getColor(String key) {
        return (Color) get(key);
    }
    
}
