
module ax.engine {
    
	exports ax.engine.core;
	exports ax.engine.core.app;
	exports ax.engine.core.geometry;
	exports ax.engine.core.material;
	exports ax.engine.core.render;
	exports ax.engine.core.shaders;
	
	exports ax.engine.mod.axis;
	exports ax.engine.mod.fx;
	exports ax.engine.mod.misc;
	exports ax.engine.mod.physics;
	exports ax.engine.mod.ray;
	exports ax.engine.mod.render;
	exports ax.engine.mod.render.pass;
	exports ax.engine.mod.text;
	
	exports ax.engine.utils;
	
	
	requires java.base;
	requires java.desktop;
	
	requires jbullet;
	requires vecmath;
	
	requires transitive ax.graphics;
	requires transitive ax.commons;
	requires transitive ax.math;
	
}
