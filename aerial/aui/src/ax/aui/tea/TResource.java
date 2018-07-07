package ax.aui.tea;

import ax.aui.Resource;

public class TResource implements Resource {
    
    String key;
    Object data;
    
    public TResource(String key, Object data) {
        this.key = key;
        this.data = data;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public Object data() {
        return data;
    }
    
    public TIcon icon() {
        return (TIcon) data;
    }
    
}
