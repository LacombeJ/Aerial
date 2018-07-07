package ax.aui;

public interface MessageDialog {

    enum State {
        APPROVE,
        REJECT,
        CANCEL,
        NONE,
    }
    
    enum Option {
        DEFAULT_OPTION,
        YES_NO_OPTION,
        YES_NO_CANCEL_OPTION,
        OK_CANCEL_OPTION
    }
    
    enum Message {
        PLAIN,
        INFORMATION,
        QUESTION,
        WARNING,
        ERROR
    }
    
    void setOption(Option option);
    
    void setMessage(Message message);
    
    State showDialog(Message message, String title, String text, Option option);
    
    State plain(String title, String text);
    
    State information(String title, String text);
    
    State question(String title, String text);
    
    State warning(String title, String text);
    
    State error(String title, String text);
    
}
