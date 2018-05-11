package jonl.ge.editor.spline;

import jonl.aui.Align;
import jonl.aui.ListLayout;
import jonl.aui.Panel;
import jonl.aui.SplitPanel;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.ge.core.SubApp;
import jonl.ge.editor.SubEditor;

public class SplineEditor implements SubEditor {

    UIManager ui;
    
    String name;
    Panel mainPanel;
    
    public SplineEditor(UIManager ui, Window window, String content) {
        this.ui = ui;
        this.name = "Spline";
        
        ListLayout layout = ui.listLayout(Align.VERTICAL);
        layout.setMargin(0,0,0,0);
        layout.setSpacing(0);
        
        mainPanel = ui.panel(layout);
        
        
        SplitPanel split = ui.splitPanel(Align.HORIZONTAL);
        split.setRatio(0.8);
        Panel sidePanel = ui.panel();
        Panel appPanel = ui.panel();
        
        split.setWidgets(appPanel,sidePanel);
        
        mainPanel.add(split);
        
        SplineScene scene = new SplineScene();
        
        SubApp app = new SubApp(window,appPanel);
        app.addScene(scene.scene);
        app.start();
    }
    
    @Override
    public String name() {
        return name;
    }

    @Override
    public Widget widget() {
        return mainPanel;
    }
    
    

}
