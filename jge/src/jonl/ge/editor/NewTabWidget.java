package jonl.ge.editor;

import jonl.aui.ArrayLayout;
import jonl.aui.Button;
import jonl.aui.Label;
import jonl.aui.List;
import jonl.aui.Signal;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.tea.TPanel;
import jonl.ge.editor.SubEditorTool.LoadedTool;
import jonl.jutils.func.Callback2D;

public class NewTabWidget extends TPanel {

    private Editor editor;
    private UIManager ui;
    
    Signal<Callback2D<Widget,String>> selected = new Signal<>();
    
    public NewTabWidget(Editor editor, UIManager ui) {
        super();
        
        this.editor = editor;
        this.ui = ui;
        
        init();
    }
    
    void init() {
        
        ArrayLayout layout = ui.arrayLayout();
        
        setLayout(layout);
        
        Label label = new UI.ProjectLabel("Project Menu");
        label.setMinSize(300,0);
        
        List list = ui.list();
        list.setName("ProjectList");
        list.setMargin(1,1,1,1);
        
        for (SubEditorTool set : editor.subEditorTools) {
            LoadedTool tool = editor.loadedTools.get(set);
            
            UI.ProjectButton button = new UI.ProjectButton(tool.name,tool.description);
            button.addStyle(UI.buttonStyle(tool.color,tool.iconResource));
            
            button.clicked().connect(()->{
                
                SubEditor se = set.open(ui,editor.gui.form.window);
                selected.emit((cb)->cb.f(se.widget(),se.name()));
                
            });
            
            list.add(button);
            
        }

        list.setMinSize(200,200);
        
        layout.add(label,0,0);
        layout.add(list,1,1);

    }
    
    Button button(String text) {
        Button button = ui.button(text);
        button.setMinSize(0,200);
        button.setMaxSize(400,200);
        return button;
    }
    
}
