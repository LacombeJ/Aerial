package jonl.aerial;

import jonl.aui.Icon;
import jonl.vmath.Color;

public abstract class AbstractSubEditorTool implements SubEditorTool {

    private final String id;
    private final String name;
    private final String description;
    private final Color color;
    
    private Pivot pivot;
    
    public AbstractSubEditorTool(String id, String name, String description, Color color) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
    }
    
    public Pivot pivot() {
        return pivot;
    }
    
    public abstract void init();
    
    @Override
    public void init(Pivot pivot) {
        this.pivot = pivot;
        init();
    }
    
    @Override
    public String id() {
        return id;
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public String description() {
        return description;
    }
    
    @Override
    public Color color() {
        return color;
    }
    
    @Override
    public Icon icon() {
        return null;
    }
    
}
