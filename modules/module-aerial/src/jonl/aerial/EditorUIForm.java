package jonl.aerial;

import jonl.aui.Align;
import jonl.aui.HAlign;
import jonl.aui.Menu;
import jonl.aui.MenuBar;
import jonl.aui.Panel;
import jonl.aui.SwitchWidget;
import jonl.aui.ToolBar;
import jonl.aui.ToolButton;
import jonl.aui.UIManager;
import jonl.aui.VAlign;
import jonl.aui.Window;
import jonl.jutils.misc.ImageUtils;

public class EditorUIForm {

    public UIManager ui;
    
    public Window window;
    public Panel main;
        public MenuBar menuBar;
            public Menu fileMenu;
                public Menu newMenu;
                public Menu openMenu;
                public Menu saveMenu;
                public Menu closeProjectMenu;
                public Menu exitMenu;
            public Menu editMenu;
            public Menu styleMenu;
                public Menu lightMenu;
                public Menu darkMenu;
                public Menu aerialMenu;
        public ToolBar toolBar;
            public ToolButton newToolButton;
            public ToolButton openToolButton;
            public ToolButton closeToolButton;
            public ToolButton saveToolButton;
        public SwitchWidget switchWidget;
    
    void create(UIManager ui) {
        this.ui = ui;
        
        // Window
        window = ui.window();
        window.setTitle("Editor");
        window.setWidth(1024);
        window.setHeight(576);
        window.setPosition(HAlign.CENTER,VAlign.MIDDLE);
        window.setResizable(true);
        window.setIcon(ImageUtils.load(getClass().getResourceAsStream("/editor/aerial.png")));
        
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
        
    }
    
    private void createMenuBar() {
        menuBar = ui.menuBar();
        
        fileMenu = ui.menu("File"); {
            newMenu = ui.menu("New");
            openMenu = ui.menu("Open");
            saveMenu = ui.menu("Save");
            closeProjectMenu = ui.menu("Close Project");
            exitMenu = ui.menu("Exit");
            
            fileMenu.add(newMenu);
            fileMenu.add(openMenu);
            fileMenu.addSeparator();
            fileMenu.add(saveMenu);
            fileMenu.addSeparator();
            fileMenu.add(closeProjectMenu);
            fileMenu.add(exitMenu);
        }
        
        editMenu = ui.menu("Edit");
        
        styleMenu = ui.menu("Style"); {
            lightMenu = ui.menu("Set style to light theme");
            darkMenu = ui.menu("Set style to dark theme");
            aerialMenu = ui.menu("Set style to aerial theme");
            
            styleMenu.add(lightMenu);
            styleMenu.add(darkMenu);
            styleMenu.add(aerialMenu);
        }
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(styleMenu);
    }
    
    private void createToolBar() {
        toolBar = ui.toolBar();
        
        newToolButton = ui.toolButton(ui.resource("editor/new_tb"));
        openToolButton = ui.toolButton(ui.resource("editor/open_tb"));
        closeToolButton = ui.toolButton(ui.resource("editor/close_tb"));
        saveToolButton = ui.toolButton(ui.resource("editor/save_tb"));
        saveToolButton.setEnabled(false);
        
        toolBar.add(newToolButton);
        toolBar.add(openToolButton);
        toolBar.add(closeToolButton);
        toolBar.addSeparator();
        toolBar.add(saveToolButton);
    }
    
    private void createSwitchWidget() {
        switchWidget = ui.switchWidget();
        
    }
    
}
