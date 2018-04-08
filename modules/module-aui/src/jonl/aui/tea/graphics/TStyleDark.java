package jonl.aui.tea.graphics;

import jonl.vmath.Color;

public class TStyleDark extends TStyleDefault {
    
    public TStyleDark() {
        
        primary = Color.fromFloat(0.2f, 0.2f, 0.2f);
        secondary = Color.fromFloat(0.3f, 0.3f, 0.31f);
        tertiary = Color.fromFloat(0.4f, 0.4f, 0.41f);
        
        background = Color.fromFloat(0.1f, 0.1f, 0.1f);
        foreground = Color.fromFloat(0.9f, 0.9f, 0.9f);
        
        light = Color.fromFloat(0.05f, 0.05f, 0.1f);
        dark = Color.fromFloat(0.8f, 0.8f, 0.8f);
        
        textColor = Color.WHITE;
        
        widget = new TDefaultWidgetStyle(this);
        button = new TButtonStyle(this);
        menuButton = new TButtonStyle(this);
        toolButton = new TButtonStyle(this);
        tabButton = new TButtonStyle(this);
        checkBox = new TCheckBoxStyle(this);
        dial = new TDialStyle(this);
        toolBar = new TToolBarStyle(this);
        
        // -----------------------------------------------------------
        
        menuButton.buttonColor(background);
        tabButton.buttonColor(light);
        
    }
    
}
