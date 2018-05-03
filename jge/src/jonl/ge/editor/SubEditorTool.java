package jonl.ge.editor;

import jonl.aui.UIManager;

public interface SubEditorTool {

    String id();
    
    SubEditor open(UIManager ui);
    
    SubEditor open(UIManager ui, String content);
    
}
