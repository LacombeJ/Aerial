package jonl.ge.editor;

import jonl.aui.Layout;
import jonl.aui.TabPanel;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class EditorProjectUI extends TPanel {

    Editor editor;
    UIManager ui;
    
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
        tabPanel.setAddable(true);
        tabPanel.setCloseable(true);
        tabPanel.newTab().connect(()->{
            addNewTab();
        });
        
        layout.add(tabPanel);
        
    }
    
    void clear() {
        tabPanel.removeAll();
    }
    
    void addNewTab() {
        NewTabWidget newTab = new NewTabWidget(ui);
        
        tabPanel.add(newTab, "New Tab");
    }
    
    void populate() {
        addNewTab();
    }
    
}
