package jonl.ge.editor;

import jonl.aui.ArrayLayout;
import jonl.aui.Button;
import jonl.aui.Label;
import jonl.aui.List;
import jonl.aui.Signal;
import jonl.aui.UIManager;
import jonl.aui.tea.TPanel;
import jonl.jutils.data.Dir;
import jonl.jutils.func.Callback;
import jonl.vmath.Color;

public class EditorStartup extends TPanel {

    private Editor editor;
    private UIManager ui;
    
    Button newProject;
    Button openProject;
    
    Signal<Callback<String>> load = new Signal<>();
    
    public EditorStartup(Editor editor, UIManager ui) {
        this.editor = editor;
        this.ui = ui;
        
        init();
    }

    void init() {
        
        ArrayLayout layout = ui.arrayLayout();
        
        setLayout(layout);
        
        Label label = new UI.ProjectLabel("Projects");
        label.setMinSize(300,0);
        
        List list = ui.list();
        list.setName("ProjectList");
        list.setMargin(1,1,1,1);
        
        newProject = new UI.ProjectButton("New Project","");
        openProject = new UI.ProjectButton("Open Project","");
        
        newProject.addStyle(UI.buttonStyle(Color.fromInt(81,219,173), "editor/new"));
        openProject.addStyle(UI.buttonStyle(Color.fromInt(24,125,226), "editor/open"));
        
        list.add(newProject);
        list.add(openProject);
        
        for (String projPath : editor.config.pastProjects) {
            Dir dir = new Dir(projPath);
            UI.ProjectButton projButton = new UI.ProjectButton(dir.name(),dir.path());
            projButton.clicked().connect(()->{
                load.emit((cb)->cb.f(projPath));
            });
            list.add(projButton);
        }
        
        list.setMinSize(200,200);
        
        layout.add(label,0,0);
        layout.add(list,1,1);
        
    }
    
    
    
    
    
}
