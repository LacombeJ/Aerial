package jonl.aui.tea;

/**
 * This layout is a fill layout that limits the window size based on size hints
 * 
 * @author Jonathan
 *
 */
public class TWindowLayout extends TFillLayout {
    
    @Override
    void validateSizeHint(TSizeHint hint) {
        super.validateSizeHint(hint);
        
        TWindow window = (TWindow) parent;
        if (window.window()!=null) {
            int minWidth = window.minWidth();
            int minHeight = window.minHeight();
            
            int prefWidth = Math.max(minWidth, hint.width);
            int prefHeight = Math.max(minHeight, hint.height);
            // Using Integer.MAX_VALUE for glfw max size limits doesn't work. Using GLFW_DONT_CARE=-1 instead
            window.window().setSizeLimits(prefWidth, prefHeight, -1, -1);
        }
        
    }
    
}
