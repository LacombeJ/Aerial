package ax.tea.graphics;

import ax.aui.Info;
import ax.tea.TGraphics;
import ax.tea.TRadioButton;

public class RadioButtonRenderer {

    public static void paint(TRadioButton radioButton, TGraphics g, Info info) {
        ButtonBoxRenderer.paint(radioButton, "RadioButton", g, info);
    }
    
}
