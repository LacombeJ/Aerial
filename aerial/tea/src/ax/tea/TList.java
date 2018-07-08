package ax.tea;

import java.util.ArrayList;

import ax.aui.Align;
import ax.aui.List;
import ax.aui.Margin;
import ax.aui.Widget;

public class TList extends TWidget implements List {

    TPanel listPanel;
    TListLayout listLayout;
    
    public TList(Align align, int type) {
        super();
        
        boolean lockWidth = align == Align.VERTICAL;
        boolean lockHeight = align == Align.HORIZONTAL;
        TScrollPanel scroll = new TScrollPanel(lockWidth,lockHeight);
        
        listLayout = new TListLayout(align);
        listLayout.setMargin(0,0,0,0);
        listLayout.setSpacing(0);
        listLayout.setStack(true);
        
        listPanel = new TPanel(listLayout);
        
        TFillLayout fill = new TFillLayout();
        scroll.setWidget(listPanel);
        fill.add(scroll);
        setWidgetLayout(fill);
        
    }
    
    public TList(Align align) {
        this(align, List.SCROLL_BAR);
    }
    
    public TList() {
        this(Align.VERTICAL);
    }

    @Override
    public Align align() {
        return listLayout.align();
    }

    @Override
    public void setAlign(Align align) {
        listLayout.setAlign(align);
    }

    @Override
    public int scrollType() {
        // TODO
        return 0;
    }

    @Override
    public void setScrollType(int scrollType) {
        // TODO
    }

    @Override
    public Widget getWidget(int index) {
        return listLayout.getWidget(index);
    }

    @Override
    public void add(Widget widget) {
        listLayout.add(widget);
    }

    @Override
    public void remove(Widget widget) {
        listLayout.remove(widget);
    }

    @Override
    public void remove(int index) {
        listLayout.remove(index);
    }

    @Override
    public void removeAll() {
        listLayout.removeAll();
    }

    @Override
    public int indexOf(Widget widget) {
        return listLayout.indexOf(widget);
    }

    @Override
    public int count() {
        return listLayout.count();
    }

    @Override
    public ArrayList<Widget> widgets() {
        return listLayout.widgets();
    }

    @Override
    public boolean isEmpty() {
        return listLayout.isEmpty();
    }

    @Override
    public boolean contains(Widget widget) {
        return listLayout.contains(widget);
    }

    @Override
    public Margin margin() {
        return listLayout.margin();
    }

    @Override
    public void setMargin(Margin margin) {
        listLayout.setMargin(margin);
    }

    @Override
    public void setMargin(int left, int right, int top, int bottom) {
        listLayout.setMargin(left,right,top,bottom);
    }

    @Override
    public int spacing() {
        return listLayout.spacing;
    }

    @Override
    public void setSpacing(int spacing) {
        listLayout.setSpacing(spacing);
    }
    
    
}
