
module ax.tea {
    
    exports ax.tea;
    exports ax.tea.event;
    exports ax.tea.graphics;
    exports ax.tea.spatial;
    
    requires java.base;
    requires java.desktop;
    
    requires transitive ax.aui;
    requires transitive ax.graphics;
    
}
