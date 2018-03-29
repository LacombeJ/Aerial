package jonl.aui.tea.graphics;

import jonl.jutils.structs.AttributeMap;

public class TWidgetInfo extends AttributeMap {

    public TColor getColor(String key) {
        return (TColor) get(key);
    }
    
}
