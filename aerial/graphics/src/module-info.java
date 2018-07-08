
module ax.graphics {
    
	exports ax.graphics;
	exports ax.graphics.utils;
	exports ax.graphics.lwjgl;

	requires java.base;
	requires transitive java.desktop;
	
	requires org.lwjgl;
	requires org.lwjgl.natives;
	requires org.lwjgl.opengl;
	requires org.lwjgl.opengl.natives;
	requires org.lwjgl.openal;
	requires org.lwjgl.openal.natives;
	requires org.lwjgl.glfw;
	requires org.lwjgl.glfw.natives;
	requires org.lwjgl.assimp;
	requires org.lwjgl.assimp.natives;
	requires org.lwjgl.stb;
	requires org.lwjgl.stb.natives;
	
	requires transitive ax.commons;
    
}
