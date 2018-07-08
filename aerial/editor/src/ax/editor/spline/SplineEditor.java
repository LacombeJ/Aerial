package ax.editor.spline;

import ax.aui.Align;
import ax.aui.ListLayout;
import ax.aui.Panel;
import ax.aui.SplitPanel;
import ax.aui.UIManager;
import ax.aui.Widget;
import ax.aui.Window;
import ax.editor.SubEditor;
import ax.engine.core.ui.SubApp;

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
