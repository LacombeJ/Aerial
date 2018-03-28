package jonl.aui.tea.graphics;

public class TStyleDefault implements TStyle {

    protected TColor primary = TColor.fromFloat(0.8f, 0.8f, 0.8f);
    protected TColor secondary = TColor.fromFloat(0.7f, 0.7f, 0.71f);
    protected TColor tertiary = TColor.fromFloat(0.6f, 0.6f, 0.61f);
    
    protected TColor background = TColor.WHITE;
    protected TColor foreground = TColor.fromFloat(0.1f, 0.1f, 0.15f);
    
    protected TColor light = TColor.fromFloat(0.95f, 0.95f, 0.97f);
    protected TColor dark = TColor.fromFloat(0.05f, 0.07f, 0.1f);
    
    protected TColor textColor = TColor.BLACK;
    
    protected TFont font = new TFont("Calibri",TFont.PLAIN,15,false);
    
    protected TDefaultWidgetStyle widget = new TDefaultWidgetStyle(this);
    protected TButtonStyle button = new TButtonStyle(this);
    protected TButtonStyle menuButton = new TButtonStyle(this);
    protected TButtonStyle toolButton = new TButtonStyle(this);
    protected TButtonStyle tabButton = new TButtonStyle(this);
    protected TDialStyle dial = new TDialStyle(this);
    protected TToolBarStyle toolBar = new TToolBarStyle(this);
    
    // ------------------------------------------------------------------------
    
    public TStyleDefault() {
        
        menuButton.buttonColor(background);
        tabButton.buttonColor(light);
        
    }
    
    // ------------------------------------------------------------------------
    
    // Getters
    
    public TColor primary() { return primary; }
    public TColor secondary() { return secondary; }
    public TColor tertiary() { return tertiary; }
    
    public TColor background() { return background; }
    public TColor foreground() { return foreground; }
    
    public TColor light() { return light; }
    public TColor dark() { return dark; }
    
    public TColor textColor() { return textColor; }
    
    public TFont font() { return font; }
    
    public TDefaultWidgetStyle widget() { return widget; }
    public TButtonStyle button() { return button; }
    public TButtonStyle menuButton() { return menuButton; }
    public TButtonStyle toolButton() { return toolButton; }
    public TButtonStyle tabButton() { return tabButton; }
    public TDialStyle dial() { return dial; }
    public TToolBarStyle toolBar() { return toolBar; }
    
    // ------------------------------------------------------------------------
    
    // Setters
    
    public void primary(TColor primary) { this.primary = primary; }
    public void secondary(TColor secondary) { this.secondary = secondary; }
    public void tertiary(TColor tertiary) { this.tertiary = tertiary; }
    
    public void background(TColor background) { this.background = background; }
    public void foreground(TColor foreground) { this.foreground = foreground; }
    
    public void light(TColor light) { this.light = light; }
    public void dark(TColor dark) { this.dark = dark; }
    
    public void textColor(TColor textColor) { this.textColor = textColor; }
    
    public void font(TFont font) { this.font = font; }
    
    public void widget(TDefaultWidgetStyle widget) { this.widget = widget; }
    public void button(TButtonStyle button) { this.button = button; }
    public void menuButton(TButtonStyle menuButton) { this.menuButton = menuButton; }
    public void toolButton(TButtonStyle toolButton)  { this.toolButton = toolButton; }
    public void tabButton(TButtonStyle tabButton)  { this.tabButton = tabButton; }
    public void dial(TDialStyle dial) { this.dial = dial; }
    public void toolBar(TToolBarStyle toolBar) { this.toolBar = toolBar; }
    
}
