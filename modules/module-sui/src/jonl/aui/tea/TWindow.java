package jonl.aui.tea;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.jgl.Closer;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Input;
import jonl.jgl.Loader;

public class TWindow extends TWidget implements Window {

    private Widget widget;
    
    TWindowManager manager;
    
    TWindow() {
        manager = new TWindowManager(this);
        setWidgetLayout(new TFillLayout());
    }
    
    @Override
    public void create() {
        manager.create();
    }

    @Override
    public Widget widget() {
        return widget;
    }

    @Override
    public void setWidget(Widget widget) {
        this.widget = widget;
        widgetLayout().set(widget);
    }

    @Override
    public String title() {
        return manager.title();
    }

    @Override
    public void setTitle(String title) {
        manager.setTitle(title);
    }

    @Override
    public void setPosition(HAlign halign, VAlign valign) {
        manager.setPosition(halign, valign);
    }

    @Override
    public void setX(int x) {
        manager.setX(x);
    }

    @Override
    public void setY(int y) {
        manager.setY(y);
    }

    @Override
    public void setWidth(int width) {
        manager.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        manager.setHeight(height);
    }

    @Override
    public void setVisible(boolean visible) {
        manager.setVisible(visible);
    }

    @Override
    public void setResizable(boolean resizable) {
        manager.setResizable(resizable);
    }
    
    public jonl.jgl.Window window() {
        return manager.window();
    }
    
    public GraphicsLibrary gl() {
        return manager.getGL();
    }
    
    public Input input() {
        return manager.input();
    }
    
    public void setLoader(Loader loader) {
        manager.setLoader(loader);
    }
    
    public void setCloser(Closer closer) {
        manager.setCloser(closer);
    }
    
}
