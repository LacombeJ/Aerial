package ax.editor;

import ax.aui.ArrayLayout;
import ax.aui.Button;
import ax.aui.Label;
import ax.aui.List;
import ax.aui.Signal;
import ax.aui.UIManager;
import ax.aui.tea.TPanel;
import ax.commons.func.Callback;
import ax.editor.EditorProjectUI.Tab;

public class ToolMenuWidget extends TPanel {

    private Editor editor;
    private UIManager ui;
    
    Signal<Callback<Tab>> selected = new Signal<>();
    
    public ToolMenuWidget(Editor editor, UIManager ui) {
        super();
        
        this.editor = editor;
        this.ui = ui;
        
        init();
    }
    
    void init() {
        
        ArrayLayout layout = ui.arrayLayout();
        
        setLayout(layout);
        
        Label label = new UI.ProjectLabel("Tool Menu");
        label.setMinSize(300,0);
        
        List list = ui.list();
        list.setName("ProjectList");
        list.setMargin(1,1,1,1);
        
        for (SubEditorTool tool : editor.subEditorTools) {
            
            String iconResource = editor.pivot.iconTraits.get(tool).iconResource;
            
            UI.ProjectButton button = new UI.ProjectButton(tool.name(),tool.description());
            button.addStyle(UI.buttonStyle(tool.color(),iconResource));
            
            button.clicked().connect(()->{
                
                SubEditor se = tool.open();
                
                editor.pivot.loadEditor(tool,se);
                
                Tab tab = new Tab(se.widget(),se.name(),tool,se);
                selected.emit((cb)->cb.f(tab));
                
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
