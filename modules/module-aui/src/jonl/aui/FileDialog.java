package jonl.aui;

public interface FileDialog {

    enum State {
        APPROVE,
        CANCEL,
        ERROR
    }
    public static final State APPROVE   = State.APPROVE;
    public static final State CANCEL    = State.CANCEL;
    public static final State ERROR     = State.ERROR;
    
    enum Mode {
        FILES_AND_DIRECTORIES,
        FILES_ONLY,
        DIRECTORIES_ONLY
    }
    public static final Mode FILES_AND_DIRECTORIES  = Mode.FILES_AND_DIRECTORIES;
    public static final Mode FILES_ONLY             = Mode.FILES_ONLY;
    public static final Mode DIRECTORIES_ONLY       = Mode.DIRECTORIES_ONLY;
    
    void setMode(Mode mode);
    
    void setDirectory(String dir);
    
    void setFilter(String description, String... pattern);
    
    void addFilter(String description, String... pattern);
    
    State showOpenDialog();
    
    State showSaveDialog();
    
    String selected();
    
}
