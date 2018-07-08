/**
 * 
 */
/**
 * @author Jonathan
 *
 */
module ax.editor {
    exports ax.editor;
    exports ax.editor.data;
    exports ax.editor.spline;
    
    requires java.desktop;
    
    requires transitive ax.engine;
    requires transitive ax.std;
    requires transitive ax.aui;
    requires transitive ax.tea;
}