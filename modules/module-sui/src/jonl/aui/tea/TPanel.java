package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Layout;
import jonl.aui.Panel;
import jonl.aui.Widget;

public class TPanel extends TWidget implements Panel {

    public TPanel() {
        super();
    }
    
    public TPanel(Layout layout) {
        this();
        setLayout(layout);
    }
    
    @Override
    public Layout layout() {
        return widgetLayout();
    }

    @Override
    public void setLayout(Layout layout) {
        setWidgetLayout((TLayout) layout);
    }

    @Override
    public Widget get(int index) {
        return layout().getWidget(index);
    }

    @Override
    public void add(Widget widget) {
        layout().add(widget);
    }

    @Override
    public void remove(Widget widget) {
        layout().remove(widget);
    }

    @Override
    public void removeAll() {
        layout().removeAll();
    }

    @Override
    public ArrayList<Widget> widgets() {
        return layout().widgets();
    }
    
}
