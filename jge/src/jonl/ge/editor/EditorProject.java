package jonl.ge.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jonl.jutils.data.Dir;
import jonl.jutils.data.Json;

public class EditorProject {

    Editor editor;
    Dir dir;
    
    Project project = new Project();
    
    Store store = new Store();
    
    Scenes scenes = new Scenes();
    HashMap<String,Scene> sceneMap = new HashMap<>();

    public EditorProject(Editor editor, Dir dir) {
        this.editor = editor;
        this.dir = dir;
    }
    
    public EditorProject(Editor editor, String path) {
        this(editor, new Dir(path));
    }
    
    boolean load() {
        if (dir.exists("project.json")) {
            // Load project
            project = dir.json("project.json").load(Project.class);
            
            // Load .edit
            Dir editDir = dir.dir(".edit");
            if (!editDir.exists()) {
                createEditDir();
            }
            Dir storeDir = editDir.dir("store");
            if (storeDir.exists("store.json")) {
                store = storeDir.json("store.json").load(Store.class);
            } else {
                storeDir.json("store.json").save(store);
            }
            
            // Load scenes
            Dir scenesDir = dir.dir("scenes");
            if (scenesDir.exists()) {
                scenes = scenesDir.json("scenes.json").load(Scenes.class);
                
                for (String scenePath : scenes.scenePaths) {
                    Scene scene = scenesDir.json(scenePath).load(Scene.class);
                    sceneMap.put(scenePath,scene);
                }
            }
            return true;
        }
        return false;
    }
    
    void save() {
        
        // Save project
        dir.json("project.json").save(project);
        
        // Save edit directory
        Dir editDir = dir.dir(".edit");
        if (!editDir.exists()) {
            createEditDir();
        }
        
        editDir.dir("store").json("store.json").save(store);
        
        // Save scenes
        Dir scenesDir = dir.child("scenes");
        
        scenesDir.json("scenes.json").save(scenes);
        
        for (Entry<String,Scene> sceneEntry : sceneMap.entrySet()) {
            scenesDir.json(sceneEntry.getKey()).save(sceneEntry.getValue());
        }
        
    }
    
    private void createEditDir() {
        Dir edit = dir.child(".edit");
        
        edit.child("store");
    }
    
    void openTool(SubEditorTool subEditorTool, SubEditor subEditor) {
        OpenTool open = new OpenTool();
        open.id = subEditorTool.id();
        open.storePath = dir.dir(".edit").dir("store").dir("tool"+store.openTools.size()+".json").path();
        editor.pivot.addStorePath(subEditorTool, subEditor, open.storePath);
        store.openTools.add(open);
        save();
    }
    
    
    
    static class Project {
        String name = "project";
        String path = "";
    }
    
    static class Store {
        ArrayList<OpenTool> openTools = new ArrayList<>();
    }
    
    static class Scenes {
        ArrayList<String> scenePaths = new ArrayList<>();;
    }
    
    static class Scene {
        String name = "scene";
        String path = "";
    }
    
    static class OpenTool {
        String id;
        String storePath;
    }
    
}
