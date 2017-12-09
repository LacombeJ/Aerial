package jonl.aui.logic;

public abstract class WindowEvent {
    
    public static class Move extends WindowEvent {
        int x, y;
        Move(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public static class Resize extends WindowEvent {
        int width, height;
        Resize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
    public static class SetVisible extends WindowEvent {
        boolean visible;
        SetVisible(boolean visible) {
            this.visible = visible;
        }
    }
    
    public static class SetResizable extends WindowEvent {
        boolean resizable;
        SetResizable(boolean resizable) {
            this.resizable = resizable;
        }
    }
    
}
