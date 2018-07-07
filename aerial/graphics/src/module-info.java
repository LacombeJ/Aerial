
module ax.graphics {
    
	exports ax.graphics;
	exports ax.graphics.utils;
	exports ax.graphics.lwjgl;

	requires java.base;
	requires transitive java.desktop;
	
	requires org.lwjgl;
	requires org.lwjgl.opengl;
	requires org.lwjgl.openal;
	requires org.lwjgl.glfw;
	requires org.lwjgl.assimp;
	requires org.lwjgl.stb;
	
	requires transitive ax.commons;
    
}
