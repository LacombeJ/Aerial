
module ax.aui {
    
	exports ax.aui;
	exports ax.aui.tea;
	exports ax.aui.tea.event;
	exports ax.aui.tea.graphics;
	exports ax.aui.tea.spatial;
	
	requires java.base;
    requires java.desktop;
	
	requires transitive ax.graphics;
	requires transitive ax.commons;
	requires transitive ax.math;
    
}
