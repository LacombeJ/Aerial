package jonl.ge.editor;

import jonl.aui.Align;
import jonl.aui.Button;
import jonl.aui.ComboBox;
import jonl.aui.ListLayout;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class EditorStartup extends TPanel {

    private UIManager ui;
    
    Button newProject;
    Button openProject;
    
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
        
        newProject = button("New Project");
        openProject = button("Open Project");
        
        ComboBox combo = ui.comboBox();
        combo.add("Alpha");
        combo.add("Beta");
        combo.add("Gamma");
        
        widgetLayout().add(newProject);
        widgetLayout().add(openProject);
        widgetLayout().add(combo);
        
    }
    
    Button button(String text) {
        Button button = ui.button(text);
        button.setMinSize(0,200);
        button.setMaxSize(200,200);
        return button;
    }
    
}
