package jonl.aui;

public interface Dial extends Widget {
    
    int getValue();
    
    void addValueChangedListener(IntChangedListener vc);
    
}
