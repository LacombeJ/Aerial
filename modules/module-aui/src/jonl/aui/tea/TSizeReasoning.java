package jonl.aui.tea;

import jonl.aui.tea.graphics.TFont;
import jonl.vmath.Mathi;

public class TSizeReasoning {

    public static final TFont FONT = TFont.CALIBRI;
    public static final int BORDER = 4;
    public static final int CHECKBOX_DIM = 12;
    public static final int DIAL_SIZE = 64;
    public static final int TAB_EXTRA = 16;
    
    public static TSizeHint button(TButton button, TFont font) {
        TSizeHint hint = new TSizeHint();
        if (button.icon()!=null) {
            hint.width = Math.max(hint.width, button.icon().width()) + BORDER;
            hint.height = Math.max(hint.height, button.icon().height()) + BORDER;
        }
        hint.width = Math.max(hint.width, (int) font.getWidth(button.text()) + 2*BORDER);
        hint.height = Math.max(hint.height, (int) font.getHeight() + BORDER);
        return hint;
    }
    
    public static TSizeHint button(TButton button) {
        return button(button, FONT);
    }
    
    public static TSizeHint checkBox(TCheckBox checkbox) {
        TSizeHint hint = new TSizeHint();
        if (checkbox.icon()!=null) {
            hint.width = Math.max(hint.width, checkbox.icon().width()) + BORDER;
            hint.height = Math.max(hint.height, checkbox.icon().height()) + BORDER;
        }
        hint.width = Math.max(hint.width, (int) FONT.getWidth(checkbox.text()) + 2*BORDER + CHECKBOX_DIM + BORDER);
        hint.height = Math.max(hint.height, (int) FONT.getHeight() + BORDER);
        return hint;
    }
    
    public static TSizeHint comboBox(TComboBox comboBox) {
        TSizeHint hint = new TSizeHint();
        hint.width = Math.max(hint.width, (int) FONT.getWidth(comboBox.text()) + 2*BORDER);
        hint.height = Math.max(hint.height, (int) FONT.getHeight() + BORDER);
        return hint;
    }
    
    public static TSizeHint dial(TDial dial) {
        return new TSizeHint(DIAL_SIZE, DIAL_SIZE);
    }
    
    public static TSizeHint label(TLabel label, TFont font) {
        TSizeHint hint = new TSizeHint();
        hint.width = font.getWidth(label.text()) + BORDER;
        hint.height = font.getHeight() + BORDER;
        return hint;
    }
    
    public static TSizeHint label(TLabel label) {
        return label(label, FONT);
    }
    
    public static TSizeHint lineEdit(TLineEdit lineEdit) {
        TSizeHint hint = new TSizeHint();
        hint.width = FONT.getWidth(lineEdit.text()) + BORDER;
        hint.height = FONT.getHeight() + BORDER;
        return hint;
    }
    
    public static TSizeHint tabButton(TTabButton button) {
        TSizeHint hint = new TSizeHint();
        if (button.icon()!=null) {
            hint.width = Math.max(hint.width, button.icon().width()) + BORDER;
            hint.height = Math.max(hint.height, button.icon().height()) + BORDER;
        }
        hint.width = Mathi.max(button.width(), hint.width, (int) FONT.getWidth(button.text()) + 4*BORDER + TAB_EXTRA); //For possible close button
        hint.height = Mathi.max(button.height(), hint.height, (int) FONT.getHeight() + BORDER);
        return hint;
    }
    
}
