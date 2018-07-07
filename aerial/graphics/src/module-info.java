
module ax.graphics {
    
	exports ax.graphics;
	exports ax.graphics.utils;
	exports ax.graphics.lwjgl;

	requires java.base;
	requires transitive java.desktop;
	
	requires lwjgl;
	requires lwjgl.opengl;
	requires lwjgl.openal;
	requires lwjgl.glfw;
	requires lwjgl.stb;
	requires lwjgl.assimp;
	
	requires transitive ax.commons;
    
}
