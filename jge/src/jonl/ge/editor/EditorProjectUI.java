package jonl.ge.editor;

import jonl.aui.Layout;
import jonl.aui.TabPanel;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class EditorProjectUI extends TPanel {

    private UIManager ui;
    
    public EditorProjectUI(UIManager ui) {
        this.ui = ui;
        
        init();
    }
    
    void init() {
        
        Layout layout = ui.listLayout();
        layout.setMargin(0,0,0,0);
        setLayout(layout);
        
        TabPanel tabPanel = ui.tabPanel();
        
        layout.add(tabPanel);
        
    }
    
}
