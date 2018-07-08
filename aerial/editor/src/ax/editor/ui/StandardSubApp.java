package ax.editor.ui;

import ax.aui.Widget;
import ax.std.fx.FXModule;
import ax.std.ray.RayModule;
import ax.std.render.RenderModule;
import ax.std.text.TextModule;

public class StandardSubApp extends SubApp {

	public StandardSubApp(ax.aui.Window window, Widget widget) {
		super(window, widget);

		add(new RenderModule());
		add(new TextModule());
		add(new FXModule());
		add(new RayModule());
	}

}
