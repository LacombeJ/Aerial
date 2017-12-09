package jonl.aui;

public interface TreeItem {

    TreeItem getParent();
    
    String getText();
    
    void setText(String text);
    
    TreeItem getItem(int i);
    
    void addItem(TreeItem item);
    
    void removeItem(TreeItem item);
    
    TreeItem removeItem(int i);
    
    int getItemCount();
    
    TreeItem[] getItems();
    
    void focus();
    
    void expand();
    
    void collapse();
    
    void expandRecursive();
    
    void collapseRecursive();
    
    boolean isExpanded();
    
}
