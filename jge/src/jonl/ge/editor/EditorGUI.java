package jonl.ge.editor;

import jonl.aui.Align;
import jonl.aui.FileDialog;
import jonl.aui.HAlign;
import jonl.aui.Menu;
import jonl.aui.MenuBar;
import jonl.aui.Panel;
import jonl.aui.ScrollPanel;
import jonl.aui.SwitchWidget;
import jonl.aui.ToolBar;
import jonl.aui.ToolButton;
import jonl.aui.VAlign;
import jonl.aui.tea.TUIManager;
import jonl.aui.tea.TWindow;
import jonl.aui.tea.graphics.TStyleDark;
import jonl.jutils.data.Dir;

public class EditorGUI {

	public Editor editor;
	public TUIManager ui;
	
    public TWindow window;
    public Panel main;
        public MenuBar menuBar;
        public ToolBar toolBar;
        public SwitchWidget switchWidget;
            public ScrollPanel startupScroll;
                public EditorStartup startupPanel;
            public EditorProjectUI projectUi;
    
    public EditorGUI(Editor editor) {
        this.editor = editor;
    }
    
    public void create() {
        
        ui = TUIManager.instance();
        ui.setStyle(new TStyleDark());
        
        // Window
        window = (TWindow) ui.window();
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
        
        window.setWidget(main);
        
        window.create();
        window.maximize();
        
        window.setVisible(true);
        
    }
    
    private void createMenuBar() {
        menuBar = ui.menuBar();
        Menu file = ui.menu("File");
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
        switchWidget.add(startupScroll);
        switchWidget.add(projectUi);
    }
    
    private void createStartupPanel() {
        startupScroll = ui.scrollPanel();
        
        startupPanel = new EditorStartup(ui);
        
        startupPanel.newProject.clicked().connect(()->{
            
            FileDialog fd = ui.fileDialog();
            fd.setMode(FileDialog.DIRECTORIES_ONLY);
            
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
                    ui.messageDialog().error("Error","Path: \""+dir.path()+"\" is not a directory");
                }
                
            }
            
        });
        
        startupPanel.openProject.clicked().connect(()->{
            
            FileDialog fd = ui.fileDialog();
            fd.setMode(FileDialog.DIRECTORIES_ONLY);
            
            if (fd.showOpenDialog()==FileDialog.APPROVE) {
                String path = fd.selected();
                
                Dir dir = new Dir(path);
                if (dir.isFile()) {
                    if (dir.name().equals("project.json")) {
                        loadProject(dir);
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
                
            }
            
            
        });
        
        
        startupScroll.setWidget(startupPanel);
    }
    
    private void loadProject(Dir dir) {
        editor.project = new EditorProject(editor, dir);
        editor.project.load();
        
        projectUi.populate();
        switchWidget.setWidget(projectUi);
    }
    
    private void createProject(Dir dir) {
        editor.project = new EditorProject(editor, dir);
        editor.project.save();
        
        projectUi.populate();
        switchWidget.setWidget(projectUi);
    }
    
    


    /*
    private void createMainSplitPanel() {
        createContentPanel();
        createSideSplitPanel();
        
        mainSplitPanel = ui.splitPanel(contentPanel, sideSplitPanel, Align.HORIZONTAL, 0.15);
    }
    
    private void createContentPanel() {
        contentPanel = ui.tabPanel();
        
        createHierarchyPanel();
        contentPanel.add(hierarchyPanel, "Hierarchy");
    }
    
    private void createHierarchyPanel() {
        hierarchyPanel = ui.panel();
    }
    
    private void createSideSplitPanel() {
        createViewPanel();
        createPropertiesPanel();
        sideSplitPanel = ui.splitPanel(viewPanel, propertiesPanel, Align.HORIZONTAL, 0.85);
    }
    
    private void createViewPanel() {
        viewPanel = ui.tabPanel();
        
        
        
        createEditorViewer();
        viewPanel.add(editorViewer, "Editor");
    }
    
    private void createEditorViewer() {
        editorViewer = ui.panel();
    }
    
    private void createPropertiesPanel() {
        propertiesPanel = ui.scrollPanel();
        
        Panel properties = ui.panel(ui.listLayout(Align.VERTICAL));
        
        Spacer spacer = ui.spacer();
        
        properties.add(background());
        properties.add(grid());
        properties.add(spacer);
        
        propertiesPanel.setWidget(properties);
    }
    
    private Panel background() {
        ArrayLayout array = ui.arrayLayout();
        Panel panel = ui.titlePanel("Background Color",array);
        
        array.add(ui.label("Red:"),0,0);
        array.add(ui.label("Green:"),1,0);
        array.add(ui.label("Blue:"),2,0);
        
        LineEdit redText = ui.lineEdit("");
        LineEdit greenText = ui.lineEdit("");
        LineEdit blueText = ui.lineEdit("");
        
        redText.setMinSize(30,0);
        greenText.setMinSize(30,0);
        blueText.setMaxSize(30,0);
        
        redText.setMaxSize(30,Integer.MAX_VALUE);
        greenText.setMaxSize(30,Integer.MAX_VALUE);
        blueText.setMaxSize(30,Integer.MAX_VALUE);
        
        array.add(redText,0,1);
        array.add(greenText,1,1);
        array.add(blueText,2,1);
        
        Slider sr = ui.slider(Align.HORIZONTAL);
        Slider sg = ui.slider(Align.HORIZONTAL);
        Slider sb = ui.slider(Align.HORIZONTAL);
        
        array.add(sr,0,2);
        array.add(sg,1,2);
        array.add(sb,2,2);
        
        Color color = Color.fromVector(editor.getBackground());
        sr.setValue((int)(color.r*100));
        sg.setValue((int)(color.g*100));
        sb.setValue((int)(color.b*100));
        redText.setText(sr.value()+"");
        greenText.setText(sg.value()+"");
        blueText.setText(sb.value()+"");
        Callback<Integer> slot = (v) -> {
            redText.setText(sr.value()+"");
            greenText.setText(sg.value()+"");
            blueText.setText(sb.value()+"");
            editor.setBackground(Color.fromFloat(sr.value()/100f,sg.value()/100f,sb.value()/100f).toVector());
        };
        
        sr.changed().connect(slot);
        sg.changed().connect(slot);
        sb.changed().connect(slot);
        
        return panel;
    }
    
    private Panel grid() {
        ArrayLayout array = ui.arrayLayout();
        Panel panel = ui.titlePanel("Grid",array);
        
        //Scale ranges from 0f to 5f;
        //We are using floating point with range from 0 to 5000
        
        array.add(ui.label("Grid size:"),0,0);
        //array.add(ui.label("Grid scale:"),1,0);
        
        LineEdit gridSizeEdit = ui.lineEdit("");
        LineEdit gridScaleEdit = ui.lineEdit("");
        
        gridSizeEdit.setMinSize(30,0);
        gridScaleEdit.setMinSize(30,0);
        
        gridSizeEdit.setMaxSize(30,Integer.MAX_VALUE);
        gridScaleEdit.setMaxSize(30,Integer.MAX_VALUE);
        
        array.add(gridSizeEdit,0,1);
        //array.add(gridScaleEdit,1,1);
        
        Slider gridSizeSlider = ui.slider(Align.HORIZONTAL,0,20);
        Slider gridScaleSlider = ui.slider(Align.HORIZONTAL,0,5000);
        
        array.add(gridSizeSlider,0,2);
        //array.add(gridScaleSlider,1,2);
        
        gridSizeSlider.setValue(core.scene.gridSize);
        gridScaleSlider.setValue((int)(core.scene.gridScale*1000));
        
        gridSizeEdit.setText(gridSizeSlider.value()+"");
        gridScaleEdit.setText((gridScaleSlider.value()/1000f)+"");
        
        Callback<Integer> slot = (v) -> {
            gridSizeEdit.setText(gridSizeSlider.value()+"");
            gridScaleEdit.setText((gridScaleSlider.value()/1000f)+"");
            
            core.scene.gridSize = gridSizeSlider.value();
            core.scene.gridScale = gridScaleSlider.value() / 1000f;
            core.scene.updateGrid();
        };
        
        gridSizeSlider.changed().connect(slot);
        gridScaleSlider.changed().connect(slot);
        
        return panel;
    }
    */
    
}
