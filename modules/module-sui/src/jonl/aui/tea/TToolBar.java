package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.ToolBar;
import jonl.aui.ToolButton;
import jonl.aui.tea.graphics.TWidgetInfo;
import jonl.jutils.func.List;

public class TToolBar extends TButtonBar implements ToolBar {
    
    public TToolBar() {
        super();
        widgetLayout().setMargin(4, 4, 4, 4);
        widgetLayout().setSpacing(4);
    }
    
    @Override
    public TToolButton get(int index) {
        return (TToolButton) super.get(index);
    }

    @Override
    public void add(ToolButton button) {
        super.add(button);
    }

    @Override
    public void remove(ToolButton button) {
        super.remove(button);
    }

    @Override
    public ArrayList<ToolButton> buttons() {
        return List.map(widgetLayout().widgets(), (w) -> (ToolButton)w);
    }
    
    @Override
    public void paint(TGraphics g) {
        style().toolBar().paint(this, TWidgetInfo.widget(), g);
        paint().emit((cb)->cb.f(g));
    }

}
