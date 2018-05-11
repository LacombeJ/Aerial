package jonl.ge.editor;

import jonl.aui.Icon;
import jonl.aui.UIManager;
import jonl.aui.Window;
import jonl.vmath.Color;

public abstract class SubEditorTool {
    
    public abstract String id();
    
    public abstract String name();
    
    public abstract String description();
    
    public abstract Color color();
    
    public abstract Icon icon(UIManager ui);
    
    public abstract SubEditor open(UIManager ui, Window window);
    
    public abstract SubEditor open(UIManager ui, Window window, Object store);
    
    public abstract Class<?> stateDefinition();
    
    static class LoadedTool {
        SubEditorTool tool;
        String id;
        String name;
        String description;
        Color color;
        String iconResource;
    }
    
}
