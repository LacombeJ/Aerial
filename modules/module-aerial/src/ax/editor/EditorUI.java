package ax.editor;

import java.io.InputStream;

import ax.aui.FileDialog;
import ax.aui.tea.TUIManager;
import ax.commons.data.Dir;
import ax.commons.io.FileUtils;
import ax.commons.jss.Style;
import ax.commons.jss.StyleSheet;

public class EditorUI {

	public Editor editor;
	public TUIManager ui;
	
	public EditorUIForm form;
    public EditorStartup startupPanel;
    public EditorProjectUI projectUi;
    
    public EditorUI(Editor editor) {
        this.editor = editor;
        ui = TUIManager.instance();
    }
    
    Style editorStyle(boolean light) {
        InputStream in = getClass().getResourceAsStream("/editor/editor.jss");
        Style jss = StyleSheet.fromString(FileUtils.readFromStream(in));
        if (light) {
            jss.style("#ProjectList").put("border-color","black");
        } else {
            jss.style("#ProjectList").put("border-color","white");
        }
        return jss;
    }
    
    void setStyle(String type) {
        if (type.equals("dark")) {
            ui.setDarkStyle();
            Style style = editorStyle(false);
            ui.addStyle(style);
        } else if (type.equals("aerial")) {
            ui.setDarkStyle();
            Style style = editorStyle(false);
            ui.addStyle(style);
            
            Style jss = new Style("jss");
            Style window = new Style("Window");
            window.put("background","linear-gradient(rgb(79,123,145), rgb(102,109,130));");
            jss.add(window);
            ui.addStyle(jss);
        } else {
            type = "light";
            ui.setLightStyle();
            Style style = editorStyle(true);
            ui.addStyle(style);
        }
        editor.config.style = type;
        editor.save();
    }
    
    void resourceIcon(String loc, String resource) {
        UI.resourceIcon(ui,this.getClass(),loc,resource);
    }
    
    private void loadTools() {
        editor.pivot = new Pivot(editor, ui, form.window);
        
        for (SubEditorTool tool : editor.subEditorTools) {
            
            editor.pivot.loadTool(tool);
            
        }
    }
    
    public void create() {
        form = new EditorUIForm();
        
        setStyle(editor.config.style);
        
        resourceIcon("/editor/aerial.png",              "editor/aerial");
        
        resourceIcon("/editor/new_icon.png",            "editor/new");
        resourceIcon("/editor/load_icon.png",           "editor/load");
        resourceIcon("/editor/open_icon.png",           "editor/open");
        
        resourceIcon("/editor/new_toolbutton.png",      "editor/new_tb");
        resourceIcon("/editor/open_toolbutton.png",     "editor/open_tb");
        resourceIcon("/editor/close_toolbutton.png",    "editor/close_tb");
        resourceIcon("/editor/save_toolbutton.png",     "editor/save_tb");
        
        resourceIcon("/editor/editor_icon.png",         "/editor/editor_icon");
        resourceIcon("/editor/settings_icon.png",       "/editor/settings_icon");
        
        form.create(ui);
        
        loadTools();
        
        init();
        connect();
        
        form.window.create();
        
        String path = editor.config.lastProj;
        if (!path.equals("")) {
            loadProject(new Dir(path));
        }
        
        form.window.maximize();
        form.window.setVisible(true);
        
    }
    
    private void init() {
        
        startupPanel = new EditorStartup(editor,ui);
        projectUi = new EditorProjectUI(editor,ui);
        
        form.switchWidget.add(startupPanel);
        form.switchWidget.add(projectUi);
        
        toStartupPage();
    }
    
    private void connect() {
        
        form.closeProjectMenu.clicked().connect(()->{
            closeProject();
        });
        
        form.exitMenu.clicked().connect(()->{
            System.exit(0);
        });
        
        form.newToolButton.clicked().connect(()->{
            newProject();
        });
        
        form.openToolButton.clicked().connect(()->{
            openProject();
        });
        
        form.closeToolButton.clicked().connect(()->{
            closeProject();
        });
        
        form.lightMenu.clicked().connect(()->{
            setStyle("light");
        });
        
        form.darkMenu.clicked().connect(()->{
            setStyle("dark");
        });
        
        form.aerialMenu.clicked().connect(()->{
            setStyle("aerial");
        });
        
        // --------------------------------------------------------------------
        
        startupPanel.newProject.clicked().connect(()->{
            newProject();
        });
        
        startupPanel.openProject.clicked().connect(()->{
            openProject();
        });
        
        startupPanel.load.connect((path) -> {
            loadProject(new Dir(path));
        });
        
    }
    
    private void newProject() {
        FileDialog fd = ui.fileDialog();
        fd.setMode(FileDialog.DIRECTORIES_ONLY);
        fd.setDirectory(editor.config.lastDir);
        
        if (fd.showSaveDialog()==FileDialog.APPROVE) {
            String path = fd.selected();
            
            Dir dir = new Dir(path);
            if (dir.isDirectory()) {
                Dir project = dir.dir("project.json");
                if (!project.exists()) {
                    createProject(dir);
                } else {
                    ui.messageDialog().error("Error","File \"project.json\" already exists in this directory.");
                }
            } else {
                if (!dir.exists()) {
                    if (dir.mkdir()) {
                        createProject(dir);
                    } else {
                        ui.messageDialog().error("Error","Failed to create directory: \""+dir.path()+"\"");
                    }
                } else {
                    ui.messageDialog().error("Error","Path: \""+dir.path()+"\" is not a directory");
                }
                
            }
            
            editor.config.lastDir = (dir.isDirectory()) ? dir.parent().path() : dir.parent().parent().path();
            editor.save();
        }
    }
    
    private void openProject() {
        FileDialog fd = ui.fileDialog();
        fd.setMode(FileDialog.FILES_AND_DIRECTORIES);
        fd.setFilter("Project", "*.json");
        fd.setDirectory(editor.config.lastDir);
        
        if (fd.showOpenDialog()==FileDialog.APPROVE) {
            String path = fd.selected();
            
            Dir dir = new Dir(path);
            if (dir.isFile()) {
                if (dir.name().equals("project.json")) {
                    loadProject(dir.parent());
                } else {
                    ui.messageDialog().error("Error","Invalid project file: \""+dir.name()+"\"");
                }
            } else {
                Dir project = dir.dir("project.json");
                if (!project.exists()) {
                    ui.messageDialog().error("Error","File \"project.json\" not found.");
                } else {
                    loadProject(dir);
                }
            }
            
            editor.config.lastDir = (dir.isDirectory()) ? dir.parent().path() : dir.parent().parent().path();
            editor.save();
        }
    }
    
    private void cleanProject() {
        form.switchWidget.remove(projectUi);
        projectUi = new EditorProjectUI(editor,ui);
        form.switchWidget.add(projectUi);
    }
    
    private void closeProject() {
        editor.config.lastProj = "";
        editor.save();
        
        toStartupPage();
    }
    
    private void loadProject(Dir dir) {
        EditorProject proj = new EditorProject(editor, dir);
        if (proj.load()) {
            editor.project = proj;
            
            toProjectPage();
        } else {
            //TODO
        }
    }
    
    private void createProject(Dir dir) {
        editor.project = new EditorProject(editor, dir);
        editor.project.save();
        
        toProjectPage();
    }
    
    private void toStartupPage() {
        cleanProject();
        form.switchWidget.setWidget(startupPanel);
        
        form.newToolButton.setEnabled(true);
        form.openToolButton.setEnabled(true);
        form.closeToolButton.setEnabled(false);
        form.saveToolButton.setEnabled(false);
    }
    
    private void toProjectPage() {
        cleanProject();
        
        projectUi.populate();
        form.switchWidget.setWidget(projectUi);
        
        editor.config.lastProj = editor.project.dir.path();
        editor.config.pastProjects.add(editor.config.lastProj);
        editor.save();
        
        form.newToolButton.setEnabled(false);
        form.openToolButton.setEnabled(false);
        form.closeToolButton.setEnabled(true);
        form.saveToolButton.setEnabled(true);
    }

}
