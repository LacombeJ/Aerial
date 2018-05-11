package jonl.ge.editor;

import jonl.aui.Icon;
import jonl.aui.UIManager;
import jonl.aui.Window;
import jonl.vmath.Color;

public interface SubEditorTool {
    
    String id();
    
    String name();
    
    String description();
    
    Color color();
    
    Icon icon(UIManager ui);
    
    SubEditor open(UIManager ui, Window window);
    
    SubEditor open(UIManager ui, Window window, String content);
    
    static class LoadedTool {
        SubEditorTool tool;
        String id;
        String name;
        String description;
        Color color;
        String iconResource;
    }
    
}
