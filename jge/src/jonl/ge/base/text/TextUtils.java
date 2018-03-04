package jonl.ge.base.text;

import jonl.ge.core.text.Font;
import jonl.ge.core.text.Text;
import jonl.jutils.misc.AwtFont;

class TextUtils {

    public static int toAwtFontType(int type) {
        switch (type) {
        case Font.PLAIN  : return java.awt.Font.PLAIN;
        case Font.BOLD   : return java.awt.Font.BOLD;
        case Font.ITALIC : return java.awt.Font.ITALIC;
        default: return java.awt.Font.PLAIN;
        }
    }
    
    public static int toAwtAlign(int align) {
        switch (align) {
        case Text.LEFT      : return AwtFont.HA_LEFT;
        case Text.CENTER    : return AwtFont.HA_CENTER;
        case Text.RIGHT     : return AwtFont.HA_RIGHT;
        default: return AwtFont.HA_LEFT;
        }
    }
    
}
