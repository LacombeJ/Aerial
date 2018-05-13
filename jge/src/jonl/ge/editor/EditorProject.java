package jonl.ge.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import jonl.jutils.data.Dir;

public class EditorProject {

    Editor editor;
    Dir dir;
    
    Project project = new Project();
    
    Scenes scenes = new Scenes();
    HashMap<String,Scene> sceneMap = new HashMap<>();
    
    ArrayList<SubEditor> subEditors = new ArrayList<>();
    
    public EditorProject(Editor editor, Dir dir) {
        this.editor = editor;
        this.dir = dir;
    }
    
    public EditorProject(Editor editor, String path) {
        this(editor, new Dir(path));
    }
    
    public boolean load() {
        if (dir.exists("project.json")) {
            // Load project
            project = dir.json("project.json").load(Project.class);
            
            // Load .edit
            Dir editDir = dir.dir(".edit");
            if (!editDir.exists()) {
                createEditDir();
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
    
    public void save() {
        
        // Save project
        dir.json("project.json").save(project);
        
        // Save edit directory
        Dir editDir = dir.dir(".edit");
        if (!editDir.exists()) {
            createEditDir();
        }
        
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
    
    static class Project {
        String name = "project";
        String path = "";
    }
    
    static class Scenes {
        ArrayList<String> scenePaths = new ArrayList<>();;
    }
    
    static class Scene {
        String name = "scene";
        String path = "";
    }
    
}
