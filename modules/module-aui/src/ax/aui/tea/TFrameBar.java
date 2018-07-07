package ax.aui.tea;

import ax.aui.Align;
import ax.aui.Margin;
import ax.aui.tea.event.TMouseEvent;
import ax.aui.tea.graphics.ButtonRenderer;
import ax.aui.tea.graphics.FrameBarRenderer;
import ax.graphics.Input;
import ax.graphics.Window;
import ax.math.vector.Mathi;

public class TFrameBar extends TWidget {

    private TFrame frame;
    
    private int buttonWidth = 40;
    private int buttonHeight = 25;
    
    private int adjustX = 0;
    private int adjustY = 0;
    private boolean inAdjustState = false;
    
    public TFrameBar(TFrame frame) {
        super();
        this.frame = frame;
        setMouseFocusSupport(true);
        
        TListLayout layout = new TListLayout(Align.HORIZONTAL);
        layout.setMargin(0,0,0,0);
        layout.setSpacing(0);
        
        layout.add(new TSpacer());
        
        TFrameButton minimize = new TFrameButton("Frame.Minimize.Button");
        minimize.setSizeConstraint(buttonWidth, buttonHeight);
        minimize.clicked().connect(()->{
            frame.window().minimize();
        });
        layout.add(minimize);
        
        TFrameButton maximize = new TFrameButton("Frame.Maximize.Button");
        maximize.setSizeConstraint(buttonWidth, buttonHeight);
        maximize.clicked().connect(()->{
            if (frame.window().getAttribute(Window.MAXIMIZED)) {
                frame.insets = new Margin(frame.defaultInsets);
                frame.window().restore();
            } else {
                frame.insets = new Margin(0,0,frame.insets.top,0);
                frame.window().maximize();
            }
            
        });
        layout.add(maximize);
        
        TFrameButton close = new TFrameButton("Frame.Close.Button");
        close.setSizeConstraint(buttonWidth, buttonHeight);
        close.clicked().connect(()->{
            frame.window().close();
        });
        layout.add(close);
        
        setWidgetLayout(layout);
    }
    
    public TFrame frame() {
        return frame;
    }
    
    @Override
    public void paint(TGraphics g) {
        FrameBarRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = true;
            adjustX = event.globalX + frame.windowManager.window().getX();
            adjustY = event.globalY + frame.windowManager.window().getY();
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = false;
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseMove(TMouseEvent event) {
        if (inAdjustState) {
            
            // Grab the screen mouse x early because window position might change
            // when moving out of maximize
            int screenMouseX = event.globalX + frame.windowManager.window().getX();
            int screenMouseY = event.globalY + frame.windowManager.window().getY();
            
            // Moving window while maximizes unmaximizes it (since this is not handle by the OS like other windows)
            // Only option is to diable moving maximized windows
            if (frame.windowManager.window().getAttribute(Window.MAXIMIZED)) {
                
                // Move frame so that mouse will hover over the center of the frame bar
                // This happens before we adjust the size of the framebar widget so we will use the
                // dimensions of the window for calculation
                int prevX = frame.windowManager.window().getX();
                int prevY = frame.windowManager.window().getY();
                frame.insets = new Margin(frame.defaultInsets); // restore insets
                frame.windowManager.window().restore();
                
                // Using prevX and prevY in calculations to handle multiple screens
                // Since we know in fullscreen mode, prevX and prevY denote the top left corner,
                // we can handle multiple screens using those values
                
                // This does not however, handle multiple screens of different sizes
                // TODO find a way to retrieve the size of the screen that contains the window
                
                int screenWidth = frame.windowManager.window().getScreenWidth();
                int width = frame.windowManager.window().getWidth();
                int left = prevX; // Using this instead of 0 to handle multiple screens
                int right = prevX + screenWidth - width;
                
                int moveX = screenMouseX - width/2;
                moveX = Mathi.clamp(moveX, left, right);
                int moveY = prevY; // Keep takbar same height
                
                frame.windowManager.window().setPosition(moveX, moveY);
            }
            
            // Adjust window position based on mouse motion
            
            int dx = screenMouseX - adjustX;
            int dy = screenMouseY - adjustY;
            adjustX = screenMouseX;
            adjustY = screenMouseY;
            int nx = frame.windowManager.window().getX() + dx;
            int ny = frame.windowManager.window().getY() + dy;
            
            frame.windowManager.window().setPosition(nx, ny);
            
            return true;
        }
        return false;
    }
    
    static class TFrameButton extends TButton {
        private String type;
        TFrameButton(String type) {
            this.type = type;
        }
        @Override
        protected void paint(TGraphics g) {
            ButtonRenderer.paint(this,type,g,info());
            paint().emit(cb->cb.f(g));
        }
    }
    
}
