package jonl.ge.core.app;

import jonl.aui.Align;
import jonl.aui.HAlign;
import jonl.aui.MenuBar;
import jonl.aui.MenuButton;
import jonl.aui.Panel;
import jonl.aui.Slider;
import jonl.aui.SpacerItem;
import jonl.aui.SplitPanel;
import jonl.aui.TabPanel;
import jonl.aui.ToolBar;
import jonl.aui.ToolButton;
import jonl.aui.UIManager;
import jonl.aui.VAlign;
import jonl.aui.tea.TUIManager;
import jonl.aui.tea.TWindow;
import jonl.aui.tea.graphics.TStyleDark;
import jonl.ge.core.Editor;
import jonl.jutils.func.Callback;
import jonl.vmath.Color;

public class EditorGUI {

	public Editor editor;
    
	public TUIManager ui;
    
    public TWindow window;
    public Panel main;
        public MenuBar menuBar;
        public ToolBar toolBar;
        public SplitPanel mainSplitPanel;
            public TabPanel contentPanel;
                public Panel hierarchyPanel;
            public SplitPanel sideSplitPanel;
                public TabPanel viewPanel;
                    public Panel editorViewer;
                public Panel propertiesPanel;
    
    public boolean resizable = true;
    public boolean fullscreen = false;
    
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
        window.setResizable(resizable);
        
        // Main panel
        main = ui.panel(ui.listLayout(Align.VERTICAL));
        main.layout().setMargin(0,0,0,0);
        main.layout().setSpacing(0);
        
        createMenuBar();
        main.add(menuBar);
        
        createToolBar();
        main.add(toolBar);
        
        createMainSplitPanel();
        main.add(mainSplitPanel);
        
        window.setWidget(main);
        
        window.create();
        
        window.setVisible(true);
        
    }
    
    private void createMenuBar() {
        menuBar = ui.menuBar();
        MenuButton file = ui.menuButton("File");
        MenuButton edit = ui.menuButton("Edit");
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
        sideSplitPanel = ui.splitPanel(viewPanel, propertiesPanel, Align.HORIZONTAL, 0.8);
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
        propertiesPanel = ui.panel(ui.listLayout(Align.VERTICAL));
        
        Slider sr = ui.slider(Align.HORIZONTAL);
        Slider sg = ui.slider(Align.HORIZONTAL);
        Slider sb = ui.slider(Align.HORIZONTAL);
        Panel bg = ui.panel();
        SpacerItem spacer = ui.spacerItem();
        
        Callback<Integer> slot = (v) -> updateBackgroundColor();
        
        sr.changed().connect(slot);
        sg.changed().connect(slot);
        sb.changed().connect(slot);
        
        propertiesPanel.add(sr);
        propertiesPanel.add(sg);
        propertiesPanel.add(sb);
        propertiesPanel.add(bg);
        propertiesPanel.add(spacer);
    }
    
    private void updateBackgroundColor() {
        Slider sr = (Slider) propertiesPanel.getWidget(0);
        Slider sg = (Slider) propertiesPanel.getWidget(1);
        Slider sb = (Slider) propertiesPanel.getWidget(2);
        float r = sr.value()/100f;
        float g = sg.value()/100f;
        float b = sb.value()/100f;
        editor.setBackground(Color.fromFloat(r,g,b).toVector());
    }
    
}
