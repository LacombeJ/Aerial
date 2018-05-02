package jonl.ge.editor;

import jonl.aui.Align;
import jonl.aui.Button;
import jonl.aui.ListLayout;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class EditorStartup extends TPanel {

    private UIManager ui;
    
    public EditorStartup(UIManager ui) {
        this.ui = ui;
        
        initLayout();
        initWidgets();
    }
    
    void initLayout() {
        ListLayout layout = ui.listLayout(Align.VERTICAL);
        setLayout(layout);        
    }
    
    void initWidgets() {
        
        Button button = ui.button("Test button");
        widgetLayout().add(button);
        
    }
    
}
