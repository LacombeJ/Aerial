package ax.examples.editor;

import ax.editor.Editor;

public class EditorMain {

    public static void main(String[]args) {
        new EditorMain().run();
    }
    
    void run() {
        
        Editor editor = new Editor();
        
        editor.start();
        
    }
    
}
