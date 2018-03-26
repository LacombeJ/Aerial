
module jonl.aui {
    
	exports jonl.aui;
	exports jonl.aui.tea;
	exports jonl.aui.tea.graphics;
	
	//requires java.base;
    //requires java.desktop;
	
	requires transitive jonl.jgl;
	requires transitive jonl.utils;
	requires transitive jonl.vmath;
    
}
