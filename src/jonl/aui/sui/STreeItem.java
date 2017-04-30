package jonl.aui.sui;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.TreeItem;

public class STreeItem implements TreeItem {

    STreeItem parent = null;
    
    String text = "TreeItem";
    
    boolean expanded = false;
    
    List<STreeItem> items = new ArrayList<>();
    
    STreeItem() {
        
    }
    
    STreeItem(String text) {
        setText(text);
    }
    
    @Override
    public STreeItem getParent() {
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
    public STreeItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public void addItem(TreeItem item) {
        STreeItem sti = (STreeItem)item;
        if (sti.parent!=null) {
            sti.parent.removeItem(sti);
        }
        items.add(sti);
    }

    @Override
    public void removeItem(TreeItem item) {
        if (items.remove(item)) {
            STreeItem sti = (STreeItem)item;
            sti.parent = null;
        }
    }

    @Override
    public STreeItem removeItem(int i) {
        STreeItem sti = (STreeItem) items.remove(i);
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
        for (STreeItem sti : items) {
            sti.expandRecursive();
        }
    }

    @Override
    public void collapseRecursive() {
       collapse();
       for (STreeItem sti : items) {
           sti.collapseRecursive();
       }
    }
    
    @Override
    public boolean isExpanded() {
        return expanded;
    }

}
