package jonl.aui.tea.graphics;

import jonl.vmath.Color;

public class TStyleDefault implements TStyle {

    protected Color primary = Color.fromFloat(0.8f, 0.8f, 0.8f);
    protected Color secondary = Color.fromFloat(0.7f, 0.7f, 0.71f);
    protected Color tertiary = Color.fromFloat(0.6f, 0.6f, 0.61f);
    
    protected Color background = Color.WHITE;
    protected Color foreground = Color.fromFloat(0.1f, 0.1f, 0.15f);
    
    protected Color light = Color.fromFloat(0.95f, 0.95f, 0.97f);
    protected Color dark = Color.fromFloat(0.05f, 0.07f, 0.1f);
    
    protected Color textColor = Color.BLACK;
    
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
    
    public Color primary() { return primary; }
    public Color secondary() { return secondary; }
    public Color tertiary() { return tertiary; }
    
    public Color background() { return background; }
    public Color foreground() { return foreground; }
    
    public Color light() { return light; }
    public Color dark() { return dark; }
    
    public Color textColor() { return textColor; }
    
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
    
    public void primary(Color primary) { this.primary = primary; }
    public void secondary(Color secondary) { this.secondary = secondary; }
    public void tertiary(Color tertiary) { this.tertiary = tertiary; }
    
    public void background(Color background) { this.background = background; }
    public void foreground(Color foreground) { this.foreground = foreground; }
    
    public void light(Color light) { this.light = light; }
    public void dark(Color dark) { this.dark = dark; }
    
    public void textColor(Color textColor) { this.textColor = textColor; }
    
    public void font(TFont font) { this.font = font; }
    
    public void widget(TDefaultWidgetStyle widget) { this.widget = widget; }
    public void button(TButtonStyle button) { this.button = button; }
    public void menuButton(TButtonStyle menuButton) { this.menuButton = menuButton; }
    public void toolButton(TButtonStyle toolButton)  { this.toolButton = toolButton; }
    public void tabButton(TButtonStyle tabButton)  { this.tabButton = tabButton; }
    public void dial(TDialStyle dial) { this.dial = dial; }
    public void toolBar(TToolBarStyle toolBar) { this.toolBar = toolBar; }
    
}
