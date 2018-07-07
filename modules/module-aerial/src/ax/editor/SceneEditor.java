package ax.editor;

import ax.aui.UIManager;
import ax.aui.Widget;
import ax.aui.Window;

public class SceneEditor implements SubEditor {

    UIManager ui;
    
    String name;
    Widget widget;
    
    public SceneEditor(UIManager ui, Window window, Object state) {
        this.ui = ui;
        this.name = "Scene";
        
        widget = ui.dial();
    }
    
    @Override
    public String name() {
        return name;
    }

    @Override
    public Widget widget() {
        return widget;
    }
    
}
