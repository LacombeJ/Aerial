package jonl.aui;

public interface LayoutItem {

    Layout layout();
    
    int preferredWidth();
    void setPreferredWidth(int width);
    
    int preferredHeight();
    void setPreferredHeight(int height);
    
    int minimumWidth();
    void setMinimumWidth();
    
    int minimumHeight();
    void setMinimumHeight();
    
    int maximumWidth();
    void setmaximumWidth();
    
    int maximumHeight();
    void setmaximumHeight();
    
}
