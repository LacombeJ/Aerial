
module ax.commons {
    
	exports ax.commons.structs;
	exports ax.commons.image;
	exports ax.commons;
	exports ax.commons.data;
	exports ax.commons.misc;
	exports ax.commons.io;
	exports ax.commons.jss;
	exports ax.commons.time;
	exports ax.commons.func;
	exports ax.commons.parallel;
	exports ax.commons.call;

	requires gson;
	requires java.sql;
	
	requires java.base;
	requires transitive java.desktop;
	
}
