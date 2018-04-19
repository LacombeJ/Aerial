package jonl.aui.tea;

import jonl.jutils.structs.AttributeMap;

public class TResourceMap extends AttributeMap {
    
    public void putResource(String key, Object data) {
        put(key, new TResource(key,data));
    }
    
    public TResource getResource(String key) {
        return getAsType(key, TResource.class);
    }
    
}
