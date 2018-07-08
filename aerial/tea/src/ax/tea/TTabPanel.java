package ax.tea;

import java.util.ArrayList;

import ax.aui.Align;
import ax.aui.Signal;
import ax.aui.TabPanel;
import ax.aui.Widget;
import ax.commons.func.Callback;
import ax.commons.func.Callback0D;
import ax.commons.func.Function;

public class TTabPanel extends TWidget implements TabPanel {

    private TTabBar tabBar;
    private TTabContent tabContent;
    
    private int index = 0;
    private ArrayList<String> labels;
    private ArrayList<TWidget> widgets;
    
    private boolean closeable = false;
    private Signal<Function<Widget,Boolean>> closed = new Signal<>();
    private Signal<Callback<Widget>> removed = new Signal<>();
    private Signal<Callback0D> newTab = new Signal<>();
    
    public TTabPanel() {
        super();
        
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
        
        tabBar.newTabButton.clicked().connect(()->{
            newTab().emit((cb)->cb.f());
        });
    }
    
    private void addWidgetContent(Widget widget) {
        tabContent.add(widget);
    }
    
    private void refreshContent() {
        if (index>=count()) {
            index = count()-1;
        }
        if (index<0) {
            index = 0;
        }
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
        } else {
            if (tabContent.count()>0) {
                tabContent.remove(0);
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
        if (closeable) {
            button.setCloseButton(true);
        }
        button.closeButton.clicked().connect(()->{
            boolean remove = true;
            for (Function<Widget,Boolean> f : closed().slots()) {
                if (!f.f(widget)) {
                    remove = false;
                }
            }
            if (remove) {
                remove(widget);
            }
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
        TWidget widget = widgets.remove(index);
        labels.remove(index);
        tabBar.remove(tabBar.get(index));
        refreshContent();
        removed().emit((cb)->cb.f(widget));
    }
    
    @Override
    public void removeAll() {
        for (int i=count()-1; i>=0; i--) {
            TWidget widget = widgets.remove(index);
            labels.remove(index);
            tabBar.remove(tabBar.get(index));
            removed().emit((cb)->cb.f(widget));
        }
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
    
    @Override
    public void setWidget(Widget widget) {
        setIndex(indexOf(widget));
    }
    
    @Override
    public void setCloseable(boolean enabled) {
        closeable = enabled;
    }
    
    @Override
    public void setAddable(boolean enabled) {
        if (enabled) {
            tabBar.addNewTabButton();
        } else {
            tabBar.removeNewTabButton();
        }
    }
    
    @Override
    public Signal<Function<Widget,Boolean>> closed() {
        return closed;
    }
    
    @Override
    public Signal<Callback<Widget>> removed() {
        return removed;
    }
    
    @Override
    public Signal<Callback0D> newTab() {
        return newTab;
    }

}
