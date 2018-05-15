package jonl.aerial;

import jonl.aui.Align;
import jonl.aui.ArrayLayout;
import jonl.aui.Label;
import jonl.aui.LineEdit;
import jonl.aui.Margin;
import jonl.aui.Panel;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.Window;

public class ConfigurationEditor implements SubEditor {

    UIManager ui;
    
    String name;
    Panel widget;
    
    public ConfigurationEditor(UIManager ui, Window window, Object state) {
        this.ui = ui;
        this.name = "Configuration";
        
        widget = ui.panel(ui.listLayout(Align.VERTICAL,new Margin(0,0,0,0),0));
        
        ArrayLayout form = ui.arrayLayout();
        Panel panel = ui.panel(form);
        
        Label locLabel = ui.label("Project path:");
        LineEdit locEdit = ui.lineEdit("");
        
        int row = 0;
        form.add(locLabel,row,0);
        form.add(locEdit,row++,1);
        
        form.add(ui.spacer(Align.VERTICAL),row++,0);
        
        widget.add(panel);
        
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
