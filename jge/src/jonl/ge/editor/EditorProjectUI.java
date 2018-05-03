package jonl.ge.editor;

import jonl.aui.Layout;
import jonl.aui.TabPanel;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class EditorProjectUI extends TPanel {

    private Editor editor;
    private UIManager ui;
    
    TabPanel tabPanel;
    
    public EditorProjectUI(Editor editor, UIManager ui) {
        this.editor = editor;
        this.ui = ui;
        
        init();
    }
    
    void init() {
        
        Layout layout = ui.listLayout();
        layout.setMargin(0,0,0,0);
        setLayout(layout);
        
        tabPanel = ui.tabPanel();
        
        layout.add(tabPanel);
        
    }
    
    void clear() {
        tabPanel.removeAll();
    }
    
    void populate() {
        
        //EditorProject project = editor.project;
        
        NewTabWidget newTab = new NewTabWidget(ui);
        
        //tabPanel.add(newTab, "New Tab");
        
        tabPanel.add(ui.panel(),"Test??");
        
    }
    
}
