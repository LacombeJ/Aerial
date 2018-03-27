package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.HAlign;
import jonl.aui.Margin;
import jonl.aui.VAlign;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.TColor;
import jonl.jgl.Input;
import jonl.jgl.Window;
import jonl.vmath.Mathi;

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
        
        layout.add(new TSpacerItem());
        
        TButton minimize = new TButton("--");
        minimize.setMinSize(buttonWidth, buttonHeight);
        minimize.setMaxSize(buttonWidth, buttonHeight);
        minimize.info.put("cButton", frame.color);
        minimize.info.put("fMaxValue", 4f);
        minimize.clicked().connect(()->{
            frame.window().minimize();
        });
        layout.add(minimize);
        
        TButton maximize = new TButton("[]]");
        maximize.setMinSize(buttonWidth, buttonHeight);
        maximize.setMaxSize(buttonWidth, buttonHeight);
        maximize.info.put("cButton", frame.color);
        maximize.info.put("fMaxValue", 4f);
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
        
        TButton close = new TButton("X");
        close.setMinSize(buttonWidth, buttonHeight);
        close.setMaxSize(buttonWidth, buttonHeight);
        close.info.put("cButton", frame.color);
        close.info.put("cHover", TColor.RED);
        close.info.put("fMaxValue", 4f);
        close.clicked().connect(()->{
            frame.window().close();
        });
        layout.add(close);
        
        setWidgetLayout(layout);
    }
    
    @Override
    public void paint(TGraphics g) {
        g.renderRect(0,0,width(),height(),frame.color);
        g.renderText(frame.title(), width()/2f, height()/2f, HAlign.CENTER, VAlign.MIDDLE, style().font(), TColor.WHITE);
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = true;
            adjustX = event.globalX + frame.manager.window().getX();
            adjustY = event.globalY + frame.manager.window().getY();
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
            int screenMouseX = event.globalX + frame.manager.window().getX();
            int screenMouseY = event.globalY + frame.manager.window().getY();
            
            // Moving window while maximizes unmaximizes it (since this is not handle by the OS like other windows)
            // Only option is to diable moving maximized windows
            if (frame.manager.window().getAttribute(Window.MAXIMIZED)) {
                
                // Move frame so that mouse will hover over the center of the frame bar
                // This happens before we adjust the size of the framebar widget so we will use the
                // dimensions of the window for calculation
                int prevX = frame.manager.window().getX();
                int prevY = frame.manager.window().getY();
                frame.insets = new Margin(frame.defaultInsets); // restore insets
                frame.manager.window().restore();
                
                // Using prevX and prevY in calculations to handle multiple screens
                // Since we know in fullscreen mode, prevX and prevY denote the top left corner,
                // we can handle multiple screens using those values
                
                // This does not however, handle multiple screens of different sizes
                // TODO find a way to retrieve the size of the screen that contains the window
                
                int screenWidth = frame.manager.window().getScreenWidth();
                int width = frame.manager.window().getWidth();
                int left = prevX; // Using this instead of 0 to handle multiple screens
                int right = prevX + screenWidth - width;
                
                int moveX = screenMouseX - width/2;
                moveX = Mathi.clamp(moveX, left, right);
                int moveY = prevY; // Keep takbar same height
                
                frame.manager.window().setPosition(moveX, moveY);
            }
            
            // Adjust window position based on mouse motion
            
            int dx = screenMouseX - adjustX;
            int dy = screenMouseY - adjustY;
            adjustX = screenMouseX;
            adjustY = screenMouseY;
            int nx = frame.manager.window().getX() + dx;
            int ny = frame.manager.window().getY() + dy;
            
            frame.manager.window().setPosition(nx, ny);
            
            return true;
        }
        return false;
    }
    
}
