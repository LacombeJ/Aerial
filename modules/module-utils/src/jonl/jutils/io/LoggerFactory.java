package jonl.jutils.io;

import jonl.jutils.func.Callback;

public class LoggerFactory {

    public static Logger create(Callback<String> cbMessage) {
        return new ALogger() {
            @Override
            public void log(String message) {
                cbMessage.f(message);
            }
        };
    }
    
}
