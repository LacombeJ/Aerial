package jonl.aui.logic;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.TreeItem;

public abstract class ATreeItem implements TreeItem {

    ATreeItem parent = null;
    
    String text = "TreeItem";
    
    boolean expanded = false;
    
    List<ATreeItem> items = new ArrayList<>();
    
    @Override
    public ATreeItem getParent() {
        return parent;
    }
    
    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public ATreeItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public void addItem(TreeItem item) {
        ATreeItem sti = (ATreeItem)item;
        if (sti.parent!=null) {
            sti.parent.removeItem(sti);
        }
        items.add(sti);
    }

    @Override
    public void removeItem(TreeItem item) {
        if (items.remove(item)) {
            ATreeItem sti = (ATreeItem)item;
            sti.parent = null;
        }
    }

    @Override
    public ATreeItem removeItem(int i) {
        ATreeItem sti = (ATreeItem) items.remove(i);
        if (sti!=null) {
            sti.parent = null;
        }
        return sti;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public TreeItem[] getItems() {
        return items.toArray(new TreeItem[0]);
    }

    void expandUp() {
        expand();
        if (parent!=null) {
            parent.expand();
        }
    }
    
    @Override
    public void focus() {
        if (parent!=null) {
            parent.expandUp();
        }
    }

    @Override
    public void expand() {
        expanded = true;
    }

    @Override
    public void collapse() {
        expanded = false;
    }

    @Override
    public void expandRecursive() {
        expand();
        for (ATreeItem sti : items) {
            sti.expandRecursive();
        }
    }

    @Override
    public void collapseRecursive() {
       collapse();
       for (ATreeItem sti : items) {
           sti.collapseRecursive();
       }
    }
    
    @Override
    public boolean isExpanded() {
        return expanded;
    }

}
