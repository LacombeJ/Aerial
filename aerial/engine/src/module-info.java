
module ax.engine {
    
	exports ax.engine.core;
	exports ax.engine.core.app;
	exports ax.engine.core.geometry;
	exports ax.engine.core.material;
	exports ax.engine.core.render;
	exports ax.engine.core.shaders;
	exports ax.engine.core.ui;
	exports ax.engine.utils;
	
	
	requires java.base;
	requires java.desktop;
	
	requires transitive ax.graphics;
	requires transitive ax.aui;
	requires ax.tea;
	requires transitive ax.commons;
	requires transitive ax.math;
	
}
