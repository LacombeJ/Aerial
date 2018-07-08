package ax.tea;

import ax.aui.Signal;
import ax.aui.Timer;
import ax.commons.func.Callback0D;
import ax.commons.time.Time;

public class TTimer implements Timer {

    TWidget widget;
    long interval;
    boolean singleShot;
    
    private Signal<Callback0D> tick = new Signal<>();
    
    Time time = null;
    
    public TTimer(TWidget widget, long interval, boolean singleShot) {
        this.widget = widget;
        this.interval = interval;
        this.singleShot = singleShot;
        add();
    }
    
    public TTimer(TWidget widget, long interval) {
        this(widget,interval,false);
    }
    
    public TTimer(long interval) {
        this(null,interval);
    }
    
    @Override
    public long interval() {
        return interval;
    }

    @Override
    public boolean isSingleShot() {
        return singleShot;
    }
    
    @Override
    public Signal<Callback0D> tick() {
        return tick;
    }
    
    void add() {
        if (widget!=null) {
            widget.timers().add(this);
        } else {
            TUIManager.instance().timers().add(this);
        }
    }
    
    void remove() {
        if (widget!=null) {
            widget.timers().remove(this);
        } else {
            TUIManager.instance().timers().remove(this);
        }
    }
    
    void update() {
        if (time==null) {
            time = new Time();
        } else {
            long ms = time.elapsed();
            if (ms >= interval()) {
                time = new Time(); //Reset
                tick().emit((cb)->cb.f());
                if (singleShot) {
                    remove();
                }
            }
        }
    }
    
}
