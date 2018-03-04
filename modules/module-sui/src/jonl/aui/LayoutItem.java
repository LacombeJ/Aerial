package jonl.aui;

public interface LayoutItem {

    Layout layout();
    
    int minWidth();
    int minHeight();
    
    int maxWidth();
    int maxHeight();
    
    int preferredWidth();
    int preferredHeight();
    
}
