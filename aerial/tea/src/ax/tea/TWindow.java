package ax.tea;

import java.awt.image.BufferedImage;

import ax.aui.HAlign;
import ax.aui.VAlign;
import ax.aui.Widget;
import ax.aui.Window;
import ax.graphics.Closer;
import ax.graphics.GL;
import ax.graphics.Input;
import ax.graphics.Loader;
import ax.tea.graphics.WidgetRenderer;

public class TWindow extends TWidget implements Window {

    protected TWidget widget;
    TRootPanel root;
    
    private TManager manager;
    protected TWindowManager windowManager;
    
    TWindow(TLayout layout) {
        super();
        manager = new TManager();
        windowManager = new TWindowManager(this);
        
        root = new TRootPanel();
        layout.add(root);
        setWidgetLayout(layout);
    }
    
    TWindow() {
        this(new TWindowLayout());
    }
    
    @Override
    protected TManager _root_manager() {
        return manager;
    }
    
    @Override
    protected TRootPanel _root_panel() {
        return root;
    }
    
    @Override
    protected void paint(TGraphics g) {
        WidgetRenderer.paint(this,"Window",g,info());
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    public void create() {
        windowManager.create();
    }

    @Override
    public TWidget widget() {
        return widget;
    }

    @Override
    public void setWidget(Widget widget) {
        if (this.widget!=null) {
            widgetLayout().remove(this.widget);
        }
        this.widget = (TWidget) widget;
        widgetLayout().add(widget);
    }

    @Override
    public int windowX() {
        return 0;
    }

    @Override
    public int windowY() {
        return 0;
    }
    
    @Override
    public String title() {
        return windowManager.title();
    }

    @Override
    public void setTitle(String title) {
        windowManager.setTitle(title);
    }

    @Override
    public void setPosition(HAlign halign, VAlign valign) {
        windowManager.setPosition(halign, valign);
    }

    @Override
    public void setX(int x) {
        windowManager.setX(x);
    }

    @Override
    public void setY(int y) {
        windowManager.setY(y);
    }

    @Override
    public void setWidth(int width) {
        windowManager.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        windowManager.setHeight(height);
    }

    @Override
    public void setVisible(boolean visible) {
        windowManager.setVisible(visible);
    }

    @Override
    public void setResizable(boolean resizable) {
        windowManager.setResizable(resizable);
    }
    
    @Override
    public void setDecorated(boolean decorated) {
        windowManager.setDecorated(decorated);
    }
    
    @Override
    public void setFloating(boolean floating) {
        windowManager.setFloating(floating);
    }
    
    public ax.graphics.Window window() {
        return windowManager.window();
    }
    
    public GL gl() {
        return windowManager.getGL();
    }
    
    public Input input() {
        return windowManager.input();
    }
    
    public Input handledInput() {
        return windowManager.handledInput();
    }
    
    public void addLoader(Loader loader) {
        windowManager.addLoader(loader);
    }
    
    public void addCloser(Closer closer) {
        windowManager.addCloser(closer);
    }
    
    @Override
    public void setCursor(TCursor cursor) {
        windowManager.setCursor(cursor);
    }
    
    @Override
    public void maximize() {
        windowManager.window().maximize();
    }
    
    @Override
    public void minimize() {
        windowManager.window().minimize();
    }
    
    @Override
    public void restore() {
        windowManager.window().restore();
    }
    
    @Override
    public void setIcon(BufferedImage image) {
        windowManager.setIcon(image);
    }
    
}
