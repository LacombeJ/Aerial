package jonl.ge.editor;

import jonl.aui.Layout;
import jonl.aui.TabPanel;
import jonl.aui.UIManager;
import jonl.aui.Widget;
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
        tabPanel.closed().connect((w)->{
            if (tabPanel.count()==0) {
                String data = (String)w.data();
                if (data == null || !data.equals("new_tab")) {
                    addNewTab();
                }
            }
        });
        
        layout.add(tabPanel);
        
    }
    
    void clear() {
        tabPanel.removeAll();
    }
    
    void addNewTab() {
        NewTabWidget newTab = new NewTabWidget(editor, ui);
        newTab.setData("new_tab");
        newTab.selected.connect((w,l)->{
            addEditorTab(w,l);
        });
        tabPanel.add(newTab, "New Tab");
        tabPanel.setWidget(newTab);
    }
    
    void addEditorTab(Widget widget, String label) {
        tabPanel.add(widget,label);
        Widget prevWidget = tabPanel.get(tabPanel.index());
        tabPanel.setWidget(widget);
        String data = (String)prevWidget.data();
        if (data != null && data.equals("new_tab")) {
            tabPanel.remove(prevWidget);
        }
    }
    
    void populate() {
        addNewTab();
    }
    
}
