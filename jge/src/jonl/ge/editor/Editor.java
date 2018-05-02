package jonl.ge.editor;

import java.util.ArrayList;

import jonl.ge.editor.EditorConfiguration.EC10;
import jonl.jutils.data.Json;
import jonl.jutils.io.Console;

public class Editor {

    String name;
    String version;
    
    EditorGUI gui;
    
    ArrayList<EditorPlugin> plugins;

    public Editor() {
        plugins = new ArrayList<>();
        
        
    }
    
    public void add(EditorPlugin plugin) {
        plugins.add(plugin);
    }
    
    public void start() {
        init();
        initUi();
    }
    
    void init() {
        Json json = new Json("config.json");
        if (!json.exists()) {
            EC10 config = defaultConfig();
            json.save(config);
        }
        
        BasicConfig bc = json.load(BasicConfig.class);
        
        if (bc.name.equals("editor")) {
            EC10 config = null;
            
            //Current version
            if (bc.version.equals("1.0")) {
                config = json.load(EC10.class);
            } else {
                Console.log("Version not supported.");
            }
            
            this.name = config.name;
            this.version = config.version;
        }
    }
    
    void initUi() {
        gui = new EditorGUI(this);
        gui.create();
    }
    
    private static class BasicConfig {
        String name;
        String version;
    }
    
    private EC10 defaultConfig() {
        EC10 config = new EC10();
        config.name = "editor";
        config.version = "1.0";
        return config;
    }
    
}
