package jonl.ge.core.editor;

import jonl.ge.core.Editor;

public class EditorCore {

    Editor editor;
    public EditorScene scene;
    public EditorGUI gui;
    
    public EditorCore(Editor editor) {
        this.editor = editor;
        scene = new EditorScene(this,editor);
        gui = new EditorGUI(this,editor);
    }
    
}
