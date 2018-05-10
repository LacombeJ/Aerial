package jonl.ge.editor;

import java.io.InputStream;

import jonl.aui.Align;
import jonl.aui.FileDialog;
import jonl.aui.HAlign;
import jonl.aui.Menu;
import jonl.aui.MenuBar;
import jonl.aui.Panel;
import jonl.aui.SwitchWidget;
import jonl.aui.ToolBar;
import jonl.aui.ToolButton;
import jonl.aui.VAlign;
import jonl.aui.tea.TIcon;
import jonl.aui.tea.TUIManager;
import jonl.jutils.data.Dir;
import jonl.jutils.io.FileUtils;
import jonl.jutils.jss.Style;
import jonl.jutils.jss.StyleSheet;
import jonl.jutils.misc.ImageUtils;

public class EditorGUI {

	public Editor editor;
	public TUIManager ui;
	
    public jonl.aui.Window window;
    public Panel main;
        public MenuBar menuBar;
        public ToolBar toolBar;
        public SwitchWidget switchWidget;
            public EditorStartup startupPanel;
            public EditorProjectUI projectUi;
    
    public EditorGUI(Editor editor) {
        this.editor = editor;
    }
    
    Style editorStyle() {
        InputStream in = getClass().getResourceAsStream("/editor/editor.jss");
        Style jss = StyleSheet.fromString(FileUtils.readFromStream(in));
        return jss;
    }
    
    void resourceIcon(String loc, String resource) {
        InputStream in = getClass().getResourceAsStream(loc);
        if (in != null) {
            ui.resource(resource, new TIcon(ImageUtils.load(in)));
        } else {
            System.err.println("Failed to find icon resource: "+loc);
        }
    }
    
    public void create() {
        
        ui = TUIManager.instance();
        
        Style style = editorStyle();
        ui.addStyle(style);
        
        resourceIcon("/editor/new_icon.png",  "editor/new");
        resourceIcon("/editor/load_icon.png", "editor/load");
        resourceIcon("/editor/open_icon.png", "editor/open");
        
        // Window
        window = ui.window();
        window.setTitle("Engine");
        window.setWidth(1024);
        window.setHeight(576);
        window.setPosition(HAlign.CENTER,VAlign.MIDDLE);
        window.setResizable(true);
        
        // Main panel
        main = ui.panel(ui.listLayout(Align.VERTICAL));
        main.layout().setMargin(0,0,0,0);
        main.layout().setSpacing(0);
        
        createMenuBar();
        main.add(menuBar);
        
        createToolBar();
        main.add(toolBar);
        
        createSwitchWidget();
        main.add(switchWidget);
        
        load();
        
        window.setWidget(main);
        
        window.create();
        window.maximize();
        
        window.setVisible(true);
        
    }
    
    private void createMenuBar() {
        menuBar = ui.menuBar();
        Menu file = ui.menu("File"); {
            
            Menu newMenu = ui.menu("New");
            
            Menu openMenu = ui.menu("Open");
            
            Menu saveMenu = ui.menu("Save");
            
            Menu closeProjectMenu = ui.menu("Close Project");
            closeProjectMenu.clicked().connect(()->{
                editor.config.lastProj = "";
                editor.save();
                
                switchWidget.remove(projectUi);
                
                projectUi = new EditorProjectUI(editor,ui);
                switchWidget.add(projectUi);
                switchWidget.setWidget(startupPanel);
            });
            
            Menu exitMenu = ui.menu("Exit");
            exitMenu.clicked().connect(()->{
                System.exit(0);
            });
            
            file.add(newMenu);
            file.add(openMenu);
            file.addSeparator();
            file.add(saveMenu);
            file.addSeparator();
            file.add(closeProjectMenu);
            file.add(exitMenu);
        }
        
        Menu edit = ui.menu("Edit");
        menuBar.add(file);
        menuBar.add(edit);
    }
    
    private void createToolBar() {
        toolBar = ui.toolBar();
        ToolButton newButton = ui.toolButton();
        ToolButton save = ui.toolButton();
        ToolButton saveAll = ui.toolButton();
        toolBar.add(newButton);
        toolBar.add(save);
        toolBar.add(saveAll);
    }
    
    private void createSwitchWidget() {
        switchWidget = ui.switchWidget();
        createStartupPanel();
        projectUi = new EditorProjectUI(editor,ui);
        switchWidget.add(startupPanel);
        switchWidget.add(projectUi);
    }
    
    private void createStartupPanel() {
        startupPanel = new EditorStartup(editor,ui);
        
        startupPanel.newProject.clicked().connect(()->{
            
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
            
        });
        
        startupPanel.openProject.clicked().connect(()->{
            
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
            
        });
        
        startupPanel.load.connect((path) -> {
            loadProject(new Dir(path));
        });
        
    }
    
    private void load() {
        String path = editor.config.lastProj;
        
        if (!path.equals("")) {
            loadProject(new Dir(path));
        } else {
            switchWidget.setWidget(startupPanel);
        }
    }
    
    private void loadProject(Dir dir) {
        EditorProject proj = new EditorProject(editor, dir);
        if (proj.load()) {
            editor.project = proj;
            
            projectUi.populate();
            switchWidget.setWidget(projectUi);
            
            editor.config.lastProj = dir.path();
            editor.config.pastProjects.add(editor.config.lastProj);
            editor.save();
        } else {
            //TODO
        }
    }
    
    private void createProject(Dir dir) {
        editor.project = new EditorProject(editor, dir);
        editor.project.save();
        
        projectUi.populate();
        switchWidget.setWidget(projectUi);
        
        editor.config.lastProj = dir.path();
        editor.config.pastProjects.add(editor.config.lastProj);
        editor.save();
    }

}
