package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.HAlign;
import jonl.aui.Margin;
import jonl.aui.VAlign;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.TColor;
import jonl.jgl.Input;
import jonl.jgl.Window;

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
    protected void handleMouseButtonPress(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = true;
            adjustX = event.globalX + frame.manager.window().getX();
            adjustY = event.globalY + frame.manager.window().getY();
        }
    }
    
    @Override
    protected void handleMouseButtonRelease(TMouseEvent event) {
        if (event.button==Input.MB_LEFT) {
            inAdjustState = false;
        }
    }
    
    @Override
    protected void handleMouseMove(TMouseEvent event) {
        if (inAdjustState) {
            int mouseWindowX = event.globalX + frame.manager.window().getX();
            int mouseWindowY = event.globalY + frame.manager.window().getY();
            int dx = mouseWindowX - adjustX;
            int dy = mouseWindowY - adjustY;
            adjustX = event.globalX + frame.manager.window().getX();
            adjustY = event.globalY + frame.manager.window().getY();
            int nx = frame.manager.window().getX() + dx;
            int ny = frame.manager.window().getY() + dy;
            
            this.frame.setX(nx);
            this.frame.setY(ny);
        }
    }
    
}
