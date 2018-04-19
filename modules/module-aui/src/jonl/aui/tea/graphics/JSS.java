package jonl.aui.tea.graphics;

import java.util.HashMap;

import jonl.aui.HAlign;
import jonl.jutils.call.Args;
import jonl.jutils.call.ParsedCall;
import jonl.jutils.call.Parser;
import jonl.vmath.Color;
import jonl.vmath.Vector2;

public class JSS {

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
        if (value.startsWith("rgb")) {
            ParsedCall call = Parser.call(value);
            Args arg = new Args(call.args);
            int r = arg.getInt(0);
            int g = arg.getInt(1);
            int b = arg.getInt(2);
            return Color.fromInt(r,g,b);
        }
        if (value.startsWith("rgba")) {
            ParsedCall call = Parser.call(value);
            Args arg = new Args(call.args);
            int r = arg.getInt(0);
            int g = arg.getInt(1);
            int b = arg.getInt(2);
            int a = arg.getInt(3);
            return Color.fromInt(r,g,b,a);
        }
        if (value.startsWith("rgbf")) {
            ParsedCall call = Parser.call(value);
            Args arg = new Args(call.args);
            float r = arg.getFloat(0);
            float g = arg.getFloat(1);
            float b = arg.getFloat(2);
            return Color.fromFloat(r,g,b);
        }
        if (value.startsWith("rgbaf")) {
            ParsedCall call = Parser.call(value);
            Args arg = new Args(call.args);
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
    
    public static float asInt(String value) {
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
