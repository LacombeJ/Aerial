package jonl.aui.tea.call;

import java.util.HashMap;

import jonl.jutils.func.Function;

public class TCaller {

    private HashMap<String, Function<TArgs,Object>> functionMaps = new HashMap<>();
    
    public Object call(String call) {
        
        TParsedCall parsed = TParser.call(call);
        
        if (functionMaps.containsKey(parsed.label)) {
            
            Function<TArgs,Object> function = functionMaps.get(parsed.label);
            
            TArgs args = new TArgs(parsed.args);
            
            return function.f(args);
        }
        
        return null;
    }
    
    public void implement(String label, Function<TArgs,Object> function) {
        functionMaps.put(label, function);
    }
    
    public boolean implemented(String label) {
        return functionMaps.containsKey(label);
    }
    
    public Function<TArgs,Object> implementation(String label) {
        return functionMaps.get(label);
    }
    
}
