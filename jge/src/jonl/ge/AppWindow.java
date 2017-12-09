package jonl.ge;

class AppWindow extends AbstractWindow {

    private App app;
    
    AppWindow(App app) {
        this.app = app;
    }
    
    @Override
    public int width() {
        return app.getWidth();
    }
    
    @Override
    public int height() {
        return app.getHeight();
    }
    
    @Override
    public void close() {
        app.close();
    }
    
}
