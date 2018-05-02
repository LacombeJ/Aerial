package jonl.ge.editor;

import jonl.aui.Align;
import jonl.aui.Button;
import jonl.aui.Layout;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class NewTabWidget extends TPanel {

    private UIManager ui;
    
    public NewTabWidget(UIManager ui) {
        this.ui = ui;
        
        init();
    }
    
    void init() {
        
        Layout layout = ui.listLayout(Align.VERTICAL);
        setLayout(layout);
        
        Button newProject = button("New Project");
        Button openProject = button("Open Project");
        
        layout.add(newProject);
        layout.add(openProject);
        
    }
    
    Button button(String text) {
        Button button = ui.button(text);
        button.setMinSize(0,200);
        button.setMaxSize(200,200);
        return button;
    }
    
}
