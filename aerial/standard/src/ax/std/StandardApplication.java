package ax.std;

import ax.engine.core.Application;
import ax.std.fx.FXModule;
import ax.std.ray.RayModule;
import ax.std.render.RenderModule;
import ax.std.text.TextModule;

public class StandardApplication extends Application {

    public StandardApplication() {
        super();
        
        add(new RenderModule());
        add(new TextModule());
        add(new FXModule());
        add(new RayModule());
    }
    
}
