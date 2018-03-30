package jonl.aui.tea.call;

import jonl.aui.tea.graphics.TColor;
import jonl.jutils.call.Args;

public class TArgTypes {

    public static TColor color(Args args) {
        String type = args.get(0);
        
        if (type.equals("f") || type.equals("4f")) {
            
            return TColor.fromFloat(args.getFloat(1), args.getFloat(2), args.getFloat(3), args.getFloat(4));
            
        } else if (type.equals("3f")) {
            
            return TColor.fromFloat(args.getFloat(1), args.getFloat(2), args.getFloat(3));
            
        } else if (type.equals("i") || type.equals("4i")) {
            
            return TColor.fromInt(args.getInt(1), args.getInt(2), args.getInt(3), args.getInt(4));
            
        } else if (type.equals("3i")) {
            
            return TColor.fromInt(args.getInt(1), args.getInt(2), args.getInt(3));
            
        }
        
        throw new IllegalArgumentException();
    }
    
}
