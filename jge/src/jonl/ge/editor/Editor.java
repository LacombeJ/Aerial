package jonl.ge.editor;

import java.io.File;
import java.net.URL;
import jonl.jutils.io.FileUtils;
import jonl.jutils.misc.SystemUtils;

public class Editor {


    public Editor() {
        
        
        
    }
    
    public void start() {
        
        init();
        
    }
    
    void init() {
        
        //TODO create module-node
        
        String appdataPath = SystemUtils.appDataLocation("JGEditor");
        
        File appdataFile = new File(appdataPath);
        
        if (!appdataFile.exists()) {
            boolean mkdir = appdataFile.mkdir();
            if (!mkdir) {
                throw new Error("Failed to create app data directory.");
            }
        }
        
        String configPath = appdataPath + File.separatorChar + "config.json";
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            URL configResourceURL = this.getClass().getResource("/editor/config.json");
            if (configResourceURL != null) {
                File configResourceFile = new File(configResourceURL.getFile());
                FileUtils.copy(configResourceFile, configFile);
            }
        }
        
        
        
    }
    

}
