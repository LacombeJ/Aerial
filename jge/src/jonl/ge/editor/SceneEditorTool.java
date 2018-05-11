package jonl.ge.editor;

import jonl.aui.Icon;
import jonl.aui.UIManager;
import jonl.vmath.Color;

public class SceneEditorTool implements SubEditorTool {

    @Override
    public String id() {
        return "scene-editor";
    }

    @Override
    public String name() {
        return "Scene Editor";
    }

    @Override
    public String description() {
        return "Create 3d scenes";
    }

    @Override
    public Color color() {
        return Color.DARK_GRAY;
    }
    
    @Override
    public Icon icon(UIManager ui) {
        return null;
    }
    
    @Override
    public SubEditor open(UIManager ui) {
        return new SceneEditor(ui,null);
    }

    @Override
    public SubEditor open(UIManager ui, String content) {
        return new SceneEditor(ui,content);
    }

}
