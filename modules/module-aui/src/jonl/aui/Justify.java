package jonl.aui;

public enum Justify {

    TOP_LEFT(HAlign.LEFT,VAlign.TOP),
    
    TOP(HAlign.CENTER,VAlign.TOP),
    
    TOP_RIGHT(HAlign.RIGHT,VAlign.TOP),
    
    RIGHT(HAlign.RIGHT,VAlign.MIDDLE),
    
    BOTTOM_RIGHT(HAlign.RIGHT,VAlign.BOTTOM),
    
    BOTTOM(HAlign.CENTER,VAlign.BOTTOM),
    
    BOTTOM_LEFT(HAlign.LEFT,VAlign.BOTTOM),
    
    LEFT(HAlign.LEFT,VAlign.MIDDLE),
    
    CENTER(HAlign.CENTER,VAlign.MIDDLE);
    
    private final HAlign halign;
    private final VAlign valign;
    Justify(HAlign halign, VAlign valign) {
        this.halign = halign;
        this.valign = valign;
    }
    public HAlign halign() {
        return halign;
    }
    public VAlign valign() {
        return valign;
    }
    
}
