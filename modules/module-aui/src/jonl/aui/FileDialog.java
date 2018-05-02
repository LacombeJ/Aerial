package jonl.aui;

public interface FileDialog {

    enum State {
        APPROVE,
        CANCEL,
        ERROR
    }
    
    enum Mode {
        FILES_AND_DIRECTORIES,
        FILES_ONLY,
        DIRECTORIES_ONLY
    }
    
    void setMode(Mode mode);
    
    void setDirectory(String dir);
    
    void setFilter(String description, String... pattern);
    
    void addFilter(String description, String... pattern);
    
    State showOpenDialog();
    
    State showSaveDialog();
    
    String selected();
    
}
