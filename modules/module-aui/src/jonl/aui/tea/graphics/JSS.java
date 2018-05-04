package jonl.aui.tea.graphics;

import java.util.HashMap;

import jonl.aui.HAlign;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TWidget;
import jonl.jutils.call.Args;
import jonl.jutils.call.ParsedCall;
import jonl.jutils.call.Parser;
import jonl.jutils.jss.Style;
import jonl.vmath.Color;
import jonl.vmath.Mathd;
import jonl.vmath.Vector2;

public class JSS {

    
    static Style style(Style style, String sub) {
        Style subStyle = style.style(sub);
        if (subStyle!=null) {
            return subStyle;
        }
        return style;
    }
    
    static Style name(TWidget widget, Style style, TGraphics g) {
        String name = widget.name();
        if (!name.equals("")) {
            String id = "#"+name;
            Style idStyle = g.style().style(id);
            if (idStyle!=null) {
                Style append = style.copy();
                append.append(idStyle);
                return append;
            }
        }
        return style;
    }
    
    static void rect(TGraphics g, float x, float y, float width, float height, float border, float radius,
            ColorValue color, ColorValue borderColor) {
        
        Color cBot = color.color;
        Color cTop = color.color;
        Color bBot = borderColor.color;
        Color bTop = borderColor.color;
        if (color.type==ColorValue.LINEAR_GRADIENT) {
            cTop = color.top;
        }
        if (borderColor.type==ColorValue.LINEAR_GRADIENT) {
            bTop = borderColor.top;
        }
        
        g.renderBox(x,y,width,height,border,radius,cBot,cTop,bBot,bTop);
    }
    
    
    
    // Every non-private method should try to catch exceptions so that no errors are thrown for faulty style sheets
    // TODO handle displaying warnings for exception messages
    
    private static final HashMap<String,Color> NAMED_COLORS = new HashMap<>();
    static {
        NAMED_COLORS.put("red",Color.RED);
        NAMED_COLORS.put("green",Color.GREEN);
        NAMED_COLORS.put("blue",Color.BLUE);
        
        NAMED_COLORS.put("yellow",Color.YELLOW);
        NAMED_COLORS.put("orange",Color.ORANGE);
        NAMED_COLORS.put("purple",Color.PURPLE);
        NAMED_COLORS.put("pink",Color.PINK);
        
        NAMED_COLORS.put("cyan",Color.CYAN);
        
        NAMED_COLORS.put("white",Color.WHITE);
        NAMED_COLORS.put("black",Color.BLACK);

        NAMED_COLORS.put("lightgray",Color.LIGHT_GRAY);
        NAMED_COLORS.put("gray",Color.GRAY);
        NAMED_COLORS.put("darkgray",Color.DARK_GRAY);
        
        NAMED_COLORS.put("lightgrey",Color.LIGHT_GRAY);
        NAMED_COLORS.put("grey",Color.GRAY);
        NAMED_COLORS.put("darkgrey",Color.DARK_GRAY);
        
        NAMED_COLORS.put("darkred",Color.DARK_RED);
        NAMED_COLORS.put("darkgreen",Color.DARK_GREEN);
        NAMED_COLORS.put("darkblue",Color.DARK_BLUE);
        
        NAMED_COLORS.put("transparent",Color.TRANSPARENT);
    }
    private static Color colorBasic(String value) {
        if (NAMED_COLORS.containsKey(value)) {
            return NAMED_COLORS.get(value);
        }
        if (value.startsWith("#")) {
            return Color.fromHex(value);
        }
        ParsedCall call = Parser.call(value);
        Args arg = new Args(call.args);
        if (call.label.equals("rgb")) {
            int r = arg.getInt(0);
            int g = arg.getInt(1);
            int b = arg.getInt(2);
            return Color.fromInt(r,g,b);
        }
        if (call.label.equals("rgba")) {
            int r = arg.getInt(0);
            int g = arg.getInt(1);
            int b = arg.getInt(2);
            int a = arg.getInt(3);
            return Color.fromInt(r,g,b,a);
        }
        if (call.label.equals("rgbf")) {
            float r = arg.getFloat(0);
            float g = arg.getFloat(1);
            float b = arg.getFloat(2);
            return Color.fromFloat(r,g,b);
        }
        if (call.label.equals("rgbaf")) {
            float r = arg.getFloat(0);
            float g = arg.getFloat(1);
            float b = arg.getFloat(2);
            float a = arg.getFloat(3);
            return Color.fromFloat(r,g,b,a);
        }
        return null;
    }
    static ColorValue color(String value) {
        try {
            Color color = colorBasic(value);
            if (color!=null) {
                return new ColorValue(color);
            }
            if (value.startsWith("linear-gradient")) {
                ParsedCall call = Parser.call(value);
                Args arg = new Args(call.args);
                Color bot = colorBasic(arg.get(0));
                Color top = colorBasic(arg.get(1));
                return new ColorValue(bot,top);
            }
            return new ColorValue(Color.BLACK);
        }
        catch (Exception e) {
            return new ColorValue(Color.BLACK);
        }
    }
    
    private static final HashMap<String,HAlign> TEXT_ALIGN = new HashMap<>();
    static {
        TEXT_ALIGN.put("left",HAlign.LEFT);
        TEXT_ALIGN.put("right",HAlign.RIGHT);
        TEXT_ALIGN.put("center",HAlign.CENTER);
        
    }
    public static HAlign textAlign(String value) {
        HAlign align = TEXT_ALIGN.get(value);
        if (align!=null) {
            return align;
        }
        return HAlign.CENTER;
    }
    
    private static final HashMap<String,TFont> FONT = new HashMap<>();
    static {
        FONT.put("calibri",TFont.CALIBRI);
        
    }
    public static TFont font(String value) {
        TFont font = FONT.get(value);
        if (font!=null) {
            return font;
        }
        return TFont.CALIBRI;
    }
    
    static class ColorValue {
        public static final int COLOR = 0;
        public static final int LINEAR_GRADIENT = 1;
        
        public int type;
        public Color color;
        public Color top;
        
        public ColorValue(Color color) {
            type = COLOR;
            this.color = color;
        }
        
        public ColorValue(Color bot, Color top) {
            type = LINEAR_GRADIENT;
            this.color = bot;
            this.top = top;
        }
        
    }
    
    public static int pixels(String value) {
        try {
            if (value.endsWith("px")) {
                return Integer.parseInt(value.substring(0,value.length()-2));
            }
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static double percent(String value) {
        try {
            if (value.endsWith("%")) {
                double d =  Double.parseDouble(value.substring(0,value.length()-1));
                return Mathd.clamp(d, 0, 100) / 100.0;
            }
            return Mathd.clamp(Double.parseDouble(value), 0, 1);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static Vector2 vec2(String value) {
        try {
            ParsedCall call = Parser.call(value);
            Args args = new Args(call.args);
            float x = args.getFloat(0);
            float y = args.getFloat(1);
            return new Vector2(x,y);
        } catch (Exception e) {
            return new Vector2();
        }
    }
    
    public static int asInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static float asFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static double asDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static boolean asBoolean(String value) {
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static String innerString(String value) {
        try {
            if (value.charAt(0)=='"' && value.charAt(value.length()-1)=='"') {
                return value.substring(1,value.length()-1);
            }
        } catch (Exception e) {
            
        }
        return value;
    }
    
}
