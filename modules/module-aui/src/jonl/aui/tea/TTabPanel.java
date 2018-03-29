package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Align;
import jonl.aui.TabPanel;
import jonl.aui.Widget;

public class TTabPanel extends TWidget implements TabPanel {

    private TTabBar tabBar;
    private TTabContent tabContent;
    
    private int index = 0;
    private ArrayList<String> labels;
    private ArrayList<TWidget> widgets;
    
    public TTabPanel() {
        tabBar = new TTabBar();
        tabContent = new TTabContent();
        
        labels = new ArrayList<>();
        widgets = new ArrayList<>();
        
        TListLayout tabPanelLayout = new TListLayout(Align.VERTICAL);
        tabPanelLayout.setMargin(0,0,0,0);
        tabPanelLayout.setSpacing(0);
        
        tabPanelLayout.add(tabBar);
        tabPanelLayout.add(tabContent);
        
        setWidgetLayout(tabPanelLayout);
    }
    
    private void addWidgetContent(Widget widget) {
        tabContent.add(widget);
        
    }
    
    private void refreshContent() {
        if (index < count()) {
            tabBar.get(index).setChecked(true);
            TWidget nextWidget = widgets.get(index);
            if (tabContent.count()!=0) {
                TWidget prevWidget = tabContent.getChild(0);
                if (prevWidget != nextWidget) {
                    tabContent.remove(0);
                    addWidgetContent(nextWidget);
                }
            } else {
                addWidgetContent(nextWidget);
            }
        }
    }
    
    @Override
    public TWidget get(int index) {
        return widgets.get(index);
    }

    @Override
    public void add(Widget widget, String label) {
        widgets.add((TWidget) widget);
        labels.add(label);
        TTabButton button = new TTabButton(label);
        button.clicked().connect(()->{
            setIndex(indexOf(widget));
        });
        tabBar.add(button);
        refreshContent();
    }

    @Override
    public void remove(Widget widget) {
        int i = indexOf(widget);
        if (i!=-1) {
            remove(i);
        }
    }

    @Override
    public void remove(int index) {
        widgets.remove(index);
        labels.remove(index);
        refreshContent();
    }

    @Override
    public int count() {
        return widgets.size();
    }

    @Override
    public int indexOf(Widget widget) {
        return widgets.indexOf(widget);
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
        refreshContent();
    }

}
