package jonl.jutils.call;

import java.util.HashMap;

import jonl.jutils.func.Function;

public class Caller {

    private HashMap<String, Function<Args,Object>> functionMaps = new HashMap<>();
    
    public Object call(String call) {
        
        ParsedCall parsed = Parser.call(call);
        
        if (functionMaps.containsKey(parsed.label)) {
            
            Function<Args,Object> function = functionMaps.get(parsed.label);
            
            Args args = new Args(parsed.args);
            
            return function.f(args);
        }
        
        return null;
    }
    
    public void implement(String label, Function<Args,Object> function) {
        functionMaps.put(label, function);
    }
    
    public boolean implemented(String label) {
        return functionMaps.containsKey(label);
    }
    
    public Function<Args,Object> implementation(String label) {
        return functionMaps.get(label);
    }
    
}
