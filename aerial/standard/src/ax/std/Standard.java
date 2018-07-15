package ax.std;

import ax.engine.core.app.AbstractApplication;
import ax.std.fx.FXModule;
import ax.std.ray.RayModule;
import ax.std.render.RenderModule;
import ax.std.render.shadow.ShadowModule;
import ax.std.text.TextModule;

public class Standard {

    public static void addStandardModules(AbstractApplication app) {

        app.add(new RenderModule());
        app.add(new ShadowModule());
        app.add(new TextModule());
        app.add(new FXModule());
        app.add(new RayModule());

    }

}
