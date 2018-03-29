package jonl.aui.tea.graphics;

public interface TStyle {

    TColor primary();
    TColor secondary();
    TColor tertiary();
    
    TColor background();
    TColor foreground();
    
    TColor light();
    TColor dark();
    
    TColor textColor();
    
    TFont font();
    
    TDefaultWidgetStyle widget();
    TButtonStyle button();
    TButtonStyle menuButton();
    TButtonStyle toolButton();
    TButtonStyle tabButton();
    TDialStyle dial();
    TToolBarStyle toolBar();
    
    // ------------------------------------------------------------------------
    
    // Setters
    
    void primary(TColor primary);
    void secondary(TColor secondary);
    void tertiary(TColor tertiary);
    
    void background(TColor light);
    void foreground(TColor dark);
    
    void light(TColor light);
    void dark(TColor dark);
    
    void textColor(TColor textColor);
    
    void font(TFont font);
    
    void widget(TDefaultWidgetStyle widget);
    void button(TButtonStyle button);
    void menuButton(TButtonStyle menuButton);
    void toolButton(TButtonStyle toolButton);
    void tabButton(TButtonStyle tabButton);
    void dial(TDialStyle dial);
    void toolBar(TToolBarStyle toolBar);
    
}
