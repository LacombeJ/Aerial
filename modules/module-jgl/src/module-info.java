
module jonl.jgl {
    
	exports jonl.jgl;
	exports jonl.jgl.utils;
	exports jonl.jgl.lwjgl;

	requires java.base;
	requires transitive java.desktop;
	
	requires lwjgl;
	requires lwjgl.opengl;
	
	requires transitive jonl.utils;
    
}
