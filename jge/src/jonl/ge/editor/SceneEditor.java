package jonl.ge.editor;

import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.Window;

public class SceneEditor implements SubEditor {

    UIManager ui;
    
    String name;
    Widget widget;
    
    public SceneEditor(UIManager ui, Window window, String content) {
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
