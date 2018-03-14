package jonl.aui;

import java.util.ArrayList;

public interface ToolBar extends Widget {

    ToolButton get(int index);
    
    void add(ToolButton button);
    
    void remove(ToolButton button);
    
    void removeAll();
    
    ArrayList<ToolButton> buttons();
    
}
