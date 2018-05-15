package jonl.aerial.spline;

import jonl.aerial.SubEditor;
import jonl.aui.Align;
import jonl.aui.ListLayout;
import jonl.aui.Panel;
import jonl.aui.SplitPanel;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.ge.core.SubApp;

public class SplineEditor implements SubEditor {

    SplineEditorTool tool;
    UIManager ui;
    
    String name;
    Panel mainPanel;
    
    SplineState state;
    SplineState stateCopy;
    
    public SplineEditor(SplineEditorTool tool, Object store) {
        this.tool = tool;
        this.name = "Spline";
        
        UIManager ui = tool.pivot().ui();
        Window window = tool.pivot().window();
        
        state = (store==null) ? new SplineState() : (SplineState) store;
        tool.store.setStore(this);
        
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
        
        SplineScene scene = new SplineScene(this);
        
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
