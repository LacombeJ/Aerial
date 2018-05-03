package jonl.ge.editor;

import jonl.aui.Align;
import jonl.aui.Button;
import jonl.aui.Dial;
import jonl.aui.Layout;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;

public class NewTabWidget extends TPanel {

    private UIManager ui;
    
    public NewTabWidget(UIManager ui) {
        super();
        
        this.ui = ui;
        
        init();
    }
    
    void init() {
        
        Layout layout = ui.listLayout(Align.VERTICAL);
        setLayout(layout);
        
        Button newProject = button("Button A");
        Button openProject = button("Button B");
        Dial dial = ui.dial();
        
        layout.add(newProject);
        layout.add(openProject);
        layout.add(dial);
        
    }
    
    Button button(String text) {
        Button button = ui.button(text);
        button.setMinSize(0,200);
        button.setMaxSize(400,200);
        return button;
    }
    
}
