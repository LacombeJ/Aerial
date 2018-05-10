package jonl.ge.editor;

import jonl.aui.ArrayLayout;
import jonl.aui.Button;
import jonl.aui.Label;
import jonl.aui.List;
import jonl.aui.Signal;
import jonl.aui.UIManager;
import jonl.aui.tea.TButton;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TLabel;
import jonl.aui.tea.TPanel;
import jonl.aui.tea.TSizeHint;
import jonl.aui.tea.TSizeReasoning;
import jonl.aui.tea.graphics.ButtonRenderer;
import jonl.aui.tea.graphics.TFont;
import jonl.aui.tea.graphics.TextRenderer;
import jonl.aui.tea.spatial.TBox;
import jonl.jutils.data.Dir;
import jonl.jutils.func.Callback;
import jonl.jutils.jss.Style;
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
        
        Label label = new ProjectLabel("Projects");
        label.setMinSize(300,0);
        
        List list = ui.list();
        list.setName("ProjectList");
        list.setMargin(1,1,1,1);
        
        newProject = new ProjectButton("New Project","");
        openProject = new ProjectButton("Open Project","");
        
        newProject.addStyle(buttonStyle(Color.fromInt(81,219,173), "editor/new"));
        openProject.addStyle(buttonStyle(Color.fromInt(24,125,226), "editor/open"));
        
        list.add(newProject);
        list.add(openProject);
        
        for (String projPath : editor.config.pastProjects) {
            Dir dir = new Dir(projPath);
            ProjectButton projButton = new ProjectButton(dir.name(),dir.path());
            projButton.clicked().connect(()->{
                load.emit((cb)->cb.f(projPath));
            });
            list.add(projButton);
        }
        
        list.setMinSize(200,200);
        
        layout.add(label,0,0);
        layout.add(list,1,1);
        
    }
    
    String buttonStyle(Color color, String icon) {
        int[] c = Color.toInt(color);
        String bg = "rgba("+c[0]+","+c[1]+","+c[2]+","+"64)";
        String bh = "rgba("+c[0]+","+c[1]+","+c[2]+","+"128)";
        String bd = "rgba("+c[0]+","+c[1]+","+c[2]+","+"32)";
        return ""
            + "ProjectButton {"
            + "     background: "+bg+";"
            +"      image: \""+icon+"\";"
            + "}"
            + "ProjectButton:hover {"
            + "     background: "+bh+";"
            + "}"
            + "ProjectButton:hover:down {"
            + "     background: "+bd+";"
            + "}";
    }
    
    class ProjectLabel extends TLabel {
        
        public ProjectLabel(String text) {
            super(text);
            setName("ProjectLabel");
        }
        
        @Override
        protected TSizeHint sizeHint() {
            return TSizeReasoning.label(this, TFont.CALIBRI_48);
        }
        
    }
    
    class ProjectButton extends TButton {

        String path = "";
        
        public ProjectButton(String text, String path) {
            super(text);
            setMinSize(0,100);
            this.path = path;
        }
        
        @Override
        protected TSizeHint sizeHint() {
            TSizeHint hint = super.sizeHint();
            hint.width += 32;
            return hint;
        }
        
        @Override
        protected void paint(TGraphics g) {
            Style style = ButtonRenderer.style(this,"ProjectButton",g,info());
            
            ButtonRenderer.paint(this,"ProjectButton",g,info());
            
            TBox box = new TBox(0,20,width(),height());
            TextRenderer.paint(style, path, box, g);
            paint().emit(cb->cb.f(g));
        }
        
    }
    
}
