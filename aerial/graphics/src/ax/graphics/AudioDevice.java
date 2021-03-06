package ax.graphics;

public interface AudioDevice {

    /** Creates context, make sure to call destroy when done */
    public void create();
    
    public void destroy();
    
    public AL getAudioLibrary();
    
}
