package jonl.aui.tea.graphics;

import jonl.vmath.Color;

public interface TStyle {

    Color primary();
    Color secondary();
    Color tertiary();
    
    Color background();
    Color foreground();
    
    Color light();
    Color dark();
    
    Color textColor();
    
    TFont font();
    
    TDefaultWidgetStyle widget();
    TButtonStyle button();
    TButtonStyle menuButton();
    TButtonStyle toolButton();
    TButtonStyle tabButton();
    TCheckBoxStyle checkBox();
    TComboBoxStyle comboBox();
    TDialStyle dial();
    TToolBarStyle toolBar();
    
    // ------------------------------------------------------------------------
    
    // Setters
    
    void primary(Color primary);
    void secondary(Color secondary);
    void tertiary(Color tertiary);
    
    void background(Color light);
    void foreground(Color dark);
    
    void light(Color light);
    void dark(Color dark);
    
    void textColor(Color textColor);
    
    void font(TFont font);
    
    void widget(TDefaultWidgetStyle widget);
    void button(TButtonStyle button);
    void menuButton(TButtonStyle menuButton);
    void toolButton(TButtonStyle toolButton);
    void tabButton(TButtonStyle tabButton);
    void checkBox(TCheckBoxStyle checkBox);
    void comboBox(TComboBoxStyle comboBox);
    void dial(TDialStyle dial);
    void toolBar(TToolBarStyle toolBar);
    
}
