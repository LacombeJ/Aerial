package jonl.aerial.editor;

import java.util.HashMap;

import jonl.aui.Icon;
import jonl.aui.Timer;
import jonl.aui.UIManager;
import jonl.aui.Window;
import jonl.jutils.data.Json;

/**
 * Core functionality exposed to sub editors
 * 
 * @author Jonathan
 *
 */
public class Pivot {

    private UIManager ui;
    private Window window;
    //private Editor editor;
    
    HashMap<SubEditorTool,StoreTrait> storeTraits = new HashMap<>();
    
    HashMap<SubEditorTool,IconTrait> iconTraits = new HashMap<>();
    
    Pivot(Editor editor, UIManager ui, Window window) {
        //this.editor = editor;
        this.ui = ui;
        this.window = window;
    }
    
    public UIManager ui() {
        return ui;
    }
    
    public Window window() {
        
        return window;
    }
    
    public void setStoreTrait(SubEditorTool tool, StoreTrait trait) {
        storeTraits.put(tool,trait);
    }
    
    // ------------------------------------------------------------------------
    
    void loadTool(SubEditorTool tool) {
        
        tool.init(this);
        
        // Icons
        String iconResource = "/editor/editor_icon";
        Icon icon = tool.icon();
        if (icon!=null) {
            ui.resource(tool.id(),icon);
            iconResource = tool.id();
        }
        IconTrait iconTrait = new IconTrait(iconResource);
        iconTraits.put(tool, iconTrait);
        
    }
    
    void loadEditor(SubEditorTool tool, SubEditor subEditor) {
        StoreTrait storeTrait = (StoreTrait) storeTraits.get(tool);
        if (storeTrait != null) {
            Timer timer = ui.timer(subEditor.widget(), storeTrait.storeFrequency());
            timer.tick().connect(()->{
                store(storeTrait,subEditor);
            });
        }
    }
    
    void store(StoreTrait storeTrait, SubEditor subEditor) {
        Object store = storeTrait.getStore(subEditor);
        String path = storeTrait.storePaths.get(subEditor);
        Json json = new Json(path);
        json.save(store);
    }
    
    void addStorePath(SubEditorTool tool, SubEditor editor, String path) {
        StoreTrait storeTrait = (StoreTrait) storeTraits.get(tool);
        if (storeTrait!=null) {
            storeTrait.storePaths.put(editor,path);
        }
    }
    
}
