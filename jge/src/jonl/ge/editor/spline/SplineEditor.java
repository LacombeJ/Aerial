package jonl.ge.editor.spline;

import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.ge.editor.SubEditor;

public class SplineEditor implements SubEditor {

    UIManager ui;
    
    String name;
    Widget widget;
    
    public SplineEditor(UIManager ui, String content) {
        this.ui = ui;
        this.name = "Spline";
        
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
