package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.MenuBar;
import jonl.aui.MenuButton;
import jonl.jutils.func.List;

public class TMenuBar extends TButtonBar implements MenuBar {
    
    public TMenuBar() {
        super();
    }
    
    @Override
    public TMenuButton get(int index) {
        return (TMenuButton) super.get(index);
    }

    @Override
    public void add(MenuButton button) {
        super.add(button);
    }

    @Override
    public void remove(MenuButton button) {
        super.remove(button);
    }

    @Override
    public ArrayList<MenuButton> buttons() {
        return List.map(widgetLayout().widgets(), (w) -> (MenuButton)w);
    }
    
}
