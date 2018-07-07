package jonl.aerial.editor;

import jonl.vmath.Color;

public class SceneEditorTool extends AbstractSubEditorTool {

    public SceneEditorTool() {
        super(
            "scene-editor",
            "Scene Editor",
            "Create 3d scenes",
            Color.DARK_GRAY);
    }
    
    @Override
    public void init() {
        
    }
    
    @Override
    public SceneEditor open() {
        return new SceneEditor(pivot().ui(),pivot().window(),null);
    }

    @Override
    public SceneEditor open(Object store) {
        return new SceneEditor(pivot().ui(),pivot().window(),store);
    }
    
}
