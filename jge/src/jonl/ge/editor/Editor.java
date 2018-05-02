package jonl.ge.editor;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jonl.ge.editor.EditorConfiguration.EC10;
import jonl.jutils.io.Console;
import jonl.jutils.io.FileUtils;

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
        String configPath = "config.json";
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            InputStream in = getClass().getResourceAsStream("/editor/config.json");
            String configContent = FileUtils.readFromStream(in);
            FileUtils.writeToFile(configPath, configContent);
        }
        
        String configContent = FileUtils.readFromFile(configFile.getPath());
        
        JsonObject element = new JsonParser().parse(configContent).getAsJsonObject();
        String name = element.get("name").getAsString();
        String version = element.get("version").getAsString();
       
        if (name.equals("editor")) {
            EC10 config = null;
            
            //Current version
            if (version.equals("1.0")) {
                config = new Gson().fromJson(configContent, EC10.class);
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
    
}
