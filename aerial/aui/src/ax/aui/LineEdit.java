package ax.aui;

import ax.commons.func.Callback;

public interface LineEdit extends Widget {
    
    String text();
    
    void setText(String text);
    
    Signal<Callback<String>> changed();
    
    Signal<Callback<String>> finished();
    
}
