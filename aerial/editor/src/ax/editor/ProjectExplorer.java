package ax.editor;

import ax.aui.Tree;
import ax.aui.TreeItem;
import ax.aui.UIManager;
import ax.commons.data.Dir;

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
