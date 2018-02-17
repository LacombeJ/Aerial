
module jonl.ge {
    
	exports jonl.ge.core;
	exports jonl.ge.core.geometry;
	exports jonl.ge.core.material;
	exports jonl.ge.core.render;
	exports jonl.ge.ext;
	exports jonl.ge.utils;
	
	//requires java.base;
	//requires java.desktop;
	
	requires transitive jonl.jgl;
	requires transitive jonl.aui;
	requires transitive jonl.utils;
	requires transitive jonl.vmath;
	
}
