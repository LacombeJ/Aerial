package jonl.aerial.editor;

import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.UIManager;
import jonl.jutils.data.Dir;

public class ProjectExplorer {

    Editor editor;
    UIManager ui;
    
    Tree tree;
    
    public ProjectExplorer(Editor editor, UIManager ui) {
        this.editor = editor;
        this.ui = ui;
        
        
        tree = ui.tree();
        
    }
    
    Tree tree() {
        return tree;
    }
    
    void populate() {
        Dir dir = editor.project.dir;
        
        for (Dir subDir : dir.list()) {
            TreeItem sub = item(subDir);
            tree.addItem(sub);
        }
        
    }
    
    TreeItem item(Dir dir) {
        TreeItem item = ui.treeItem(dir.name());
        for (Dir subDir : dir.list()) {
            TreeItem sub = item(subDir);
            item.addItem(sub);
        }
        return item;
    }
    
    
    
    
    
    
}
