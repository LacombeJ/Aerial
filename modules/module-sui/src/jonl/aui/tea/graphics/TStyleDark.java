package jonl.aui.tea.graphics;

public class TStyleDark extends TStyleDefault {
    
    public TStyleDark() {
        
        primary = TColor.fromFloat(0.2f, 0.2f, 0.2f);
        secondary = TColor.fromFloat(0.3f, 0.3f, 0.31f);
        tertiary = TColor.fromFloat(0.4f, 0.4f, 0.41f);
        
        background = TColor.fromFloat(0.1f, 0.1f, 0.1f);
        foreground = TColor.fromFloat(0.9f, 0.9f, 0.9f);
        
        light = TColor.fromFloat(0.05f, 0.05f, 0.1f);
        dark = TColor.fromFloat(0.95f, 0.8f, 0.9f);
        
        textColor = TColor.WHITE;
        
        widget = new TDefaultWidgetStyle(this);
        button = new TButtonStyle(this);
        menuButton = new TButtonStyle(this);
        toolButton = new TButtonStyle(this);
        tabButton = new TButtonStyle(this);
        dial = new TDialStyle(this);
        toolBar = new TToolBarStyle(this);
        
        // -----------------------------------------------------------
        
        menuButton.buttonColor(background);
        tabButton.buttonColor(light);
        
    }
    
}
