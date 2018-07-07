package ax.commons.call;

import java.util.ArrayList;

public class Parser {
    
    public static ParsedCall call(String call) {
        int firstSpace = call.indexOf(' ');
        int firstParen = call.indexOf('(');
        
        if (firstSpace==-1) firstSpace = Integer.MAX_VALUE;
        
        int labelEnd = Math.min(firstSpace, firstParen);
        String label = call.substring(0, labelEnd);
        
        int lastParen = call.lastIndexOf(')');
        String args = call.substring(firstParen, lastParen+1);
        
        return new ParsedCall(label,args);
    }
    
    public static ArrayList<String> args(String args)  {
        String sub = args.substring(1, args.length()-1);
        ArgParser parser = new ArgParser(sub);
        return parser.args;
    }
    
    private static class ArgParser {
        
        static final char[] ROOT_IGNORES = {
            ' ',
            '\t'
        };
        
        ArrayList<String> args = new ArrayList<>();
        String collection = "";
        char[] contents;
        int index;
        
        ArgParser(String args) {
            contents = args.toCharArray();
            index = 0;
            parse();
        }
        
        boolean hasNext() {
            return index < contents.length;
        }
        
        char next() {
            return contents[index++];
        }
        
        char back() {
            index--;
            return contents[index];
        }
        
        void error(String string) {
            throw new IllegalStateException("Error parsing arguments: "+string);
        }
        
        boolean isMember(char c, char[] set) {
            for (char s : set) {
                if (c==s) return true;
            }
            return false;
        }
        
        void pop() {
            if (!collection.equals("")) {
                collection = collection.trim();
                args.add(collection);
                collection = "";
            }
        }
        
        void parse() {
            while (hasNext()) {
                char c = next();
                if (isMember(c,ROOT_IGNORES)) {
                    continue;
                }
                if (c==',') {
                    error("Argument is empty");
                }
                back();
                arg();
            }
            pop();
        }
        
        void arg() {
            while (hasNext()) {
                char c = next();
                if (checkEnclosure(c,'"','"')) {
                    continue;
                }
                if (checkEnclosure(c,'\'','\'')) {
                    continue;
                }
                if (checkEnclosure(c,'(',')')) {
                    continue;
                }
                if (checkEnclosure(c,'{','}')) {
                    continue;
                }
                if (checkEnclosure(c,'[',']')) {
                    continue;
                }
                if (c==',') {
                    pop();
                    return;
                }
                collection += c;
            }
        }
        
        boolean checkEnclosure(char c, char begin, char end) {
            if (c==begin) {
                collection += c;
                enclosure(end);
                return true;
            }
            return false;
        }
        
        void enclosure(char end) {
            while (hasNext()) {
                char c = next();
                collection += c;
                if (c==end) {
                    return;
                }
            }
        }
        
    }
    
}
