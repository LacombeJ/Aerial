package jonl.ge.editor;

import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.jutils.data.Cereal;

public class SceneEditor extends SubEditor {

    UIManager ui;
    
    String name;
    Widget widget;
    
    SceneState state;
    SceneState stateCopy;
    
    public SceneEditor(UIManager ui, Window window, Object state) {
        this.ui = ui;
        this.name = "Scene";
        
        this.state = (state==null) ? new SceneState() : (SceneState) state;
        setStore();
        
        widget = ui.dial();
    }
    
    @Override
    public String name() {
        return name;
    }

    @Override
    public Widget widget() {
        return widget;
    }

    @Override
    public boolean shouldStore() {
        return true;
    }
    
    @Override
    public synchronized void setStore() {
        stateCopy = Cereal.copy(state);
    }

    @Override
    public synchronized Object getStore() {
        return stateCopy;
    }

}
