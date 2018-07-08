package ax.aui;

public interface ToolBar extends Widget {

    ToolButton get(int index);
    
    void add(ToolButton button);
    
    void addSeparator();
    
    void remove(ToolButton button);
    
    void removeAll();
    
    int count();
    
}
