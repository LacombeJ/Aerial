package jonl.aui;

public interface Frame extends Window {

    Margin insets();
    
    void setInsets(int left, int right, int top, int bottom);
    
    void setInsets(Margin margin);
    
}
