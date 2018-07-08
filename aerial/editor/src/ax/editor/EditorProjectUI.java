package ax.editor;

import ax.aui.Align;
import ax.aui.Layout;
import ax.aui.SplitPanel;
import ax.aui.TabPanel;
import ax.aui.Tree;
import ax.aui.UIManager;
import ax.aui.Widget;
import ax.tea.TPanel;
import ax.commons.data.Json;
import ax.editor.EditorProject.OpenTool;

public class EditorProjectUI extends TPanel {

    Editor editor;
    UIManager ui;
    
    ProjectExplorer explorer;
    
    SplitPanel splitPanel;
        TabPanel leftPanel;
            Tree tree;
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
        
        
        
        explorer = new ProjectExplorer(editor,ui);
        tree = explorer.tree();
        
        leftPanel = ui.tabPanel();
        leftPanel.add(tree,"Project Explorer");
        
        
        tabPanel = ui.tabPanel();
        tabPanel.setAddable(true);
        tabPanel.setCloseable(true);
        tabPanel.newTab().connect(()->{
            addNewTab();
        });
        tabPanel.closed().connect((w)->{
            boolean remove = true;
            if (w.info().containsKey("_tab_")) {
                Tab tab = w.info().getAsType("_tab_",Tab.class);
                StoreTrait storeTrait = editor.pivot.storeTraits.get(tab.tool);
                if (storeTrait!=null) {
                    if (!storeTrait.onClose()) {
                        remove = false;
                    } else {
                        if (remove) {
                            editor.project.removeTool(tab.tool,tab.editor);
                        }
                    }
                }
                return remove;
            }
            
            return true;
        });
        tabPanel.removed().connect((w)->{
            if (tabPanel.count()==0) {
                String data = (String)w.data();
                if (data == null || !data.equals("new_tab")) {
                    addNewTab();
                }
            }
        });
        
        splitPanel = ui.splitPanel(leftPanel,tabPanel,Align.HORIZONTAL,0.2);
        
        layout.add(splitPanel);
        
    }
    
    void clear() {
        tabPanel.removeAll();
    }
    
    void addNewTab() {
        ToolMenuWidget newTab = new ToolMenuWidget(editor, ui);
        newTab.setData("new_tab");
        newTab.selected.connect((tab)->{
            addEditorTab(tab);
        });
        tabPanel.add(newTab, "New Tab");
        tabPanel.setWidget(newTab);
    }
    
    void addEditorTab(Tab tab) {
        tab.widget.info().put("_tab_",tab);
        tabPanel.add(tab.widget,tab.name);
        Widget prevWidget = tabPanel.get(tabPanel.index());
        tabPanel.setWidget(tab.widget);
        String data = (String)prevWidget.data();
        if (data != null && data.equals("new_tab")) {
            tabPanel.remove(prevWidget);
        }
    }
    
    void populate() {
        explorer.populate();
        if (editor.project.store.openTools.size()==0) {
            addNewTab();
        } else {
            for (OpenTool openTool : editor.project.store.openTools) {
                SubEditorTool tool = editor.tool(openTool.id);
                if (tool!=null) {
                    StoreTrait storeTrait = editor.pivot.storeTraits.get(tool);
                    if (storeTrait != null) {
                        Object store = new Json(openTool.storePath).load(storeTrait.getStoreDefinition());
                        SubEditor se = tool.open(store);
                        editor.pivot.addStorePath(tool,se,openTool.storePath);
                        editor.pivot.loadEditor(tool,se);
                        Tab tab = new Tab(se.widget(),se.name(),tool,se);
                        addEditorTab(tab);
                    }
                }
            }
        }
    }
    
    static class Tab {
        Widget widget;
        String name;
        SubEditorTool tool;
        SubEditor editor;
        Tab(Widget widget, String name, SubEditorTool tool, SubEditor editor) {
            this.widget = widget;
            this.name = name;
            this.tool = tool;
            this.editor = editor;
        }
    }
    
}
