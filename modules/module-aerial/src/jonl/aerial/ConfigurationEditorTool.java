package jonl.aerial;

import jonl.aui.Icon;
import jonl.vmath.Color;

public class ConfigurationEditorTool extends AbstractSubEditorTool {

    Icon icon;
    
    public ConfigurationEditorTool() {
        super(
            "config-editor",
            "Configuration Editor",
            "Edit project settings and configurations.",
            Color.LIGHT_GRAY);
    }
    
    @Override
    public void init() {
        icon = UI.icon(pivot().ui(),this.getClass(),"/editor/settings_icon.png");
    }
    
    @Override
    public Icon icon() {
        return icon;
    }
    
    @Override
    public ConfigurationEditor open() {
        return new ConfigurationEditor(pivot().ui(),pivot().window(),null);
    }

    @Override
    public ConfigurationEditor open(Object store) {
        return new ConfigurationEditor(pivot().ui(),pivot().window(),store);
    }
    
}
