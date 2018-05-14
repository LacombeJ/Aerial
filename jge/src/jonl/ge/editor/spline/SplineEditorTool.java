package jonl.ge.editor.spline;

import jonl.aui.Icon;
import jonl.ge.editor.AbstractSubEditorTool;
import jonl.ge.editor.UI;
import jonl.vmath.Color;

public class SplineEditorTool extends AbstractSubEditorTool {

    Icon icon;
    
    SplineStoreTrait store;
    
    public SplineEditorTool() {
        super(
            "spline-editor",
            "Spline Editor",
            "Create and edit splines and curves",
            Color.GRAY);
        
        store = new SplineStoreTrait();
    }
    
    @Override
    public void init() {
        icon = UI.icon(pivot().ui(),this.getClass(),"/editor/spline_icon.png");
        
        pivot().setStoreTrait(this, store);
    }
    
    @Override
    public Icon icon() {
        return icon;
    }

    @Override
    public SplineEditor open() {
        return new SplineEditor(this,null);
    }

    @Override
    public SplineEditor open(Object store) {
        return new SplineEditor(this,store);
    }
}
