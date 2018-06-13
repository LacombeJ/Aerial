
module jonl.ge {
    
	exports jonl.ge.core;
	exports jonl.ge.core.app;
	exports jonl.ge.core.geometry;
	exports jonl.ge.core.material;
	exports jonl.ge.core.render;
	exports jonl.ge.core.shaders;
	
	exports jonl.ge.mod.axis;
	exports jonl.ge.mod.fx;
	exports jonl.ge.mod.misc;
	exports jonl.ge.mod.physics;
	exports jonl.ge.mod.ray;
	exports jonl.ge.mod.render;
	exports jonl.ge.mod.render.pass;
	exports jonl.ge.mod.text;
	
	exports jonl.ge.utils;
	
	
	requires java.base;
	requires java.desktop;
	
	requires jbullet;
	requires vecmath;
	
	requires transitive jonl.jgl;
	requires transitive jonl.utils;
	requires transitive jonl.vmath;
	
}
