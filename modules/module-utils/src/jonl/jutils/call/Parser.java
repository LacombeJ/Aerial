package jonl.jutils.call;

import java.util.ArrayList;

import jonl.jutils.func.List;

public class Parser {
    
    public static ParsedCall call(String call) {
        int firstSpace = call.indexOf(' ');
        String label = call.substring(0, firstSpace);
        
        int firstParen = call.indexOf('(');
        int lastParen = call.lastIndexOf(')');
        String args = call.substring(firstParen, lastParen+1);
        
        return new ParsedCall(label,args);
    }
    
    public static ArrayList<String> args(String args)  {
        String sub = args.substring(1, args.length()-1);
        String[] split = sub.split(",");
        return List.list(split);
    }
    
}
