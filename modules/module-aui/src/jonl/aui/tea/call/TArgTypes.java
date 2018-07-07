package jonl.aui.tea.call;

import jonl.jutils.call.Args;
import jonl.vmath.Color;

public class TArgTypes {

    public static Color color(Args args) {
        String type = args.get(0);
        
        if (type.equals("f") || type.equals("4f")) {
            
            return Color.fromFloat(args.getFloat(1), args.getFloat(2), args.getFloat(3), args.getFloat(4));
            
        } else if (type.equals("3f")) {
            
            return Color.fromFloat(args.getFloat(1), args.getFloat(2), args.getFloat(3));
            
        } else if (type.equals("i") || type.equals("4i")) {
            
            return Color.fromInt(args.getInt(1), args.getInt(2), args.getInt(3), args.getInt(4));
            
        } else if (type.equals("3i")) {
            
            return Color.fromInt(args.getInt(1), args.getInt(2), args.getInt(3));
            
        }
        
        throw new IllegalArgumentException();
    }
    
}
