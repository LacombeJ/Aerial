package jonl.aerial.spline;

import jonl.aerial.AbstractSubEditorTool;
import jonl.aerial.UI;
import jonl.aui.Icon;
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
