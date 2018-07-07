package ax.aui.tea;

import ax.commons.structs.AttributeMap;

public class TResourceMap extends AttributeMap {
    
    public void putResource(String key, Object data) {
        put(key, new TResource(key,data));
    }
    
    public TResource getResource(String key) {
        return getAsType(key, TResource.class);
    }
    
}
