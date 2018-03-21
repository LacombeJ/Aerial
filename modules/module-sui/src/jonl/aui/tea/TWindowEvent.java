package jonl.aui.tea;

abstract class TWindowEvent {
    
    static class Move extends TWindowEvent {
        int x, y;
        Move(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    static class Resize extends TWindowEvent {
        int width, height;
        Resize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
    static class SetVisible extends TWindowEvent {
        boolean visible;
        SetVisible(boolean visible) {
            this.visible = visible;
        }
    }
    
    static class SetResizable extends TWindowEvent {
        boolean resizable;
        SetResizable(boolean resizable) {
            this.resizable = resizable;
        }
    }
    
    static class SetDecorated extends TWindowEvent {
        boolean decorated;
        SetDecorated(boolean decorated) {
            this.decorated = decorated;
        }
    }
    
}
