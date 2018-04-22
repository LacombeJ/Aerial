package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Align;
import jonl.aui.TreeItem;
import jonl.aui.tea.graphics.ButtonRenderer;

public class TTreeItem implements TreeItem {

    TTreeItem parent = null;
    
    String text;
    
    boolean expanded = false;
    
    ArrayList<TTreeItem> items = new ArrayList<>();
    
    private TreeItemWidget widget;
    
    public TTreeItem(String text) {
        this.text = text;
        widget = new TreeItemWidget();
    }
    
    public TTreeItem() {
        this("TreeItem");
    }
    
    TreeItemWidget widget() {
        return widget;
    }
    
    @Override
    public TTreeItem getParent() {
        return parent;
    }
    
    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        widget.invalidateSizeHint();
    }
    
    @Override
    public TTreeItem getItem(int i) {
        return items.get(i);
    }

    @Override
    public void addItem(TreeItem item) {
        TTreeItem sti = (TTreeItem)item;
        if (sti.parent!=null) {
            sti.parent.removeItem(sti);
        }
        items.add(sti);
        widget.invalidateSizeHint();
    }

    @Override
    public void removeItem(TreeItem item) {
        if (items.remove(item)) {
            TTreeItem sti = (TTreeItem)item;
            sti.parent = null;
            widget.invalidateSizeHint();
        }
    }

    @Override
    public TTreeItem removeItem(int i) {
        TTreeItem sti = (TTreeItem) items.remove(i);
        if (sti!=null) {
            sti.parent = null;
        }
        widget.invalidateSizeHint();
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
            parent.expandUp();
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
        widget.invalidateSizeHint();
    }

    @Override
    public void collapse() {
        expanded = false;
        widget.invalidateSizeHint();
    }

    @Override
    public void expandRecursive() {
        expand();
        for (TTreeItem sti : items) {
            sti.expandRecursive();
        }
    }

    @Override
    public void collapseRecursive() {
       collapse();
       for (TTreeItem sti : items) {
           sti.collapseRecursive();
       }
    }
    
    @Override
    public boolean isExpanded() {
        return expanded;
    }
    
    // The widget is separated to avoid inheritance conflicts
    class TreeItemWidget extends TWidget {
        
        int border = 4;
        
        TButton expandButton;
        TButton itemButton;
        
        TreeItemWidget() {
            super();
            TListLayout layout = new TListLayout(Align.HORIZONTAL);
            setWidgetLayout(layout);
            
            layout.setMargin(0,0,0,0);
            layout.setSpacing(0);
            
            expandButton = new TreeDropButton();
            expandButton.setIcon(TIcon.CARET);
            
            itemButton = new TreeItemButton();
            itemButton.setText(text);
            
            layout.add(expandButton);
            layout.add(itemButton);
            
            expandButton.clicked().connect(()->{
                if (isExpanded()) {
                    collapse();
                } else {
                    expand();
                }
            });
            
        }
        
        class TreeItemButton extends TButton {
            @Override
            protected void paint(TGraphics g) {
                ButtonRenderer.paint(this,"Tree.Item",g,info());
                paint().emit(cb->cb.f(g));
            }
        }
        
        class TreeDropButton extends TButton {
            @Override
            protected void paint(TGraphics g) {
                ButtonRenderer.paint(this,"Tree.Drop",g,info());
                paint().emit(cb->cb.f(g));
            }
        }
        
    }

}
