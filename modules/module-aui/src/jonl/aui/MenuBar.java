package jonl.aui;

import java.util.ArrayList;

public interface MenuBar extends Widget {

    MenuButton get(int index);
    
    void add(MenuButton button);
    
    void remove(MenuButton button);
    
    void removeAll();
    
    ArrayList<MenuButton> buttons();
    
}
