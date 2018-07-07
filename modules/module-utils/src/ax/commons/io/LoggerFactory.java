package ax.commons.io;

import ax.commons.func.Callback;

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
