
module jonl.aui {
    
	exports ax.aui;
	exports ax.aui.tea;
	exports ax.aui.tea.event;
	exports ax.aui.tea.graphics;
	exports ax.aui.tea.spatial;
	
	requires java.base;
    requires java.desktop;
	
	requires transitive jonl.jgl;
	requires transitive jonl.utils;
	requires transitive jonl.vmath;
    
}
