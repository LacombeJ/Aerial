package ax.aui;

public interface Tree extends Widget {

    TreeItem getItem(int i);
    
    void addItem(TreeItem item);
    
    void removeItem(TreeItem item);
    
    TreeItem removeItem(int i);
    
    int getItemCount();
    
    TreeItem[] getItems();
    
}
