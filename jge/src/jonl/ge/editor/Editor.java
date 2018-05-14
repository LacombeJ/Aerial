package jonl.ge.editor;

import java.util.ArrayList;
import jonl.ge.editor.EditorConfiguration.EC10;
import jonl.ge.editor.spline.SplineEditorTool;
import jonl.jutils.data.Cereal;
import jonl.jutils.data.Dir;
import jonl.jutils.data.Json;
import jonl.jutils.io.Console;

public class Editor {

    //TODO decouple classes for clarity and define and provide an interface to expose functionality
    //between editor hierarchy instead of making all variable package-private
    
    Dir dir = Dir.current();
    EC10 config = new EC10();
    
    EditorUI gui;
    EditorProject project;
    
    Pivot pivot;
    
    ArrayList<SubEditorTool> subEditorTools;

    public Editor() {
        subEditorTools = new ArrayList<>();
        
        add(new SceneEditorTool());
        add(new SplineEditorTool());
    }
    
    public void add(SubEditorTool sub) {
        subEditorTools.add(sub);
    }
    
    public void start() {
        init();
        initUi();
    }
    
    void init() {
        Json json = dir.json("config.json");
        if (!json.exists()) {
            EC10 def = new EC10();
            json.save(def);
        }
        
        BasicConfig bc = json.load(BasicConfig.class);
        
        if (bc.name.equals("editor")) {
            EC10 loaded = null;
            
            //Current version
            if (bc.version.equals("1.0")) {
                loaded = json.load(EC10.class);
            } else {
                Console.log("Version not supported.");
            }
            
            config = Cereal.copy(loaded);
        }
    }
    
    void save() {
        Json json = dir.json("config.json");
        json.save(config);
    }
    
    void initUi() {
        gui = new EditorUI(this);
        gui.create();
    }
    
    SubEditorTool tool(String id) {
        for (SubEditorTool tool : subEditorTools) {
            if (tool.id().equals(id)) {
                return tool;
            }
        }
        return null;
    }
    
    private static class BasicConfig {
        String name;
        String version;
    }
    
}
