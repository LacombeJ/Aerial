package jonl.ge.editor;

import jonl.aui.UIManager;

public class SceneEditorTool implements SubEditorTool {

    @Override
    public String id() {
        return "scene-editor";
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
