package jonl.aui.tea.graphics;

public class TStyle {

    private TColor primary = TColor.fromFloat(0.8f, 0.8f, 0.8f);
    private TColor secondary = TColor.fromFloat(0.7f, 0.7f, 0.71f);
    private TColor tertiary = TColor.fromFloat(0.6f, 0.6f, 0.61f);
    
    private TColor light = TColor.fromFloat(0.95f, 0.95f, 0.97f);
    private TColor dark = TColor.fromFloat(0.05f, 0.7f, 0.1f);
    
    private TColor textColor = TColor.BLACK;
    
    private TFont font = new TFont("Arial",TFont.PLAIN,16,false);
    
    private TButtonStyle button = new TButtonStyle(this);
    private TButtonStyle menuButton = new TButtonStyle(this);
    private TButtonStyle toolButton = new TButtonStyle(this);
    private TButtonStyle tabButton = new TButtonStyle(this);
    
    // ------------------------------------------------------------------------
    
    public TStyle() {
        
        menuButton.buttonColor(TColor.WHITE);
        tabButton.buttonColor(light);
        
    }
    
    // ------------------------------------------------------------------------
    
    // Getters
    
    public TColor primary() { return primary; }
    public TColor secondary() { return secondary; }
    public TColor tertiary() { return tertiary; }
    public TColor light() { return light; }
    public TColor dark() { return dark; }
    
    public TColor textColor() { return textColor; }
    
    public TFont font() { return font; }
    
    public TButtonStyle button() { return button; }
    public TButtonStyle menuButton() { return menuButton; }
    public TButtonStyle toolButton() { return toolButton; }
    public TButtonStyle tabButton() { return tabButton; }
    
    // ------------------------------------------------------------------------
    
    // Setters
    
    public void primary(TColor primary) { this.primary = primary; }
    public void secondary(TColor secondary) { this.secondary = secondary; }
    public void tertiary(TColor tertiary) { this.tertiary = tertiary; }
    public void light(TColor light) { this.light = light; }
    public void dark(TColor dark) { this.dark = dark; }
    
    public void textColor(TColor textColor) { this.textColor = textColor; }
    
    public void font(TFont font) { this.font = font; }
    
    public void button(TButtonStyle button) { this.button = button; }
    public void menuButton(TButtonStyle menuButton) { this.menuButton = menuButton; }
    public void toolButton(TButtonStyle toolButton)  { this.toolButton = toolButton; }
    public void tabButton(TButtonStyle tabButton)  { this.tabButton = tabButton; }
    
}
