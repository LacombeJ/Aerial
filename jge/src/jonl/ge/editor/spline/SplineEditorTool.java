package jonl.ge.editor.spline;

import jonl.aui.Icon;
import jonl.aui.UIManager;
import jonl.ge.editor.SubEditorTool;
import jonl.ge.editor.UI;
import jonl.vmath.Color;

public class SplineEditorTool implements SubEditorTool {

    @Override
    public String id() {
        return "spline-editor";
    }
    
    @Override
    public String name() {
        return "Spline Editor";
    }

    @Override
    public String description() {
        return "Create and edit splines and curves";
    }

    @Override
    public Color color() {
        return Color.GRAY;
    }
    
    @Override
    public Icon icon(UIManager ui) {
        return UI.icon(ui,this.getClass(),"/editor/spline_icon.png");
    }
    

    @Override
    public SplineEditor open(UIManager ui) {
        return new SplineEditor(ui,null);
    }

    @Override
    public SplineEditor open(UIManager ui, String content) {
        return new SplineEditor(ui,content);
    }

}
