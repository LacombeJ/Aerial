package jonl.aui;

public interface Button extends Widget {
    
    String getText();
    
    void setText(String text);
    
    void addAction(Action action);
    
}
