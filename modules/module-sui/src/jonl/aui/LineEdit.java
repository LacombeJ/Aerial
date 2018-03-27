package jonl.aui;

import jonl.jutils.func.Callback;

public interface LineEdit extends Widget {
    
    String text();
    
    void setText(String text);
    
    Signal<Callback<String>> changed();
    
}
