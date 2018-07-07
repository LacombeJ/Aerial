package ax.aui.tea.event;

public abstract class TEvent {

    public final TEventType type;
    
    TEvent(TEventType type) {
        this.type = type;
    }
    
}
